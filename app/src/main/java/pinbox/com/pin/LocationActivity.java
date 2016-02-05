package pinbox.com.pin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Helper.LocationHelper;
import pinbox.com.pin.Helper.UserHelper;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.Model.Username;
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
