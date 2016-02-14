package pinbox.com.pin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.UserModel;
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
    private String userFacebook;



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
                loginFacebookButton.setVisibility(View.GONE);
                userFacebook = loginResult.getAccessToken().getUserId();


                userModel.setUsername(userFacebook);
                Call<UserModel> callUser = pinServiceApi.loadUsername(userFacebook);
                callUser.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Response<UserModel> response) {
                        if(!TextUtils.isEmpty(response.body().getUsername())){
                            if(!TextUtils.isEmpty(response.body().getId())) {
                               helper.setId(response.body().getId());
                            }
                            helper.setUsername(userFacebook);

                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{

                            login();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getApplication(), "การเชื่อมต่อล้มเหลว", Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                        loginFacebookButton.setVisibility(View.VISIBLE);
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
    private void login(){

        Call<UserModel> call = pinServiceApi.newUser(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Response<UserModel> response) {
                if (response.body().getStatus()) {

                            userModel.setName(Profile.getCurrentProfile().getFirstName());
                            Call<UserModel> calls = pinServiceApi.updateName(userModel);
                            calls.enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Response<UserModel> response) {
                                    helper.setUsername(userFacebook);
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });



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
