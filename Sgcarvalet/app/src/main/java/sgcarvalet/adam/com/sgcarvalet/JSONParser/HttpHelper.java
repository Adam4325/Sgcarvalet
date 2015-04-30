package sgcarvalet.adam.com.sgcarvalet.JSONParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by adam on 1/2/15.
 */
public class HttpHelper {

    public static String getRequest(String url){
        String sret = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try{
            HttpResponse response = client.execute(request);
            sret = request(response);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  sret;
    }

    public static String request(HttpResponse response) {
        String result="";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                str.append(line+"\n");
            }
            in.close();result=str.toString();
        }catch(Exception ex){
            result  = "error";
        }
        return result;
    }
}
