package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



import android.R.integer;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.MotionEvent.PointerCoords;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;

@SuppressLint("ValidFragment")
public class MainActivity extends Activity {

    private String mTitle;
	private String mDrawerTitle;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = (String) getTitle();
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        android.app.Fragment fragment = new StockDetails();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    static class StockDetails extends android.app.Fragment{
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
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					RestClient rc=new RestClient(YahooSupport.INSTANCE.historyBaseURL, YahooSupport.INSTANCE.getParams30days(new Date(), "AAPL"));
					rc.connect();
					Log.d("data from yahoo",rc.result);
					
				}
			}).start();
            
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
					Log.d("lodo", ""+Math.pow(input,1.0/3.0));
					return (float)(Math.pow(input,1.0/3.0));
				}
			});
            v.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					
					Log.d("Anim",""+(int)Math.round(100.0*((Float)animation.getAnimatedValue()/100.0)));
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
					Log.d("value", ""+(int)((Float)animation.getAnimatedValue()/1.0));
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
    
    /*static class StockView extends android.app.Fragment{
    	public static final String ARG_PLANET_NUMBER = "planet_number";
    	public static Integer multiplier=1;
    	TextView a;
    	VelocityTracker vt;
    	float x;
        public StockView() {
            // Empty constructor required for fragment subclasses
        }

        void updateMultiplier(){
        	if(multiplier<=0){
        		multiplier=1;
        	}
        	a.setText("x"+multiplier);
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.stocklist, container, false);
            
            ArrayList<Stock> st=new ArrayList<Stock>();
            st.add(new Stock("MSFT"));
            st.add(new Stock("MSFT"));
            st.add(new Stock("MSFT"));
            st.add(new Stock("MSFT"));
            StockAdapter adp=new StockAdapter(getActivity(), R.layout.stockitem, st);
            ((ListView) rootView.findViewById(R.id.stock_list)).setAdapter(adp);
            a=((TextView) rootView.findViewById(R.id.stock_multiplier));
            a.setText("x"+multiplier);
            a.setOnTouchListener(new OnTouchListener() {
    			
    			@Override
    			public boolean onTouch(final View v, MotionEvent event) {
    				PointerCoords pc=new PointerCoords();
    				switch (event.getAction()) {
    				case MotionEvent.ACTION_UP:
    				case MotionEvent.ACTION_CANCEL:
    					if(vt==null){
    						return false;
    					}
    					Log.d("finger","up");
    					vt.computeCurrentVelocity(10);
    					if((vt.getXVelocity()>20 || vt.getXVelocity()<-20) || v.getTranslationX()>=v.getWidth()/2 || v.getTranslationX()<=-v.getWidth()/2){
    						Log.d("yes","yes " +vt.getXVelocity());
    						if(vt.getXVelocity()>0){
    						v.animate().translationX(v.getWidth()).setListener(new AnimatorListener() {
    							
    							@Override
    							public void onAnimationStart(Animator animation) {
    								// TODO Auto-generated method stub
    								
    							}
    							
    							@Override
    							public void onAnimationRepeat(Animator animation) {
    								// TODO Auto-generated method stub
    								Log.d("repeat", "repeat");
    							}
    							
    							@Override
    							public void onAnimationEnd(Animator animation) {
    								// TODO Auto-generated method stub
    								multiplier/=10;
    								updateMultiplier();
    								v.setTranslationX(-v.getWidth());
    								v.animate().translationX(0).setListener(new AnimatorListener() {
    									
    									@Override
    									public void onAnimationStart(Animator animation) {
    										// TODO Auto-generated method stub
    										
    									}
    									
    									@Override
    									public void onAnimationRepeat(Animator animation) {
    										// TODO Auto-generated method stub
    										Log.d("repeat", "repeat");
    									}
    									
    									@Override
    									public void onAnimationEnd(Animator animation) {
    										// TODO Auto-generated method stub
    										
    										
    									}
    									
    									@Override
    									public void onAnimationCancel(Animator animation) {
    										// TODO Auto-generated method stub
    										
    									}
    								});
    							}
    							
    							@Override
    							public void onAnimationCancel(Animator animation) {
    								// TODO Auto-generated method stub
    								
    							}
    						});
    						}else{
    							v.animate().translationX(-v.getWidth()).setListener(new AnimatorListener() {
    								
    								@Override
    								public void onAnimationStart(Animator animation) {
    									// TODO Auto-generated method stub
    									
    								}
    								
    								@Override
    								public void onAnimationRepeat(Animator animation) {
    									// TODO Auto-generated method stub
    									Log.d("repeat", "repeat");
    								}
    								
    								@Override
    								public void onAnimationEnd(Animator animation) {
    									// TODO Auto-generated method stub
    									multiplier*=10;
        								updateMultiplier();
    									v.setTranslationX(v.getWidth());
    									v.animate().translationX(0).setListener(new AnimatorListener() {
    										
    										@Override
    										public void onAnimationStart(Animator animation) {
    											// TODO Auto-generated method stub
    											
    										}
    										
    										@Override
    										public void onAnimationRepeat(Animator animation) {
    											// TODO Auto-generated method stub
    											Log.d("repeat", "repeat");
    										}
    										
    										@Override
    										public void onAnimationEnd(Animator animation) {
    											// TODO Auto-generated method stub
    											
    											
    										}
    										
    										@Override
    										public void onAnimationCancel(Animator animation) {
    											// TODO Auto-generated method stub
    											
    										}
    									});
    								}
    								
    								@Override
    								public void onAnimationCancel(Animator animation) {
    									// TODO Auto-generated method stub
    									
    								}
    							});
    						}
    					}else{
    						Log.d("no","no "+vt.getXVelocity());
    						v.animate().translationX(0).setListener(new AnimatorListener() {
    							
    							@Override
    							public void onAnimationStart(Animator animation) {
    								// TODO Auto-generated method stub
    								
    							}
    							
    							@Override
    							public void onAnimationRepeat(Animator animation) {
    								// TODO Auto-generated method stub
    								Log.d("repeat", "repeat");
    							}
    							
    							@Override
    							public void onAnimationEnd(Animator animation) {
    								// TODO Auto-generated method stub
    								
    								
    							}
    							
    							@Override
    							public void onAnimationCancel(Animator animation) {
    								// TODO Auto-generated method stub
    								
    							}
    						});
    					}
    					vt.recycle();
    					vt=null;
    					break;
    				case MotionEvent.ACTION_DOWN:
    					event.getPointerCoords(0, pc);
    					x=pc.getAxisValue(MotionEvent.AXIS_X);
    					Log.d("finger","down");
    					vt=VelocityTracker.obtain();
    					vt.addMovement(event);
    					break;
    				case MotionEvent.ACTION_MOVE:
    					if(vt==null){
    						return false;
    					}
    					vt.addMovement(event);
    					Log.d("finger","move");
    					event.getPointerCoords(0, pc);
    					v.setTranslationX(pc.x-x);
    					Log.d("finger",""+(pc.x-x));
    					break;

    				default:
    					break;
    				}
    				
    				return true;
    			}
    			
    			
    		});
            return rootView;
        }
    }*/
    
}