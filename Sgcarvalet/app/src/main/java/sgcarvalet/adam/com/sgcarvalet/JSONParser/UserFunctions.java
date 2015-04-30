package sgcarvalet.adam.com.sgcarvalet.JSONParser;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 1/1/15.
 */
public class UserFunctions {
    private JSONParser jsonParser;
    private static String loginURL = "http://sgcavarlet.maktekindo.net/index.php/dashboard/getDataUserJson";
    private static String sendURL = "http://sgcavarlet.maktekindo.net/index.php/dashboard/saveDataMaps";
    private static String cek_history_maps = "http://sgcavarlet.maktekindo.net/index.php/dashboard/getLoadHistoryMaps";
    private static String registerUser = "http://sgcavarlet.maktekindo.net/index.php/dashboard/saveRegister";
    private static String batalDataUrl = "http://sgcavarlet.maktekindo.net/index.php/dashboard/cancelData";

    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    public JSONObject loginUser(String tag,String email, String password){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    public JSONObject sendDataUser(String email, String password,String latitude, String longitude,String alamat){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("alamat", alamat));
        JSONObject json = jsonParser.getJSONFromUrl(sendURL, params);
        return json;
    }

    public JSONObject cekDataHistory(String email, String password){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(cek_history_maps, params);
        return json;
    }

    public JSONObject sendRegister(String nama, String email,String password, String hp){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("nama", nama));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("hp", hp));
        JSONObject json = jsonParser.getJSONFromUrl(registerUser, params);
        return json;
    }

    public JSONObject cancelDATAS(String id_mapss, String pesan){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id_mapss", id_mapss));
        params.add(new BasicNameValuePair("messageData", pesan));
        JSONObject json = jsonParser.getJSONFromUrl(batalDataUrl, params);
        //Log.e("BATAL DATA","BATAL KAN");
        return json;
    }

}
