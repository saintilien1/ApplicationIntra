package evaldy.applicationintra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends ArrayAdapter<listejson> {

    Context context;
    ArrayList<listejson> arrayList;

    public myAdapter(Context context, ArrayList<listejson> arraylist) {
        super(context, R.layout.custom, arraylist);

        this.arrayList = arraylist;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom,parent,false);
        TextView nom = convertView.findViewById(R.id.nom);
        nom.setText(arrayList.get(position).getNom());

        TextView adresse = convertView.findViewById(R.id.adresse);
        adresse.setText(arrayList.get(position).getAdresse());

        return convertView;
    }
}
