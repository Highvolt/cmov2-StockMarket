package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StockDetails extends android.app.Fragment implements OnTouchListener, OnClickListener,OnLongClickListener{
	ImageView r=null;
	Bitmap b=null;
	Canvas c=null;
	ValueAnimator v=null;
	HorizontalScrollView sr=null;
	Path path=null;
	BroadcastReceiver br=null;
	Stock s=null;
	ArrayList<Double> history=null;
	Double historyMax;
	Double historyMin;
	ArrayList<Integer> volume=null;
	Integer volumeMin;
	Integer volumeMax;
	TextView value=null;
	TextView name=null;
	TextView quote=null;
	TextView percentage=null;
	TextView diff=null;
	ImageView changeState=null;
	private TextView wallet;
	
	private void updateDetails(){
		this.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(s!=null){
					name.setText(s.name);
					value.setText("$"+s.value); //TODO correct coin
					quote.setText(s.quote.toUpperCase());
					percentage.setText(Double.toString(s.percentage)+"%");
					if(s.change>0){
						percentage.setTextColor(getResources().getColor(R.color.rising));
						diff.setTextColor(getResources().getColor(R.color.rising));
						changeState.setImageResource(R.drawable.stock_up);
						diff.setText("+ $"+Double.toString(s.change)); //TODO correct coin
					}else if(s.change<0){
						percentage.setTextColor(getResources().getColor(R.color.falling));
						diff.setTextColor(getResources().getColor(R.color.falling));
						changeState.setImageResource(R.drawable.stock_down);
						diff.setText("- $"+Double.toString(-s.change)); //TODO correct coin
					}else{
						percentage.setTextColor(getResources().getColor(R.color.equals));
						diff.setTextColor(getResources().getColor(R.color.equals));
						changeState.setImageResource(R.drawable.equals);
						diff.setText("$"+Double.toString(s.change)); //TODO correct coin
					}
				}
				
			}
		});
	}
	
	
	
	void init(){
		
		br = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String tmp=intent.getStringExtra("source");
				if(tmp!=null && s!=null && tmp.equalsIgnoreCase(s.quote)){
					Log.d("StockUpdate On Details", "valores"+s.quote+" "+s.value);
					//updateData();
					//if(intent.getAction().equals(Stock.historyUpdateAction)){
					//updateBitmap();
					//}
					//Stoc.this.invalidate();
					
					if(intent.getAction().equals(Stock.historyUpdateAction)){
						updateGraphAnim();
					}
					updateDetails();
				} 
				
			}
		};
		StockStorage.INSTANCE.getApplicationContext().registerReceiver(br, new IntentFilter(Stock.instantUpdateAction));
		StockStorage.INSTANCE.getApplicationContext().registerReceiver(br, new IntentFilter(Stock.historyUpdateAction));
		
	}
	
	
	@Override
	public void onDestroy() {
		if(br!=null)
			StockStorage.INSTANCE.getApplicationContext().unregisterReceiver(br);
		super.onDestroy();
	}
	
	
	private void updateGraphAnim(){
		if(s!=null && s.history!=null){
			history=new ArrayList<Double>();
			volume=new ArrayList<Integer>();
			historyMax=Double.MIN_VALUE;
			historyMin=Double.MAX_VALUE;
			volumeMax=Integer.MIN_VALUE;
			volumeMin=Integer.MAX_VALUE;
			for(int i=0; i<s.history.size();i++){
				JSONObject obj=s.history.get(i);
				try {
					Double val=obj.getDouble("open");
					//Log.d("data",""+obj.get("date"));
					history.add(val);
					if(val>historyMax){
						historyMax=val;
					}
					if(val<historyMin){
						historyMin=val;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Integer val=obj.getInt("volume");
					volume.add(val);
					if(val>volumeMax){
						volumeMax=val;
					}
					if(val<volumeMin){
						volumeMin=val;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		if(s!=null && s.value<0){
			history.add(s.value);
			volume.add((int)s.volume);
		}
		c.drawColor(Color.WHITE);
		 path=new Path();
		 if(s!=null && s.history!=null && history!=null && history.size()>=1){
			 double ratio=(double)(b.getHeight()/2)/(double)(historyMax-historyMin);
	        path.moveTo(0, (float) ((b.getHeight()/2)-(history.get(0)-historyMin)*ratio));
	     }
	     if(v!=null){
	    	 v.cancel();
	     }  
		v=new ValueAnimator();
        v.setFloatValues(0,3000);
        v.setDuration(2000);
        v.setInterpolator(new LinearInterpolator());
        v.addUpdateListener(new AnimatorUpdateListener() {
			int lastDraw=-1;
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				
				//Log.d("Anim",""+(int)Math.round(100.0*((Float)animation.getAnimatedValue()/100.0)));
				Paint p=new Paint();
	            p.setColor(Color.BLACK);
	            if(volume!=null){
	            	int step=b.getWidth()/volume.size();
	            	float step2=(float)3000.0/volume.size();
					for(int i=1; i<volume.size();i++){
						//if((Float)animation.getAnimatedValue()<i*100.0){
							int val=(int)Math.round((volume.get(i)-volumeMin)*(100.0/(volumeMax-volumeMin))*Math.pow(Math.sin(((Float)animation.getAnimatedValue()-step2*(i-1))/(step2)%1),1.0/6.0));
							if(val>200){
								val=200;
							}else if(val<0){
								val=0;
							}
							Rect rect=new Rect((i-1)*step, 200+val, (i-1)*step+step-2, 200);
							 c.drawRect(rect, p);
						//}
						
					}
				}
				p.setStrokeWidth(6);
				p.setStyle(Paint.Style.STROKE);
				if(history!=null && history.size()>0){
					float step=b.getWidth()/history.size();
					float step2=((float)3000.0)/(float)history.size();
					float pos=((Float)animation.getAnimatedValue()/step2);
					//Log.d("Values Poneis","Size: "+history.size()+" Step: "+animation.getAnimatedValue()+" x: "+(pos*step));
					double ratio=(double)(b.getHeight()/2)/(double)(historyMax-historyMin);
					
					if(pos<history.size()){
						int posDown=(int) Math.floor(pos);
						int posUP=(int) Math.ceil(pos);
						if(posUP>=history.size()){
							
						}else{
							float historyUP=history.get(posUP).floatValue();
							float historyDown=history.get(posDown).floatValue();
							float diff=historyUP-historyDown;
							float val=pos-posDown;
							float valY=(float) ((b.getHeight()/2)-((historyDown+diff*val)-historyMin)*ratio);
							path.lineTo(pos*step, valY);
							lastDraw=(int) Math.floor(pos);
						}
						/*float valY=(float) ((b.getHeight()/2)-(history.get((int) Math.floor(pos)).floatValue()-historyMin)*ratio);
						path.lineTo(pos*step, valY);
						lastDraw=(int) Math.floor(pos);*/
					}
					
					//Log.d("Value Pos", "POS: "+pos);
				}
				//Log.d("value", ""+(int)((Float)animation.getAnimatedValue()/1.0));
				//path.lineTo( (int)((Float)animation.getAnimatedValue()/10.0),10);
				c.drawPath(path, p);
				
	           
	            r.invalidate();
				
			}
		});
       
        v.start();
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_view, container, false);
        if(getArguments() != null){
	        String stockQuote=getArguments().getString("stock");
	        s=StockStorage.INSTANCE.getStock(stockQuote);
	        init();
	        s.RequestUpdate();
	        s.RequestUpdateHistory();
        }
        r=(ImageView) rootView.findViewById(R.id.detailsGraph);
        sr=(HorizontalScrollView) rootView.findViewById(R.id.detailsGraphScroll);
        b=Bitmap.createBitmap(30*22, 320, Bitmap.Config.ARGB_8888);
        c=new Canvas(b);
        /*Rect rect=new Rect(0, 50, 9, 75);
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        c.drawRect(rect, p);*/
        updateGraphAnim();
        r.setImageBitmap(b);
        r.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getPointerCount()>0){
					PointerCoords pointerCoords=new PointerCoords();
					event.getPointerCoords(0, pointerCoords);
					Log.d("Pointer",""+pointerCoords.x+" , "+pointerCoords.y);
				}
				return false;
			}
		});
        sr.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				// TODO Auto-generated method stub
				sr.scrollTo(100000, 0);
			}
		});
        
        value=(TextView) rootView.findViewById(R.id.detailStockValue);
    	name=(TextView) rootView.findViewById(R.id.details_fullname);
    	quote=(TextView) rootView.findViewById(R.id.details_ticket);
    	percentage=(TextView) rootView.findViewById(R.id.details_diffPercentage);
    	diff=(TextView) rootView.findViewById(R.id.details_diffPrice);
        changeState=(ImageView) rootView.findViewById(R.id.detailChangeState);
        wallet=(TextView) rootView.findViewById(R.id.details_stock_wallet);
        if(s!=null){
        	wallet.setText(Integer.toString(StockStorage.INSTANCE.getQuantity(s.quote)));
        }
        ((Button) rootView.findViewById(R.id.details_wallet_less)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int actual=StockStorage.INSTANCE.getQuantity(s.quote);
				if(actual>0){
					StockStorage.INSTANCE.setQuantity(s.quote,actual-1);
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							wallet.setText(Integer.toString(StockStorage.INSTANCE.getQuantity(s.quote)));
							
						}
					});
				}
				
			}
		});
        ((Button) rootView.findViewById(R.id.details_wallet_more)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int actual=StockStorage.INSTANCE.getQuantity(s.quote);
				
					StockStorage.INSTANCE.setQuantity(s.quote,actual+1);
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							wallet.setText(Integer.toString(StockStorage.INSTANCE.getQuantity(s.quote)));
							
						}
					});
				
				
			}
		});
        ((LinearLayout) rootView.findViewById(R.id.details_walletLoc)).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				StockNumberPicker sp=new StockNumberPicker();
				Bundle bund=new Bundle();
				bund.putInt("start", StockStorage.INSTANCE.getQuantity(s.quote));
				sp.setArguments(bund);
				sp.setTargetFragment(StockDetails.this, INT_CODE);
				sp.show(getFragmentManager(), "stockCoisas");
				
				return false;
			}
		});
        
        return rootView;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int newVal=0;
		if (requestCode == INT_CODE){ //make sure fragment codes match up {
	        newVal = data.getIntExtra("newVal", -1);
	        StockStorage.INSTANCE.setQuantity(s.quote,newVal);
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					wallet.setText(Integer.toString(StockStorage.INSTANCE.getQuantity(s.quote)));
					
				}
			});
	        
		}
	}
	static public int INT_CODE=12;


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
}