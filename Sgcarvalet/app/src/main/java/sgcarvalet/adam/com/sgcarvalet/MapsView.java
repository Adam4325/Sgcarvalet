package sgcarvalet.adam.com.sgcarvalet;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;

public class MapsView extends FragmentActivity implements LocationListener ,GooglePlayServicesClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    GoogleMap googleMap;
    MySQLiteHelper MysqlDB;
    ImageButton btnSenData;
    Button btnBack;
    double LONGITUDE, LATITUDE;
    private static String KEY_SUCCESS = "SUKSES";
    UserFunctions userFunctions;
    Context context;
    private String strAdd = "";
    static String alamat;
    private Location mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_view);

        final static_user globalClass =(static_user)getApplicationContext();
        btnSenData = (ImageButton)findViewById(R.id.btnSendData2);
        btnBack = (Button)findViewById(R.id.btnBack);
        MysqlDB     = new MySQLiteHelper(this);
        context = this;
        if (android.os.Build.VERSION.SDK_INT >8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MapsView.this,NavHome.class);
                startActivity(in);
            }
        });

        btnSenData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("YOUR REQUEST HAVE\n BEEN PROCESSED");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDataDialog();
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),"Sending data to server cancelled!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        getLocationMaps(status,2000,0);
    }

    public  void getDataDialog(){
        if (android.os.Build.VERSION.SDK_INT >8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final static_user globalClass =(static_user)getApplicationContext();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        getLocationMaps(status,0,0);
        final String email = globalClass.getEmail();
        final String password = globalClass.getPassword();

        if (LONGITUDE==0.0 && LATITUDE==0.0){
            Toast.makeText(getBaseContext(),"Wait location still searching ...!",Toast.LENGTH_LONG).show();
        }else{
            String latitude = String.valueOf(LATITUDE).toString();
            String longitude = String.valueOf(LONGITUDE).toString();

            alamat  = getCompleteAddress(LATITUDE,LONGITUDE);
            Log.e("Kirim Data :","data :" + email+" "+ password +" "+latitude+" "+longitude+" ALamat "+ alamat);

            try {
                userFunctions = new UserFunctions();
                JSONObject json =  userFunctions.sendDataUser(email, password, latitude, longitude,alamat);
                //if(json.getString(KEY_SUCCESS).equals("SUKSES")){
                    //String res = json.getString(KEY_SUCCESS);
                    if ("TWICE".equals(json.getString("SUKSES"))){
                        Toast.makeText(context,"You've done the demand pick-up!",Toast.LENGTH_LONG).show();
                        Log.e("Dari MAP","OK BANGET");
                    }else if ("SUKSES_DATA".equals(json.getString("SUKSES"))){
                        Toast.makeText(context,"Sending data to server sucsess!",Toast.LENGTH_LONG).show();
                        Log.e("Dari MAP","OK BANGET");
                    }else if ("GAGAL_DATA".equals(json.getString("SUKSES"))){
                        Toast.makeText(MapsView.this,"Sending data to server failed!",Toast.LENGTH_LONG).show();
                    }else if ("EMPTY_ROW".equals(json.getString("SUKSES"))){
                        Toast.makeText(MapsView.this,"email and password not valid!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MapsView.this,"Sending data to server failed!",Toast.LENGTH_LONG).show();
                    }

                Log.e("SUKSES TRY",(String) json.get(KEY_SUCCESS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e("LATITUDE","latitude : "+LONGITUDE);
    }
    private String getCompleteAddress(double latitude,double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address returnedAddress=null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());

            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Cant get Address!");
        }finally {
            if (returnedAddress==null){
                Toast.makeText(getBaseContext(),"Connected Off!",Toast.LENGTH_LONG).show();
            }
        }
        return strAdd;
    }

    private void getLocationMaps(int status,int min,int between) {
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps_view);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
                Criteria criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);

                criteria.setSpeedRequired(true);
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, min, between, this);
            }else{
                openSettingLocation();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleMap.getUiSettings().setCompassEnabled(false); //when onPause turn off sensor to save battery
        googleMap.setTrafficEnabled(false);
        //finish();
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        googleMap.getUiSettings().setCompassEnabled(true);
        //when onResume turn on sensor
        googleMap.getUiSettings().setMyLocationButtonEnabled(true); // if app was paused, take care of the my location button to be back appear
        googleMap.setTrafficEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()));
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        alamat = getCompleteAddress(latitude,longitude);

        if(alamat!=null){
            Log.e("Alamat Perubahan",alamat);
            marker.title(alamat).isVisible();

        }

        googleMap.addMarker(marker).showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        LATITUDE = latitude;
        LONGITUDE = longitude;
        //updateAddressTextView();
    }

    private void setUpMapIfNeeded() {

        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        if (googleMap == null) {
            return;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
    // TODO Auto-generated method stub
        openSettingLocation();

    }

    private void openSettingLocation() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Enable Location")
                .setMessage("Please Active/enable setting location your GPS!?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onProviderEnabled(String provider) {
    // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void updateAddressTextView() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude(); // returns latitude
        double longitude = gps.getLongitude(); // returns longitude
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
    // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("LONGITUDE LATITUDE :",latitude +" | "+ longitude);
        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition myCameraDefaultPositionAtStartup = new CameraPosition(latLng, 16, 60, 0);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myCameraDefaultPositionAtStartup));
    } // updateAddressTextView


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}