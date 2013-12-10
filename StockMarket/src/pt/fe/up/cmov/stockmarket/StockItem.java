package pt.fe.up.cmov.stockmarket;

import org.w3c.dom.Text;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.MotionEvent.PointerCoords;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StockItem extends LinearLayout {
	float x;
	float y;
	VelocityTracker vt;
	private TextView wallet;
	Context context;
	int wallet_count=1;
	public void updateWallet(){
		if(wallet_count>=10000){
			if((wallet_count/1000)*1000<wallet_count){
				wallet.setText(""+wallet_count/1000+"k+");
			}else{
				wallet.setText(""+wallet_count/1000+"k");
			}
			
		}else{
			wallet.setText(""+wallet_count);
		}
	}
	
	public StockItem(Context context){
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v=layoutInflater.inflate(R.layout.stockitem, this);
		wallet=((TextView)v.findViewById(R.id.stock_wallet));
		updateWallet();
		this.setOnTouchListener(new OnTouchListener() {
			
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
								//wallet_count-=MainActivity.StockView.multiplier;
								updateWallet();
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
									//wallet_count+=MainActivity.StockView.multiplier;
									updateWallet();
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
		
	}
	
	
	
	public StockItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v=layoutInflater.inflate(R.layout.stockitem, this);
		

		// TODO Auto-generated constructor stub
	}

}
