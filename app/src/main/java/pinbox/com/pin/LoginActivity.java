package pinbox.com.pin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.FriendModel;
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
                            helper.setUsername(userFacebook,response.body().getName());

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
    private void login() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        userModel.setName(Profile.getCurrentProfile().getFirstName());
                        Call<UserModel> call = pinServiceApi.newUser(userModel);
                        call.enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Response<UserModel> response) {

                                if (response.body().getStatus()) {


                                    helper.setUsername(userFacebook,userModel.getName());
                                    FriendModel friendModel = new FriendModel();
                                    friendModel.setUserID(userFacebook);
                                    friendModel.setFriendID(userFacebook);
                                    Call<FriendModel> newfriend = pinServiceApi.newFriend(friendModel);
                                    newfriend.enqueue(new Callback<FriendModel>() {
                                        @Override
                                        public void onResponse(Response<FriendModel> response) {
                                            Intent intent = new Intent(getApplication(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
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

                    }
                });
        request.executeAsync();
        //userModel.setName(Profile.getCurrentProfile().getFirstName());



        }



}
