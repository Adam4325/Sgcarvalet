package sgcarvalet.adam.com.sgcarvalet;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;

public class Maps extends Fragment implements LocationListener {
    GoogleMap googleMap;
//    private String address;
//    private String city;
//    private String country;
//    private TextView tvLocation;
//    private static final float ACCURACY = 3.0f;
    ImageView btnSenData;
    double LONGITUDE, LATITUDE;
    private static String KEY_SUCCESS = "SUKSES";
    UserFunctions userFunctions;
    private SupportMapFragment mMapFragment;
    private final LatLng SINGAPURA = new LatLng(1.2808663,103.8454482);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.compassEnabled(true).camera(new CameraPosition(SINGAPURA, 13, 0f, 0f));
        mMapFragment = SupportMapFragment.newInstance(mapOptions);

        googleMap = mMapFragment.getMap();
        if(googleMap != null){
            googleMap.addMarker(new MarkerOptions().position(SINGAPURA).title("Sgcarvalet Singapore"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gps_maps, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getView();
        final static_user globalClass =(static_user)getActivity().getApplicationContext();
        btnSenData = (ImageView)v.findViewById(R.id.btnSendData);

        if (android.os.Build.VERSION.SDK_INT >8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

                btnSenData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                final static_user globalClass =(static_user)getActivity().getApplicationContext();

                int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
                getLocationMaps(status,0,0);
                final String email = globalClass.getEmail();
                final String password = globalClass.getPassword();
                LATITUDE=1.2808663;
                LONGITUDE=103.8454482;

                if (LONGITUDE==0.0 && LATITUDE==0.0){
                    Toast.makeText(getActivity(),"Wait location still searching ...!",Toast.LENGTH_LONG).show();
                }else{
                    String latitude = String.valueOf(LATITUDE).toString();
                    String longitude = String.valueOf(LONGITUDE).toString();
                    Toast.makeText(getActivity(),"Longitude :"+LONGITUDE+ " Latitude :"+ LATITUDE,Toast.LENGTH_LONG).show();
                    String alamat  = getCompleteAddress(LATITUDE,LONGITUDE);
                    Log.e("Kirim Data :","data :" + email+" "+ password +" "+latitude+" "+longitude+" ALamat "+ alamat);

                    try {
                        userFunctions = new UserFunctions();
                        JSONObject json =  userFunctions.sendDataUser(email, password, latitude, longitude,alamat);

                        String res = json.getString(KEY_SUCCESS);
                        if (json.getString(KEY_SUCCESS) != null && res.toString().equals("SUKSES") && res.toString().equals("SUKSES_DATA")){
                            Toast.makeText(getActivity(),"Sending data to server sucsess!",Toast.LENGTH_LONG);
                        }else if (res.toString().equals("GAGAL_DATA")){
                            Toast.makeText(getActivity(),"Sending data to server failed!",Toast.LENGTH_LONG);
                        }else if (res.toString().equals("EMPTY_ROW")){
                            Toast.makeText(getActivity(),"email and password not valid!",Toast.LENGTH_LONG);
                        }else{
                            Toast.makeText(getActivity(),"Sending data to server failed!",Toast.LENGTH_LONG);
                        }
                        Log.e("SUKSES TRY",(String) json.get(KEY_SUCCESS));
                    } catch (JSONException e) {

                       // Toast.makeText(this, String.format("Error %s", new Object[]{String.valueOf(e.printStackTrace())}),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                Log.e("LATITDE","latitude : "+LONGITUDE);
            }
        });


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        getLocationMaps(status,2000,0);
    }



    private String getCompleteAddress(double latitude,double longitude){
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
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
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private void getLocationMaps(int status,int min,int between) {
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
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
    public void onPause() {
        super.onPause();
        googleMap.getUiSettings().setCompassEnabled(false); //when onPause turn off sensor to save battery
        googleMap.setTrafficEnabled(false);
        getChildFragmentManager().beginTransaction()
                .remove(mMapFragment)
                .commit();
    }

    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        googleMap = mMapFragment.getMap();
        if(googleMap != null){
            googleMap.addMarker(new MarkerOptions().position(SINGAPURA).title("Sgcarvalet Singapore"));
        }
//        googleMap.getUiSettings().setCompassEnabled(true); //when onResume turn on sensor
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true); // if app was paused, take care of the my location button to be back appear
//        googleMap.setTrafficEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        LATITUDE = latitude;
        LONGITUDE = longitude;
        //updateAddressTextView();
    }

//    private void setUpMapIfNeeded() {
//        if (googleMap == null) {
//            googleMap = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//            //googleMap =
//        }
//        if (googleMap == null) {
//            return;
//        }
//    }

    @Override
    public void onProviderDisabled(String provider) {
// TODO Auto-generated method stub
        openSettingLocation();

    }

    private void openSettingLocation() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Enable Location")
                .setMessage("Please Active setting location GPS!?")
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }

    private void updateAddressTextView() {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude(); // returns latitude
        double longitude = gps.getLongitude(); // returns longitude
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
    public void onDestroyView()
    {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}