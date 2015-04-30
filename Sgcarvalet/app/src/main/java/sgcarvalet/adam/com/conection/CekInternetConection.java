package sgcarvalet.adam.com.conection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by adam on 1/1/15.
 */
public class CekInternetConection {

    private Context _context;
    //

    public CekInternetConection (Context context){
        this._context = context;
    }
    public CekInternetConection (){}

    public boolean isConnectingInternet() {


        ConnectivityManager connect = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connect != null)

        {

            NetworkInfo[] information = connect.getAllNetworkInfo();

            if (information != null)

                for (int x = 0; x < information.length; x++)

                    if (information[x].getState() == NetworkInfo.State.CONNECTED)

                    {

                        return true;

                    }

        }

        return false;

    }

    ConnectivityManager connectivityManager;
    NetworkInfo info;

    public boolean checkConnectionDua(Context context) {
        boolean flag = false;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                System.out.println(info.getTypeName());
                flag = true;
            }
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                System.out.println(info.getTypeName());
                flag = true;
            }
        } catch (Exception exception) {
            System.out.println("Exception at network connection....."
                    + exception);
        }
        return flag;
    }
}
