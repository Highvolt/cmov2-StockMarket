package pt.fe.up.cmov.stockmarket;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import pt.fe.up.cmov.stockmarket.Stock;

public class StockAdapter extends ArrayAdapter<Stock> {
	Activity context;
	public StockAdapter(Context context, int resource, List<Stock> objects) {
		super(context, resource, objects);
		this.context=(Activity) context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null){
			Log.d("create new",""+position);
			View rowView= new StockItem(context);
			return rowView;
		}
		return convertView;
	}

	

}
