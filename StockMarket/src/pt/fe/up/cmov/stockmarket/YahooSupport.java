package pt.fe.up.cmov.stockmarket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public enum YahooSupport {
	INSTANCE;
	public Context context=null;
	private YahooSupport() {
		// TODO Auto-generated constructor stub
	}
	static final String historyBaseURL="http://ichart.finance.yahoo.com/table.txt";
	static final String instantBaseURL="http://finance.yahoo.com/d/quotes";
	static final String completeQuery="snxc4l1c6k2d1t1v";
	static final String basicQuery="sl1c6k2d1t1v";
	static final String existsQuery="nxl1";
	//http://ichart.finance.yahoo.com/table.txt?a=9&b=5&c=2013&d=9&e=19&f=2013&g=d&s=DELL
	
	
	public List<NameValuePair> queryInstant(String query,String tick){
		List<NameValuePair> nv=new LinkedList<NameValuePair>();
		nv.add(new BasicNameValuePair("f", query));
		nv.add(new BasicNameValuePair("s", tick));
		return nv;
		
	}

	public List<NameValuePair> getParams30days(Date data,String tick){
		List<NameValuePair> nv=new LinkedList<NameValuePair>();
		Calendar cal=Calendar.getInstance();
		cal.setTime(data);
		nv.add(new BasicNameValuePair("d", Integer.toString(cal.get(Calendar.MONTH))));
		nv.add(new BasicNameValuePair("e", Integer.toString(cal.get(Calendar.DAY_OF_MONTH))));
		nv.add(new BasicNameValuePair("f", Integer.toString(cal.get(Calendar.YEAR))));
		cal.add(Calendar.DAY_OF_MONTH, -30);
		nv.add(new BasicNameValuePair("a", Integer.toString(cal.get(Calendar.MONTH))));
		nv.add(new BasicNameValuePair("b", Integer.toString(cal.get(Calendar.DAY_OF_MONTH))));
		nv.add(new BasicNameValuePair("c", Integer.toString(cal.get(Calendar.YEAR))));
		nv.add(new BasicNameValuePair("g", "d"));
		nv.add(new BasicNameValuePair("s", tick));
		return nv;
		
	}
	
	public ArrayList<JSONObject> parseHistory(String values){
		ArrayList<JSONObject> data=new ArrayList<JSONObject>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String[] lines=values.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String[] pars=lines[i].split(",");
			if(pars.length>=7){
				JSONObject obj=new JSONObject();
				try {
					obj.put("date", formatter.parse(pars[0]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("open", Double.parseDouble(pars[1]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("high", Double.parseDouble(pars[2]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("low", Double.parseDouble(pars[3]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("close", Double.parseDouble(pars[4]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("volume", Double.parseDouble(pars[5]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					obj.put("adj_close", Double.parseDouble(pars[6]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				data.add(obj);
			}
			
		}
		return data;
	}
}
