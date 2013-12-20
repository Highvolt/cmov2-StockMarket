package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;

import org.json.JSONException;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.sax.RootElement;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.MotionEvent.PointerCoords;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StockItem extends LinearLayout {
	final static private int image_width=566;
	final static private int image_height=67;
	float x;
	float y;
	VelocityTracker vt;
	private TextView wallet;
	Context context;
	int wallet_count=1;
	private View v;
	public Stock s;
	BroadcastReceiver b;
	Bitmap image=null;
	Canvas c=null;
	public void updateWallet(){
		if(s!=null){
			wallet_count=StockStorage.INSTANCE.getQuantity(s.quote);
		}
		if(wallet_count>=10000){
			if((wallet_count/1000)*1000<wallet_count){
				wallet.setText(""+wallet_count/1000+"k+");
			}else{
				wallet.setText(""+wallet_count/1000+"k");
			}
			
		}else{
			wallet.setText(""+wallet_count);
		}
	}
	
	protected void updateBitmap() {
		if(s==null || s.history==null)
			return;
		c.drawColor(Color.WHITE);
		int size=s.history.size();
		if(size<=1){
			return;
		}
		double step=(double)image_width/((double)size+1);
		double min=Double.MAX_VALUE;
		double max=Double.MIN_VALUE;
		ArrayList<Double> values=new ArrayList<Double>();
		for(int i=0; i<size;i++){
			try {
				Double v_tmp=s.history.get(i).getDouble("close");
				if(v_tmp>max){
					max=v_tmp;
				}
				if(v_tmp<min){
					min=v_tmp;
				}
				values.add(v_tmp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Path path=new Path();
		double ratio=(double)image_height/(double)(max-min);
		path.moveTo(0, image_height-(float)((values.get(0)-min)*ratio));
		for(int i=0; i<size;i++){
			//Log.d("Graph "+s.quote,""+(float)((values.get(i)-min)*ratio));
			path.lineTo((float)(i*step), image_height-(float)((values.get(i)-min)*ratio));
		}
		if(s.value>=0){
			path.lineTo((float)(size*step), image_height-(float)((s.value-min)*ratio));
		}
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(6);
		paint.setStyle(Paint.Style.STROKE);
		c.drawPath(path, paint);
		paint.setStrokeWidth(8);
		paint.setColor(Color.GRAY);
		c.drawLine(0, image_height-(float)((values.get(size-1)-min)*ratio), image_width, image_height-(float)((values.get(size-1)-min)*ratio), paint);
		((ImageView)this.findViewById(R.id.imageView1)).setImageBitmap(image);
		
		
	}
	

	
	public void setContent(Stock s){
		this.s=s;
		updateData();
		this.invalidate();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		updateWallet();
		
	}
	
	protected void updateData() {
		if(s==null)
			return;
		((TextView)this.findViewById(R.id.menu_quotes)).setText(s.quote);
		((TextView)this.findViewById(R.id.menu_price)).setText("$"+Double.toString(s.value));
		updateWallet();
	}
	
	private void init(){
		image=Bitmap.createBitmap(image_width, image_height, Bitmap.Config.ARGB_8888);
		c=new Canvas(image);
		 b=new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					
					String tmp=intent.getStringExtra("source");
					if(tmp!=null && s!=null && tmp.equalsIgnoreCase(s.quote)){
						Log.d("StockUpdate", "valores"+s.quote+" "+s.value);
						updateData();
						//if(intent.getAction().equals(Stock.historyUpdateAction)){
						updateBitmap();
						//}
						StockItem.this.invalidate();
						updateWallet();
					}
					
				}
			};
			StockStorage.INSTANCE.getApplicationContext().registerReceiver(b, new IntentFilter(Stock.instantUpdateAction));
			StockStorage.INSTANCE.getApplicationContext().registerReceiver(b, new IntentFilter(Stock.historyUpdateAction));
			wallet=(TextView) v.findViewById(R.id.menu_wallet);
			updateWallet();
	}
	
	public StockItem(Context context){
		super(context);
		
		LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 v=layoutInflater.inflate(R.layout.stock_menu_item, this);
		 init();
		
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if(b!=null){
			StockStorage.INSTANCE.getApplicationContext().unregisterReceiver(b);
		}
	}
	
	
	public StockItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v=layoutInflater.inflate(R.layout.stock_menu_item, this);
		init();
		
	}

}
