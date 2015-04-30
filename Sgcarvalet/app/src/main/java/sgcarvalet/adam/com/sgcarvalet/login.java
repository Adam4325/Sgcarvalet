package sgcarvalet.adam.com.sgcarvalet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;
import sgcarvalet.adam.com.conection.CekInternetConection;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.User;
import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;
import sgcarvalet.adam.com.sgcarvalet.adam.com.security.Secure;

public class login extends ActionBarActivity {
    CekInternetConection cek;
    boolean isInternetPresent= false;
    MySQLiteHelper MysqlDB;
    UserFunctions userFunctions;
    Secure secure;
    private static String KEY_SUCCESS = "SUKSES";
    private static String KEY_NAMA = "NAMA";
    private static String KEY_TELP = "TELP";

    ImageButton imLogin;
    //ImageView mapIcon;
    private EditText emailNew, passwordNew;
    TextView txtRegister,eLocation,txtHave;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        emailNew        = (EditText)findViewById(R.id.txtEmail);
        passwordNew     = (EditText)findViewById(R.id.txtPassword);
        imLogin         = (ImageButton)findViewById(R.id.mgLogin);
        txtRegister     = (TextView)findViewById(R.id.txtRegister);
        //mapIcon         = (ImageView)findViewById(R.id.mapIcon);
        //eLocation       = (TextView)findViewById(R.id.eLocation);
        txtHave       = (TextView)findViewById(R.id.textView6);

//        mapIcon.setVisibility(View.GONE);
//        eLocation.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        cek = new CekInternetConection(login.this);

        MysqlDB     = new MySQLiteHelper(this);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(login.this,Register.class);
                startActivity(in);
            }
        });

        imLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cek.isConnectingInternet();
//
                if (!isInternetPresent){
                    //_btnTry.setVisibility(View.VISIBLE);
                    Toast.makeText(login.this,"Cek your connection!",Toast.LENGTH_SHORT).show();
                }else{
                    String email = emailNew.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    userFunctions = new UserFunctions();
                    JSONObject json = userFunctions.loginUser("login",emailNew.getText().toString(),secure.md5(passwordNew.getText().toString()));
                    try{
                        if("SUKSES".equals(json.getString(KEY_SUCCESS))){
                            String res = json.getString(KEY_SUCCESS);
                            User user = new User();
                            user.setNama((String) json.get(KEY_NAMA));
                            user.setEmail(emailNew.getText().toString());
                            user.setPassword(secure.md5(passwordNew.getText().toString()));
                            user.setTelp((String)json.get(KEY_TELP));
                            user.setStatus_logout("1");

                            final static_user globalClass = (static_user) getApplicationContext();
                            globalClass.setEmail(user.getEmail());
                            globalClass.setNama(user.getNama());
                            globalClass.setTelpn(user.getTelp());
                            globalClass.setPassword(user.getPassword());
                            //Toast.makeText(login.this, "Registrasi success!", Toast.LENGTH_SHORT).show();
                            Log.d("nama", (String) json.get(KEY_NAMA)+" ID: "+user.getEmail());
                            MysqlDB.addUser(user);

                            new loadRegisterDo().execute();

                        }else if("EMPTY_ROW".equals(json.getString(KEY_SUCCESS))){
                            Toast.makeText(login.this,"check your email and password again!",Toast.LENGTH_SHORT).show();
                        }else if(!email.matches(emailPattern)){
                            Toast.makeText(login.this,"email not valid!",Toast.LENGTH_SHORT).show();
                        }else if(emailNew.getText().toString().equals("") && passwordNew.getText().toString().equals("")){
                            Toast.makeText(login.this,"email and password can't empty!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(login.this, "error!", Toast.LENGTH_SHORT).show();
                            MysqlDB.resetTables();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                }

        });
    }


    ProgressDialog pDialog;
    class loadRegisterDo extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Intent in = new Intent(login.this,NavHome.class);
            startActivity(in);
            finish();
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}