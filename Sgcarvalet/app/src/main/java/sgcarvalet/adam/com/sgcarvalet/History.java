package sgcarvalet.adam.com.sgcarvalet;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sgcarvalet.adam.com.conection.CekInternetConection;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;

/**
 * Created by adam on 1/7/15.
 */
public class History extends Fragment {

    CekInternetConection cek;
    boolean isInternetPresent = false;
    private ProgressDialog pDialog;
    SearchView tCari;

    JSONArray jsonArray;
    private static String SUKSES = "sukses";
    private static String ARRAY_MAPS = "maps";
    private static String ALAMAT = "address";
    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";
    private static String ID_MAPSS = "id_maps";
    UserFunctions userFunctions;
    private EditText txtPesan;

    private static String KEY_S1 = "1";
    private static String KEY_S2 = "2";
    private static String KEY_S3 = "3";
    private static String selected;
    Context context;

    ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //loadData();
        View view = inflater.inflate(R.layout.history_user, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getView();
        final static_user globalClass = (static_user) getActivity().getApplicationContext();
        //txtPesan = (EditText) v.findViewById(R.id.txtPesan);

        //tCari = (SearchView)v.findViewById(R.id.txtCari);
        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        loadData();

    }


    JSONObject json;
    private void showCancelDialog() {

        LayoutInflater li = LayoutInflater.from(this.getActivity());
        final View slideView = li.inflate(R.layout.cancel_data, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(slideView);
        alertDialogBuilder
                .setPositiveButton("Send Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtPesan = (EditText)slideView.findViewById(R.id.txtPesan);
                        Log.e("Data JSON",txtPesan.getText().toString());
                        try {
                            userFunctions = new UserFunctions();
                            json = userFunctions.cancelDATAS(selected.toString(), txtPesan.getText().toString());
                            if ("1".equals(json.getString("SUKSES")) || "2".equals(json.getString("SUKSES"))) {
                                Toast.makeText(getActivity().getApplicationContext(), "Data already sent!", Toast.LENGTH_LONG).show();
                                Log.e("SUKSES:","Data already send");
                            } else if ("3".equals(json.getString("SUKSES"))) {
                                Toast.makeText(getActivity().getApplicationContext(), "Cancellation send data failed!", Toast.LENGTH_LONG).show();
                                Log.e("SUKSES:","Data failed send");
                            } else if ("error".equals(json.getString("SUKSES"))) {
                                Toast.makeText(getActivity().getApplicationContext(), "Data error in send!", Toast.LENGTH_LONG).show();
                                Log.e("SUKSES:","Data error send");
                            }
                            loadData();
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "Data was Cancell!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadData() {
        CustomAdapter adapter = new CustomAdapter(getActivity(),generateData());
        View v = getView();
        ListView lstView = (ListView)v.findViewById(R.id.lstHistory);
        lstView.setAdapter(adapter);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected = ((TextView) view.findViewById(R.id.id_mapss)).getText().toString();
                Log.e("ID MAPS:",selected.toString());
                showCancelDialog();
            }
        });
    }

    private ArrayList<M_History> generateData(){
        final static_user globalClass =(static_user)getActivity().getApplicationContext();
        final String email = globalClass.getEmail();
        final String password = globalClass.getPassword();
        ArrayList<M_History> items = new ArrayList<M_History>();
        cek = new CekInternetConection(getActivity());
        isInternetPresent = cek.isConnectingInternet();

        if (isInternetPresent){
            UserFunctions userFunctions = new UserFunctions();
            JSONObject json =  userFunctions.cekDataHistory(email, password);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                String res = json.getString(SUKSES);
                if ("1".equals(res)){
                    jsonArray = json.getJSONArray(ARRAY_MAPS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String alamat = jsonObject.getString(ALAMAT);
                        String latitude = jsonObject.getString(LATITUDE);
                        String longitude = jsonObject.getString(LONGITUDE);
                        String id_mapss = jsonObject.getString(ID_MAPSS);
                        String dates = jsonObject.getString("create_date");
                        Log.d("Alamat :",alamat);
                        try {
                            Date dtWaktu = formatter.parse(dates);
                            items.add(new M_History("Address :"+alamat,"Latitude :"+latitude,"Longitude :"+longitude,dtWaktu,id_mapss));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("JSON SUKSES : ", ""+res);
                }else if("0".equals(res)){
                    Toast.makeText(getActivity(),"No Data Display this time!",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getActivity(),"Check your connection!",Toast.LENGTH_SHORT).show();
        }


        return items;
    }

    private class AsyncDATA extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                //super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Loading ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            cek = new CekInternetConection(getActivity());
            isInternetPresent = cek.isConnectingInternet();

            if (isInternetPresent){
                loadData();
            }else{
                Log.e("DATA :","Can't retrieve data!");
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            pDialog.dismiss();


        }
    }
}
