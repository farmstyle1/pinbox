package pinbox.com.pin.AddActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        friendName = (TextView)findViewById(R.id.friend_name);
        friendEditText = (EditText)findViewById(R.id.friend_id);
        friendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getApplication(), "กำลังค้นหา", Toast.LENGTH_SHORT).show();
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
                }else{
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
        Helper helper = new Helper(this);
        Call<UserModel> call = pinServiceApi.loadID(id);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Response<UserModel> response) {
               if(!TextUtils.isEmpty(response.body().getId())){
                   Log.d("check",response.body().getId());
                   friendName.setText(response.body().getName());
               }else{
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
