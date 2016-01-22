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
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;


public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private final String KEY_LOCATION = "prefs_location";
    private Button skipButton;
    private GoogleApiClient googleApiClient;
    private List<Address> addresses;
    private String locationKey, cityName;
    private TextView txtlocation,txtCheckIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getApplicationContext().getSharedPreferences(KEY_LOCATION, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        setContentView(R.layout.activity_location);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        txtlocation = (TextView)findViewById(R.id.txtLocation);
        txtCheckIn = (TextView)findViewById(R.id.txtCheckIn);

        locationKey = mPrefs.getString("KEY_LOCATION", "");
        txtlocation.setText(locationKey);

        skipButton = (Button)findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
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
        if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, LocationActivity.this);
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
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
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
                commitLocation(cityName);
            } else {
                if (!cityName.equals(locationKey)) {
                    txtlocation.setText(cityName);
                    txtCheckIn.setText("ต้องการ Check-In หรือไม่");
                }
            }
        } catch (Exception e) {

            Log.e("check", "Exep " + e);
            googleApiClient.disconnect();

        }
    }

    private void commitLocation(String city) {
        Log.e("check", "commit location ");
        mEditor.putString("KEY_LOCATION", city);
        mEditor.commit();
    }


}
