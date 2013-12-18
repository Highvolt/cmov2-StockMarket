package pt.fe.up.cmov.stockmarket;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public enum StockStorage {
	INSTANCE;
	private Context appC=null;
	private Map<String,Stock> stocks;
	private StockStorage(){
		stocks=new HashMap<String, Stock>();
	}
	
	void setApplicationContext(Context ct){
		appC=ct;
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
			
		}
		return tmp;
	} 
	
	
	public boolean addStock(Stock stock){
		if(stocks.containsKey(stock.quote)){
			return false;
		}else{
			stocks.put(stock.quote, stock);
			return true;
		}
	}
	
	Context getApplicationContext(){
		return appC;
	}
}
