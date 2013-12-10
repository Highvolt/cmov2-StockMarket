package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;
import java.util.Formatter.BigDecimalLayoutForm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Stock {
	public static final int width=566;
	public static final int height=67;
	Bitmap graph=null;
	String quote=null;
	
	
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
}