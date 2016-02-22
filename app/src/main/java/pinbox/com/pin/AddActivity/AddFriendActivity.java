package pinbox.com.pin.AddActivity;

import android.support.v4.app.Fragment;
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


import com.facebook.login.widget.ProfilePictureView;


import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Fragment.FriendFragment;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.FriendModel;
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
    private pinbox.com.pin.Library.ProfilePictureView profilePictureView;
    private Button saveFriend;
    private PinServiceApi pinServiceApi;
    private String user;
    private FriendModel friendModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Helper helper = new Helper(getApplication());
        user = helper.getUsername();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pinServiceApi = retrofit.create(PinServiceApi.class);
        friendModel = new FriendModel();
        profilePictureView = (pinbox.com.pin.Library.ProfilePictureView) findViewById(R.id.friendProfilePicture);
        friendName = (TextView)findViewById(R.id.friend_name);
        saveFriend = (Button)findViewById(R.id.save_friend_button);
        saveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<FriendModel> callFriend = pinServiceApi.newFriend(friendModel);
                callFriend.enqueue(new Callback<FriendModel>() {
                    @Override
                    public void onResponse(Response<FriendModel> response) {
                        if (response.body().getStatus()){
                            saveFriend.setVisibility(View.GONE);
                            Toast.makeText(getApplication(), "เพิ่มในรายการเพิ่อน", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        friendEditText = (EditText)findViewById(R.id.friend_id);
        friendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchID(friendEditText.getText().toString());
                    return false;
                }
                return false;

            }
        });

    }

    private void searchID(String id){
        final Call<UserModel> call = pinServiceApi.loadID(id);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Response<UserModel> response) {
                if (!TextUtils.isEmpty(response.body().getId())) {
                    friendName.setText(response.body().getName());
                    friendModel.setUserID(user);
                    profilePictureView.setProfileId(response.body().getUsername());
                    if(!response.body().getUsername().equals(user)){
                        friendModel.setFriendID(response.body().getUsername());

                        Call<FriendModel> callFriend = pinServiceApi.findFriend(friendModel);
                        callFriend.enqueue(new Callback<FriendModel>() {
                            @Override
                            public void onResponse(Response<FriendModel> response) {

                                if(!response.body().getStatus()){
                                    saveFriend.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }

                } else {
                    friendName.setText("");
                    profilePictureView.setProfileId(null);
                    saveFriend.setVisibility(View.GONE);
                    Toast.makeText(getApplication(), "ไม่พบ ID ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("check", "faill " + t);
            }
        });
    }


}
