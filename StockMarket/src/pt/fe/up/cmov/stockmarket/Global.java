package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Global extends Fragment {
	ImageView img;
	ListView list;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listvalues, container, false);
        img=(ImageView) rootView.findViewById(R.id.imageView1);
        list=(ListView) rootView.findViewById(R.id.listView12);
        ArrayList<String> a=new ArrayList<String>();
        Iterator<String> i=StockStorage.INSTANCE.wallet.keys();
        while(i.hasNext()){
        	
				try {
					Object obj=i.next();
					a.add(StockStorage.INSTANCE.wallet.getInt(obj.toString())+" - "+obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
        }
        
        
        ArrayAdapter<String> array=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,a);
        list.setAdapter(array);
        return rootView;
     }
}
