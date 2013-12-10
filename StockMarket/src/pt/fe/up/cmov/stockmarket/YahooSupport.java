package pt.fe.up.cmov.stockmarket;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public enum YahooSupport {
	INSTANCE;
	private YahooSupport() {
		// TODO Auto-generated constructor stub
	}
	static final String historyBaseURL="http://ichart.finance.yahoo.com/table.txt";
	
	//http://ichart.finance.yahoo.com/table.txt?a=9&b=5&c=2013&d=9&e=19&f=2013&g=d&s=DELL

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
}
