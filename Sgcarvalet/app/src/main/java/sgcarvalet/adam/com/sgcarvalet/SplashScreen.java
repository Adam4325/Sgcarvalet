package sgcarvalet.adam.com.sgcarvalet;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;
import sgcarvalet.adam.com.conection.CekInternetConection;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.User;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;


public class SplashScreen extends ActionBarActivity  {

    CekInternetConection cek;
    Boolean isInternetPresent= false;
    MySQLiteHelper MysqlDB;
    Button _btnTry;
    private static String KEY_SUCCESS = "SUKSES";
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    ProgressBar pgload;



    UserFunctions userFunctions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.bgSplash);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.getting_started) );
        }
        pgload = (ProgressBar)findViewById(R.id.pgLoad);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        MysqlDB     = new MySQLiteHelper(this);
        _btnTry     = (Button)findViewById(R.id.btnTry);
        _btnTry.setVisibility(View.GONE);
        cek = new CekInternetConection(SplashScreen.this);

        if (!cek.isConnectingInternet()) {
            pgload.setVisibility(View.GONE);
            _btnTry.setVisibility(View.VISIBLE);
            Toast.makeText(SplashScreen.this, "check your your connection!", Toast.LENGTH_SHORT).show();

        }else {
            new loadSplash().execute();
        }



        _btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cek.isConnectingInternet()) {
                    pgload.setVisibility(View.GONE);
                    _btnTry.setVisibility(View.VISIBLE);
                    Toast.makeText(SplashScreen.this, "check your your connection!", Toast.LENGTH_SHORT).show();
                }else {
                    new loadSplash().execute();
                }
            }
        });

    }

    class loadSplash extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgload.setVisibility(View.VISIBLE);
            _btnTry.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            isInternetPresent = cek.isConnectingInternet();


                SQLiteDatabase db = MysqlDB.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + MysqlDB.TABLE_USER, null);

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                User user = new User();

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    user.setId(Integer.parseInt(cursor.getString(0)));
                    user.setNama(cursor.getString(1));
                    user.setEmail(cursor.getString(2));
                    user.setPassword(cursor.getString(3));
                    user.setTelp(cursor.getString(4));
                    user.setStatus_logout(cursor.getString(5));

                    final static_user globalClass = (static_user) getApplicationContext();
                    globalClass.setEmail(user.getEmail());
                    globalClass.setNama(user.getNama());
                    globalClass.setTelpn(user.getTelp());
                    globalClass.setPassword(user.getPassword());


                    Log.d("email", cursor.getString(2));
                    Log.d("password MD5 :", user.getPassword());


                    userFunctions = new UserFunctions();
                    JSONObject json = userFunctions.loginUser("login", user.getEmail(), user.getPassword());

                    try {
                        if (json.getString(KEY_SUCCESS).equals("SUKSES") && json.getString(KEY_SUCCESS) != null) {
                            String res = json.getString(KEY_SUCCESS);
                            if (res.toString().equals("SUKSES")) {
                                Log.e("Json", "OK post data");
                                globalClass.setTelpn(json.getString("TELP").toString());
                                Intent in = new Intent(SplashScreen.this, NavHome.class);
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(SplashScreen.this, "Server not responding!", Toast.LENGTH_SHORT).show();
                            }

                        } else if ("EMPTY_ROW".equals(json.getString(KEY_SUCCESS))) {
                            MysqlDB.resetTables();
                        } else if ("EMPTY_ROW".equals(json.getString(KEY_SUCCESS))) {
                            Toast.makeText(SplashScreen.this, "check your email and password again!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SplashScreen.this, "check your your connection!", Toast.LENGTH_SHORT).show();
                            pgload.setVisibility(View.GONE);
                            _btnTry.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cursor.close();
                    db.close();
                } else {
                    Intent in = new Intent(SplashScreen.this, login.class);
                    startActivity(in);
                    Log.e("Login", "Intent");
                    finish();
                }

            return null;

        }
        protected void onPostExecute(Void result) {
            // dismiss the dialog once done
            isInternetPresent = cek.isConnectingInternet();

            if (!isInternetPresent) {
                pgload.setVisibility(View.GONE);
                _btnTry.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
        }
    }

}
