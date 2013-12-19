package pt.fe.up.cmov.stockmarket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter.BigDecimalLayoutForm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class Stock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6541192250641531209L;
	public final static String historyUpdateAction = "pt.fe.up.cmov.history";
	public final static String instantUpdateAction = "pt.fe.up.cmov.data";
	public final static String detailsAction = "pt.fe.up.cmov.details";
	String quote = null;
	String name = null;
	ArrayList<JSONObject> history = null;
	double value = -1, change = -1, percentage = -1;
	Integer volume = -1;
	long lastHistoryUpdate = -1;
	long lastUpdate = -1;
	String lastTransaction = null;
	String market=null;

	public void RequestUpdateHistory() {
		new Thread(new Runnable() {

			@Override
			public void run() { 
				RestClient rc = new RestClient(YahooSupport.historyBaseURL,
						YahooSupport.INSTANCE
								.getParams30days(new Date(), quote));
				rc.connect();
				//Log.d("data history for " + quote, rc.result);
				history = YahooSupport.INSTANCE.parseHistory(rc.result);
				Collections.sort(history,new Comparator<JSONObject>() {

					@Override
					public int compare(JSONObject lhs, JSONObject rhs) {
						// TODO Auto-generated method stub
						try {
							if(((Date)lhs.get("date")).getTime()>((Date)rhs.get("date")).getTime())
								return 1;
							else if(((Date)lhs.get("date")).getTime()<((Date)rhs.get("date")).getTime())
								return -1;
							return 0;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return 0;
					}
				});
			
				lastHistoryUpdate = System.currentTimeMillis();
				Intent intent = new Intent(historyUpdateAction);
				intent.putExtra("source", quote);
				StockStorage.INSTANCE.getApplicationContext().sendBroadcast(
						intent);
			}
		}).start();
	}

	// "AAPL",565.55,"-0.88","N/A - -0.16%","12/10/2013","4:00pm",9939483
	public void RequestUpdate() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				RestClient rc = new RestClient(YahooSupport.instantBaseURL,
						YahooSupport.INSTANCE.queryInstant(
								YahooSupport.basicQuery, quote));
				rc.connect();
				Intent intent = new Intent(instantUpdateAction);
				intent.putExtra("source", quote);
				if (rc.status == 200) {
					Log.d("data for " + quote, rc.result);
					String[] vals = rc.result.split(",");
					for(int i=0;i<vals.length;i++){
						vals[i]=vals[i].replace("\"", "");
					}
					if (vals.length >= 7) {
						if (quote.equals(vals[0])) {
							try {
								value = Double.parseDouble(vals[1]);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							try {
								change = Double.parseDouble(vals[2].replace("+",""));
								Log.d("Change "+quote,""+change);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							String[] percentageSpliter = vals[3].split(" - ");
							if (percentageSpliter.length == 2) {
								try {
									percentage = Double
											.parseDouble(percentageSpliter[1]
													.replaceAll("%", "").replace("+",""));
									Log.d("Percentage "+quote,""+percentage);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}

							} else {
								percentage = 0;
							}
							lastTransaction = vals[4] + " " + vals[5];
							try {
								Log.d("Volume",vals[6]);
								volume = Integer.parseInt(vals[6].replace("\"", "").trim());
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
					lastUpdate = System.currentTimeMillis();
					intent.putExtra("ok", true);
				}
				
				
				StockStorage.INSTANCE.getApplicationContext().sendBroadcast(
						intent);

			}
		}).start();
	}
	
	public void requestCheck() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(detailsAction);
				try{
					RestClient rc = new RestClient(YahooSupport.instantBaseURL,
							YahooSupport.INSTANCE
									.queryInstant(YahooSupport.existsQuery, quote));
					rc.connect();
					Log.d("data check for " + quote, rc.result);
					
					String[] results=rc.result.split(",");
					if(results.length>=3){
						if(results[1].equals("\"N/A\"") && results[2].equals("0.00")){
							
						}else{
							name=results[0];
							market=results[1];
							value=Double.parseDouble(results[2]);
							intent.putExtra("ok", true);
						}
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
				
					intent.putExtra("source", quote);
					StockStorage.INSTANCE.getApplicationContext().sendBroadcast(
							intent);
				}
			}
		}).start();
	}

	Stock(String name) {
		quote = name;
		requestCheck();
	}

	public void loadHistory() {

	}
}
