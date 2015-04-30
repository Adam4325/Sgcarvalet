package sgcarvalet.adam.com.sgcarvalet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;

import sgcarvalet.adam.com.sgcarvalet.JSONParser.UserFunctions;

/**
 * Created by adam on 1/14/15.
 */

public class Register extends Activity {
    private static String KEY_SUCCESS = "SUKSES";
    UserFunctions userFunctions;
    EditText nama,email,password,hp;
    ImageView imgReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nama = (EditText)findViewById(R.id.eNama);
        email = (EditText)findViewById(R.id.eMail);
        password = (EditText)findViewById(R.id.ePassword);
        hp = (EditText)findViewById(R.id.eHp);
        imgReg = (ImageView)findViewById(R.id.imgReg);

        imgReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regUser();
            }
        });
    }

    private void regUser(){
        userFunctions = new UserFunctions();
        JSONObject json = userFunctions.sendRegister(nama.getText().toString(),email.getText().toString(),password.getText().toString(),hp.getText().toString());
        //Log.e("VALUE JSON",nama.getText().toString()+"  "+email.getText().toString());
        try{

            if("".equals(nama.getText().toString()) || "".equals(nama.getText().equals(null))){
                Toast.makeText(Register.this, "name can't empty!", Toast.LENGTH_SHORT).show();
            }else if("".equals(email.getText().toString()) || "".equals(email.getText().equals(null))){
                Toast.makeText(Register.this, "email can't empty!", Toast.LENGTH_SHORT).show();
            }else if("".equals(password.getText().toString()) || "".equals(password.getText().equals(null))){
                Toast.makeText(Register.this, "password can't empty!", Toast.LENGTH_SHORT).show();
            }else if("".equals(hp.getText().toString()) || "".equals(hp.getText().equals(null))){
                Toast.makeText(Register.this, "Hp/ Telpon can't empty!", Toast.LENGTH_SHORT).show();
            }else {
                if ("SUKSES_DATA".equals(json.getString(KEY_SUCCESS))) {
                    String res = json.getString(KEY_SUCCESS);
                    if (res.toString().equals("SUKSES_DATA")) {
                        Toast.makeText(Register.this, "Register success!", Toast.LENGTH_SHORT).show();
                        Log.e("Json", "OK post data register");
                        Intent in = new Intent(Register.this, login.class);
                        startActivity(in);
                        finish();
                    }
                } else if ("SUKSES_EMAIL".equals(json.getString(KEY_SUCCESS))) {
                    Toast.makeText(Register.this, "E-mail has available,please change your email!", Toast.LENGTH_SHORT).show();
                } else if ("GAGAL_REGISTER".equals(json.getString(KEY_SUCCESS))) {
                    Toast.makeText(Register.this, "Failed to register, please contact admin!", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
