package pinbox.com.pin;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import pinbox.com.pin.Adapter.MainPagerAdapter;
import pinbox.com.pin.Helper.Helper;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleApiClient googleApiClient;
    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Helper helper;
    private String lastLocation, currentLocation;
    private TextView txtBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBar = (TextView)findViewById(R.id.txt_bar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_friend));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_board));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_event));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);




        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build(); // if success,Call onConnected method.
        googleApiClient.connect();

        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        txtBar.setText("เพื่อน");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    txtBar.setText("Friend");
                }else if (tab.getPosition()==1){
                    txtBar.setText("Board");
                }else if (tab.getPosition()==2){
                    txtBar.setText("Event");
                }else if (tab.getPosition()==3){
                    txtBar.setText("Option");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });







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
