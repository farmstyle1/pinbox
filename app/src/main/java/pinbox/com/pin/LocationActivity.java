package pinbox.com.pin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;



public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private final String KEY_PREFS = "prefs_user";
    private Button clearButton;
    private GoogleApiClient googleApiClient;
    private List<Address> addresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getApplicationContext().getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        setContentView(R.layout.activity_location);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        clearButton = (Button)findViewById(R.id.clear_prefs);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.clear();
                mEditor.commit();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        Log.e("check", "Connect");

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.e("check", "GPS Call");
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()){
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest , this);
        }else{

        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("check","failed "+connectionResult);
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder gdc = new Geocoder(LocationActivity.this, Locale.ENGLISH);
        try {
            addresses = gdc.getFromLocation(latitude, longitude, 1);
            Log.e("check",addresses.get(0).getAdminArea());
            /*
            getAddressLine(0) //ชื่อซอย หรือ ชื่อตำบล
            getLocality() //ชื่ออำเภอ
            getAdminArea() //ชื่อจังหวัด
            getCountryName() //ชื่อประเทศ
            getCountryCode()  //รหัสประเทศ
            getPostalCode() //รหัสไปรษณีย์
            */
        }catch (Exception e){
            Log.e("check","Exep "+ e);
        }


    }
}
