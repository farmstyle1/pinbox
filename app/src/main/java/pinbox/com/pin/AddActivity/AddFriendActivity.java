package pinbox.com.pin.AddActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddFriendActivity extends AppCompatActivity {
    private ImageView searchImage;
    private EditText friendEditText;
    private TextView friendName;
    private ProfilePictureView profilePictureView;
    private Button saveFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        profilePictureView = (ProfilePictureView)findViewById(R.id.friendProfilePicture);
        friendName = (TextView)findViewById(R.id.friend_name);
        saveFriend = (Button)findViewById(R.id.save_friend_button);
        friendEditText = (EditText)findViewById(R.id.friend_id);
        friendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchID(friendEditText.getText().toString());
                    return true;
                }
                return false;

            }
        });
        searchImage = (ImageView)findViewById(R.id.search_friend);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendEditText.getText().toString().matches("")) {
                    Toast.makeText(getApplication(), "ไม่พบ ID ดังกล่าว", Toast.LENGTH_SHORT).show();
                } else {
                    searchID(friendEditText.getText().toString());
                }


            }
        });
    }

    private void searchID(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PinServiceApi pinServiceApi = retrofit.create(PinServiceApi.class);
        Call<UserModel> call = pinServiceApi.loadID(id);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Response<UserModel> response) {
                if (!TextUtils.isEmpty(response.body().getId())) {

                    friendName.setText(response.body().getName());
                    profilePictureView.setVisibility(View.VISIBLE);
                    saveFriend.setVisibility(View.VISIBLE);
                    profilePictureView.setProfileId(response.body().getUsername());
                } else {
                    friendName.setText("");
                    profilePictureView.setProfileId(null);
                    saveFriend.setVisibility(View.GONE);
                    Toast.makeText(getApplication(), "ไม่พบ ID ดังกล่าว", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("check", "faill " + t);
            }
        });
    }


}
