package pinbox.com.pin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginFacebookButton;
    private Button loginButton;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private final String KEY_PREFS = "prefs_user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mPrefs = getApplicationContext().getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        setContentView(R.layout.activity_login);



        // check user has found
        String userKeyLogin = mPrefs.getString("KEY_USER","");
        if (!TextUtils.isEmpty(userKeyLogin)){
            Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
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
                checkPrefsKey(userFacebook);
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
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

    private void checkPrefsKey(String userLogin) {
        //หาค่า KEY_PREFS แล้วเช็คว่าว่างหรือไม่
        /*String userKeyLogin = mPrefs.getString("KEY_USERNAME","");
        if (TextUtils.isEmpty(userKeyLogin)){
            Log.e("check","test");
        }
        */
        mEditor.putString("KEY_USER", userLogin);
        mEditor.commit();
    }


}
