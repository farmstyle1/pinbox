package pinbox.com.pin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Helper.UserHelper;
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
    private UserHelper userHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_login);


        userHelper = new UserHelper(this);
        String userKeyLogin = userHelper.getUserID();
        // check user has found

        if (!TextUtils.isEmpty(userKeyLogin)){
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loginFacebookButton = (LoginButton) findViewById(R.id.login_facebook_button);

        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userFacebook = loginResult.getAccessToken().getUserId();
                userHelper.createSession(userFacebook);
                Retrofit retrofit = new Retrofit.Builder()
                        //.baseUrl("http://10.0.3.2:8080")
                        .baseUrl("http://128.199.141.126:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PinServiceApi pinServiceApi = retrofit.create(PinServiceApi.class);
                Username username = new Username(userFacebook,null);
                Call<Username> call = pinServiceApi.newUser(username);
                call.enqueue(new Callback<Username>() {
                    @Override
                    public void onResponse(Response<Username> response) {
                        Log.e("check","success");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("check","faill "+t);
                    }
                });
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
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



}
