package pinbox.com.pin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import pinbox.com.pin.Adapter.MainPagerAdapter;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Helper.LocationHelper;
import pinbox.com.pin.Helper.UserHelper;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleApiClient googleApiClient;
    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Helper helper;
    private String lastLocation, currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build(); // if success,Call onConnected method.
        googleApiClient.connect();

        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);







    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onConnected(Bundle bundle) {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MainActivity.this);
        } else {
            Toast.makeText(MainActivity.this, "GPS ถูกปิด", Toast.LENGTH_SHORT).show();
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder gdc = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = gdc.getFromLocation(latitude, longitude, 1);
            currentLocation = addresses.get(0).getAdminArea();
            if (TextUtils.isEmpty(currentLocation)){
                currentLocation = addresses.get(0).getLocality();
            }
            /*
            getAddressLine(0) //ชื่อซอย หรือ ชื่อตำบล
            getLocality() //ชื่ออำเภอ
            getAdminArea() //ชื่อจังหวัด
            getCountryName() //ชื่อประเทศ
            getCountryCode()  //รหัสประเทศ
            getPostalCode() //รหัสไปรษณีย์
            */
            helper = new Helper(this);
            lastLocation = helper.getLocation();
            if(TextUtils.isEmpty(lastLocation)){

                Intent intent = new Intent(getApplication(), LocationActivity.class);
                intent.putExtra("currentLocation",currentLocation);
                startActivity(intent);
                googleApiClient.disconnect();
            }else{
                if (!lastLocation.equals(currentLocation)){
                    Intent intent = new Intent(getApplication(), LocationActivity.class);
                    intent.putExtra("currentLocation", currentLocation);
                    startActivity(intent);

                    googleApiClient.disconnect();
                }else{
                    googleApiClient.disconnect();
                }
            }
        } catch (Exception e) {
            Log.e("check", "Exep " + e);
            googleApiClient.disconnect();

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        googleApiClient.disconnect();
    }
}
