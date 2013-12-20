package pt.fe.up.cmov.stockmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;



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
import android.content.Intent;
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
import android.view.MenuItem;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;

@SuppressLint("ValidFragment")
public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener{

    private String mTitle;
	private String mDrawerTitle;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StockStorage.INSTANCE.setApplicationContext(getApplicationContext());
        getActionBar().setDisplayShowHomeEnabled(true);
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
            	Log.d("OPEN","OPEN");
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                ((ListView) findViewById(R.id.left_drawer)).invalidateViews();
                mDrawerLayout.invalidate();
            }
        };
        
        ArrayList<String> st=StockStorage.INSTANCE.onList;
        
        StockAdapter adp=new StockAdapter(this, R.layout.stockitem, st);
        ((ListView) findViewById(R.id.left_drawer)).setAdapter(adp);
        ((ListView) findViewById(R.id.left_drawer)).setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		String st=(String)arg0.getItemAtPosition(arg2);
        		Log.d("menu","clicked ->"+st);
        		android.app.Fragment fragment = new StockDetails();
        		Bundle args=new Bundle();
        		args.putString("stock", st);
        		fragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                mDrawerLayout.closeDrawers();
        		
        	}
		});
        ((ListView) findViewById(R.id.left_drawer)).setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				StockStorage.INSTANCE.removeFromWallet(((StockItem)arg1).s.quote);
				runOnUiThread(new Runnable() {
					public void run() {
						StockAdapter adp=new StockAdapter(MainActivity.this, R.layout.stockitem, StockStorage.INSTANCE.onList);
				        ((ListView) findViewById(R.id.left_drawer)).setAdapter(adp);
					}
				});
				
				return false;
			}
        	
        });
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        android.app.Fragment fragment = new Global();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String vals;
		if (requestCode == INT_CODE){ //make sure fragment codes match up {
	        vals = data.getStringExtra("newVal");
	        runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					StockAdapter adp=new StockAdapter(MainActivity.this, R.layout.stockitem, StockStorage.INSTANCE.onList);
			        ((ListView) findViewById(R.id.left_drawer)).setAdapter(adp);
					
				}
			});
	        
	        
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    static public int INT_CODE=13;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:
	            AddStock ad=new AddStock();
	            ad.show(getFragmentManager(), "addStock");
	            return true;
	        case R.id.Global:{
	        	 android.app.Fragment fragment = new Global();
	             FragmentManager fragmentManager = getFragmentManager();
	             fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	        }
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
    
    
    
    
 
    
}
