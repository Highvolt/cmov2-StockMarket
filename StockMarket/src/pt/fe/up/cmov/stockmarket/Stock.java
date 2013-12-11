package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter.BigDecimalLayoutForm;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Stock {
	public static final int width=566;
	public static final int height=67;
	Bitmap graph=null;
	String quote=null;
	String name=null;
	ArrayList<JSONObject> history=null;
	double value=-1, change=-1,percentage=-1,volume=-1;
	long lastHistoryUpdate=-1;
	long lastUpdate=-1;
	
	
	private void RequestUpdateHistory(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				RestClient rc=new RestClient(YahooSupport.INSTANCE.historyBaseURL, YahooSupport.INSTANCE.getParams30days(new Date(), quote));
				rc.connect();
				Log.d("data history for "+quote,rc.result);
				history=YahooSupport.INSTANCE.parseHistory(rc.result);
				lastUpdate=System.currentTimeMillis();
				
			}
		}).start();
	}
	
	private void RequestUpdate(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				RestClient rc=new RestClient(YahooSupport.INSTANCE.instantBaseURL, YahooSupport.INSTANCE.queryInstant(YahooSupport.basicQuery, quote));
				rc.connect();
				Log.d("data for "+quote,rc.result);
				
				
			}
		}).start();
	}
	
	
	Stock(String name){
		quote=name;
	}
	
	public Bitmap loadGraph(Context ct){
		
		if(graph==null){
			graph=BitmapFactory.decodeFile(ct.getFilesDir().getAbsolutePath()+quote+".png");
			if(graph==null){
				//NEEDS to be created
				
				graph=Bitmap.createBitmap(Stock.width, Stock.height, Bitmap.Config.ARGB_8888);
				Canvas c=new Canvas(graph);
				
			}
		}
		return graph;
		
	}
	
	public void loadHistory(){
		
	}
}
