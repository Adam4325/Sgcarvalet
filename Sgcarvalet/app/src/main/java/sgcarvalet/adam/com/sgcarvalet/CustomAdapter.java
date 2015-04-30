package sgcarvalet.adam.com.sgcarvalet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adam on 1/7/15.
 */
public class CustomAdapter extends ArrayAdapter<M_History> {
    private int resource;
    private LayoutInflater inflater;
    private final Context context;
    private final ArrayList<M_History> itemsArrayList;

    public CustomAdapter(Context context,ArrayList<M_History> itemsArrayList){
        super(context,R.layout.activity_column ,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;


    }

    public View getView(int position, View convertView, ViewGroup parent){

          LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View rowView = inflater.inflate(R.layout.activity_column, parent, false);
          TextView tAddress = (TextView) rowView.findViewById(R.id.HAddress);
          TextView tLNG = (TextView) rowView.findViewById(R.id.HLNG);
          TextView tLTD = (TextView) rowView.findViewById(R.id.HLTD);
          TextView tWaktu = (TextView) rowView.findViewById(R.id.HWaktu);
          TextView id_mapss = (TextView) rowView.findViewById(R.id.id_mapss);

        tAddress.setText(itemsArrayList.get(position).getAddress());
        tLTD.setText(itemsArrayList.get(position).getLTD());
        tLNG.setText(itemsArrayList.get(position).getLNG());
        tWaktu.setText((String) itemsArrayList.get(position).getDtWaktu().toString());
        id_mapss.setText(itemsArrayList.get(position).getId_mapss());

        return rowView;
    }
}
