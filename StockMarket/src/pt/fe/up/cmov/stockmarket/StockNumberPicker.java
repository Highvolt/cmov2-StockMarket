package pt.fe.up.cmov.stockmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class StockNumberPicker extends DialogFragment {
	private NumberPicker np;

	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View root=inflater.inflate(R.layout.dialog_number, null);
	    builder.setView(root)
	    // Add action buttons
	           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	               
	            	   Intent i = new Intent();
	            	   i.putExtra("newVal", np.getValue());
		           	    
		           	    getTargetFragment().onActivityResult(getTargetRequestCode(), StockDetails.INT_CODE, i);

	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   StockNumberPicker.this.getDialog().cancel();
	               }
	           });     
	    
	    np = (NumberPicker) root.findViewById(R.id.numberPicker1);
	    np.setMaxValue(10000);
	    np.setMinValue(0);
	    Bundle bund=getArguments();
	    if(bund!=null){
	    	np.setValue(bund.getInt("start", 0));
	    }else{
	    	np.setValue(0);
	    }
	    return builder.create();
	}
}
