package pt.fe.up.cmov.stockmarket;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

class PathDrawable extends Drawable implements AnimatorUpdateListener  {
    private Path mPath;
    private Paint mPaint;
    private ValueAnimator mAnimator;

    public PathDrawable() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Style.STROKE);
        
        
    }
    
    

    public void startAnimating() {
        Rect b = getBounds();
        mAnimator = ValueAnimator.ofInt(-b.bottom, b.bottom);
        mAnimator.setDuration(1000);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void draw(Canvas canvas) {
    	
    	Log.d("aasd",""+canvas.getHeight());
    	Log.d("aasd WIDTH",""+canvas.getWidth());
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animator) {
        mPath.reset();
        Rect b = getBounds();
        mPath.moveTo(b.left, b.bottom);
        mPath.quadTo((b.right-b.left)/2, (Integer) animator.getAnimatedValue(), b.right, b.bottom);
        invalidateSelf();
    }
}