package pt.fe.up.cmov.stockmarket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public enum StockStorage {
	INSTANCE;
	private Context appC=null;
	private Map<String,Stock> stocks;
	FileInputStream inputWallet=null;
	FileOutputStream outputWallet=null;
	public ArrayList<String> onList=null;
	
	JSONObject wallet=null;
	private StockStorage(){
		stocks=new HashMap<String, Stock>();
		onList=new ArrayList<String>();
		
	}
	
	
	public int getQuantity(String quote){
		if(wallet!=null){
			try {
				return wallet.getInt(quote.toUpperCase());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
			
		}
		return -1;
		
	}
	
	
	public void removeFromWallet(String s){
		if(wallet!=null){
			try {
				outputWallet=appC.openFileOutput("stocks.json", Context.MODE_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				wallet.remove(s.toUpperCase());
				onList.remove(s.toUpperCase());
			try {
				outputWallet.write(wallet.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setQuantity(String quote,int quantity){
		if(wallet!=null){
			try {
				outputWallet=appC.openFileOutput("stocks.json", Context.MODE_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				wallet.put(quote.toUpperCase(), quantity);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				outputWallet.write(wallet.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	void setApplicationContext(Context ct){
		appC=ct;
		try {
			inputWallet=appC.openFileInput("stocks.json");
			if(inputWallet!=null){
				String full="";
				int c=0;
				while((c=inputWallet.read())!=-1){
					full+=(char)c;
				}
				try {
					wallet=new JSONObject(full);
					Iterator a=wallet.keys();
					while(a.hasNext()){
						onList.add((String) a.next());
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wallet=new JSONObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Stock getStock(String quote){
		return stocks.get(quote);
	} 
	
	public Stock getOrAddStock(String quote){
		Stock tmp=stocks.get(quote);
		if(tmp==null){
			tmp=new Stock(quote);
			tmp.RequestUpdate();
			tmp.RequestUpdateHistory();
			this.addStock(tmp);
			if(!onList.contains(quote)){
				onList.add(quote);
			}
			
		}
		return tmp;
	} 
	
	
	public boolean addStock(Stock stock){
		if(stocks.containsKey(stock.quote)){
			return false;
		}else{
			stocks.put(stock.quote, stock);
			if(!onList.contains(stock.quote)){
				onList.add(stock.quote);
			}
			return true;
		}
	}
	
	Context getApplicationContext(){
		return appC;
	}
}
