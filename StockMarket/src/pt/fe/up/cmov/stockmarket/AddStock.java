package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

public class AddStock extends DialogFragment {
	private Spinner spiner;
	private EditText custom;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View root = inflater.inflate(R.layout.dialog_addstock, null);
		builder.setView(root)
				// Add action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// sign in the user ...

						Intent i = new Intent();
						// i.putExtra("newVal", np.getValue());

						// getActivity().onActivityResult(getTargetRequestCode(),
						// StockDetails.INT_CODE, i);

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AddStock.this.getDialog().cancel();
							}
						});

		spiner = (Spinner) root.findViewById(R.id.addStockDefaults);
		ArrayList<String> a = new ArrayList<String>();

		a.add("IBM");
		a.add("MSFT");
		a.add("CSCO");
		a.add("AMZN");
		a.add("HPQ");
		a.add("GOOG");
		a.add("AAPL");
		a.add("ORCL");
		a.add("MSI");
		a.add("AMD<");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, a);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spiner.setAdapter(adapter);
		custom = (EditText) root.findViewById(R.id.addStockText);
		Bundle bund = getArguments();
		if (bund != null) {

		} else {

		}
		return builder.create();
	}
	
	BroadcastReceiver br=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getStringExtra("source")!=null && intent.getStringExtra("source").equals(str.toUpperCase())){
				boolean ok=intent.getBooleanExtra("ok", false);
				if(ok){
					dismiss();
					
					if(StockStorage.INSTANCE.addStock(s)){
						Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
						Intent i = new Intent();
		            	   i.putExtra("newVal",s.quote);
			           	    
			           	    ((MainActivity)getActivity()).onActivityResult(MainActivity.INT_CODE, MainActivity.INT_CODE, i);

					}else{
						Toast.makeText(getActivity(), "Already on the list", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "Invalid Stock", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	String str=null;
	Stock s=null;
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(br);
	}
	
	@Override
	public void onStart()
	{
	    super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
	    getActivity().registerReceiver(br, new IntentFilter(Stock.detailsAction));
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d != null)
	    {
	        Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
	                {
	                    @Override
	                    public void onClick(View v)
	                    {
	                    	
	                    	if(custom.getText().length()>0){
	                    		str=custom.getText().toString().toUpperCase();
	                    		
	                    		
	                    	}else{
	                    		str=((String)spiner.getSelectedItem());
	                    	}
	                    	Toast.makeText(getActivity(), "Checking for <"+str+">", Toast.LENGTH_SHORT).show();
	                    	s=new Stock(str);
	                        Boolean wantToCloseDialog = false;
	                        //Do stuff, possibly set wantToCloseDialog to true then...
	                        if(wantToCloseDialog)
	                            dismiss();
	                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
	                    }
	                });
	    }
	}
}
