package sgcarvalet.adam.com.sgcarvalet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;


public class SettingUser extends Fragment {

//    Button btnLogout;
    TextView email1,nama1,telp;

    Activity context;
//    HttpPost httppost;
//    StringBuffer buffer;
//    HttpResponse response;
//    HttpClient httpclient;
//    ProgressDialog pd;
//    CustomAdapter adapter;
//    ListView listData;
//    ArrayList<String> records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getActivity().setTitle("PROFILE");
        View view = inflater.inflate(R.layout.setting_user, container, false);
        return view;

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getView();
        final static_user globalClass = (static_user) getActivity().getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        email1 = (TextView)v.findViewById(R.id.txtEmail);
        nama1 = (TextView)v.findViewById(R.id.txtNama);
        telp = (TextView)v.findViewById(R.id.txtPhone);

        final String namas = globalClass.getNama();
        final String emails = globalClass.getEmail();
        final String telps = globalClass.getTelpn();


        nama1.setText(namas);
        email1.setText(emails);
        telp.setText(telps);
        Log.e("NAMA:", (String) nama1.getText());

    }

//    public String  getJSONUrl(String url) {
//        StringBuilder strBuilder = new StringBuilder();
//        HttpClient hClient = new DefaultHttpClient();
//        HttpGet hGet = new HttpGet(url);
//
//        try{
//            HttpResponse response = hClient.execute(hGet);
//            StatusLine sLine = response.getStatusLine();
//            int statusCode = sLine.getStatusCode();
//            if(statusCode == 200){
//                HttpEntity entity = response.getEntity();
//                InputStream content = entity.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                String line;
//                while((line = reader.readLine()) != null){
//                    strBuilder.append(line);
//                }
//            }else{
//                Log.e("Log", "Failed to download result...");
//            }
//
//        }catch(ClientProtocolException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//        return strBuilder.toString();
//    }
}
