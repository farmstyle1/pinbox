package pinbox.com.pin;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.UserModel;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class LocationActivity extends AppCompatActivity {


    private Helper helper;
    private String cityName, user;
    private TextView txtLocation;
    private Button btnCheckin;
    private PinServiceApi pinServiceApi;
    private FrameLayout skipLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle bundle = getIntent().getExtras();
        cityName = bundle.getString("currentLocation");
        helper = new Helper(this);
        user = helper.getUsername();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pinServiceApi = retrofit.create(PinServiceApi.class);

        txtLocation = (TextView) findViewById(R.id.txtLocation);

        btnCheckin = (Button) findViewById(R.id.btn_checkin);

        txtLocation.setText(cityName);

        btnCheckin.setVisibility(View.VISIBLE);
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = new UserModel();
                userModel.setUsername(user);
                userModel.setLocation(cityName);
                Call<UserModel> call = pinServiceApi.updateLocation(userModel);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Response<UserModel> response) {
                        if (response.body().getStatus()) {
                            helper.setLocation(cityName);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(LocationActivity.this, "ไม่สามารถเชื่อมต่อกับเซิพเวอร์", Toast.LENGTH_SHORT).show();
                        Log.e("check", "throwable " + t);
                    }
                });

            }
        });


        skipLayout = (FrameLayout) findViewById(R.id.layout_skip);
        skipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


}
