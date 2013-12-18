package pt.fe.up.cmov.stockmarket;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class StockDetails extends android.app.Fragment{
	ImageView r=null;
	Bitmap b=null;
	Canvas c=null;
	ValueAnimator v=null;
	HorizontalScrollView sr=null;
	Path path=null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_view, container, false);
              
        r=(ImageView) rootView.findViewById(R.id.detailsGraph);
        sr=(HorizontalScrollView) rootView.findViewById(R.id.detailsGraphScroll);
        b=Bitmap.createBitmap(30*22, 320, Bitmap.Config.ARGB_8888);
        c=new Canvas(b);
        /*Rect rect=new Rect(0, 50, 9, 75);
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        c.drawRect(rect, p);*/
        path=new Path();
        path.moveTo(0, 10);
        r.setImageBitmap(b); 
        v=new ValueAnimator();
        v.setFloatValues(0,3000);
        v.setDuration(5000);
        v.setInterpolator(new Interpolator() {
			
			@Override
			public float getInterpolation(float input) {
				// TODO Auto-generated method stub
				//Log.d("lodo", ""+Math.pow(input,1.0/3.0));
				return (float)(Math.pow(input,1.0/3.0));
			}
		});
        v.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				
				//Log.d("Anim",""+(int)Math.round(100.0*((Float)animation.getAnimatedValue()/100.0)));
				Paint p=new Paint();
	            p.setColor(Color.BLACK);
				for(int i=1; i<=30;i++){
					//if((Float)animation.getAnimatedValue()<i*100.0){
						int val=(int)Math.round(100.0*Math.pow(Math.sin(((Float)animation.getAnimatedValue()-100.0*(i-1))/(100.0)%1),1.0/6.0));
						if(val>200){
							val=200;
						}else if(val<0){
							val=0;
						}
						Rect rect=new Rect((i-1)*22, 200+val, (i-1)*22+20, 200);
						 c.drawRect(rect, p);
					//}
					
				}
				p.setStrokeWidth(6);
				p.setStyle(Paint.Style.STROKE);
				//Log.d("value", ""+(int)((Float)animation.getAnimatedValue()/1.0));
				path.lineTo( (int)((Float)animation.getAnimatedValue()/10.0),10);
				c.drawPath(path, p);
				
	           
	            r.invalidate();
				
			}
		});
       
        v.start();
        sr.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				// TODO Auto-generated method stub
				sr.scrollTo(100000, 0);
			}
		});
        return rootView;
	}
}