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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Helper.LocationHelper;
import pinbox.com.pin.Helper.UserHelper;
import pinbox.com.pin.Model.Username;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LocationHelper locationHelper;
    private UserHelper userHelper;
    private GoogleApiClient googleApiClient;
    private List<Address> addresses;
    private String locationKey, cityName, user;
    private TextView txtLocation,txtCheckIn,txtSkip;
    private Switch swCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        userHelper = new UserHelper(this);
        user = userHelper.getUserID();
        Log.e("check","user "+user);
        locationHelper = new LocationHelper(this);
        locationKey = locationHelper.getLocation();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build(); // if success,Call onConnected method.

        txtLocation = (TextView)findViewById(R.id.txtLocation);
        txtCheckIn = (TextView)findViewById(R.id.txtCheckIn);
        swCheckin = (Switch)findViewById(R.id.switch_checkin);

        txtSkip = (TextView)findViewById(R.id.skip_button);
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            Log.e("check", "Disconnect");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);

        // Check GPS Available.
        if (locationAvailability.isLocationAvailable()) {


            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, LocationActivity.this);
            // Get location sent to onLocationChanged method.
        } else {
            Log.e("check", "GPS disable");
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("check", "failed " + connectionResult);
    }


    @Override
    public void onLocationChanged(final Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // Get City name.
        Geocoder gdc = new Geocoder(LocationActivity.this, Locale.ENGLISH);
        try {
            addresses = gdc.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getAdminArea();
            /*
            getAddressLine(0) //ชื่อซอย หรือ ชื่อตำบล
            getLocality() //ชื่ออำเภอ
            getAdminArea() //ชื่อจังหวัด
            getCountryName() //ชื่อประเทศ
            getCountryCode()  //รหัสประเทศ
            getPostalCode() //รหัสไปรษณีย์
            */
            if (TextUtils.isEmpty(locationKey)) {

            } else {
                if (!cityName.equals(locationKey)) {
                    txtLocation.setText(cityName);
                    txtCheckIn.setText("ต้องการ Check-In หรือไม่");
                    swCheckin.setVisibility(View.VISIBLE);
                    swCheckin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                locationHelper.setLocation(cityName);
                                Retrofit retrofit = new Retrofit.Builder()
                                        //.baseUrl("http://10.0.3.2:8080")
                                        .baseUrl("http://128.199.141.126:8080")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                PinServiceApi pinServiceApi = retrofit.create(PinServiceApi.class);
                                Username username = new Username(user,cityName);
                                Call<Username> call = pinServiceApi.updateLocation(username);
                                call.enqueue(new Callback<Username>() {
                                    @Override
                                    public void onResponse(Response<Username> response) {

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.e("check","throwable "+t);
                                    }
                                });

                            }else{

                            }
                        }
                    });
                }else{
                    txtLocation.setText(locationKey);
                }
            }
        } catch (Exception e) {

            Log.e("check", "Exep " + e);
            googleApiClient.disconnect();

        }
    }




}
