package pinbox.com.pin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Helper.UserHelper;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.Model.Username;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginFacebookButton;
    private Button loginButton;
    private Helper helper;
    private UserModel userModel;
    private PinServiceApi pinServiceApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);




        helper = new Helper(this);
        String userKeyLogin = helper.getUsername();
        // check user has found

        if (!TextUtils.isEmpty(userKeyLogin)){
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       pinServiceApi = retrofit.create(PinServiceApi.class);

        userModel = new UserModel();

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginFacebookButton = (LoginButton) findViewById(R.id.login_facebook_button);
        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                String userFacebook = loginResult.getAccessToken().getUserId();
                helper.setUsername(userFacebook);

                userModel.setUsername(userFacebook);
                Call<UserModel> callUser = pinServiceApi.loadUsername(userFacebook);
                callUser.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Response<UserModel> response) {
                        if(!TextUtils.isEmpty(response.body().getUsername())){
                            if(!TextUtils.isEmpty(response.body().getId())) {
                               helper.setId(response.body().getId());
                            }

                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            login(loginResult);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });



            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }
    private void login(final LoginResult loginResult){
        Log.d("check", "Login function");
        Call<UserModel> call = pinServiceApi.newUser(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Response<UserModel> response) {
                Log.d("check", "Login Success ");
                if (response.body().getStatus()) {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            //Log.e("check", Profile.getCurrentProfile().getFirstName());
                            userModel.setName(Profile.getCurrentProfile().getFirstName());
                            Call<UserModel> calls = pinServiceApi.updateName(userModel);
                            calls.enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Response<UserModel> response) {

                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });

                        }
                    });
                    request.executeAsync();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("check", "faill " + t);
            }
        });

        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }



}
