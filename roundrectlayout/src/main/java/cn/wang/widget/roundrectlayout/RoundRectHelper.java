package cn.wang.widget.roundrectlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 圆角帮助类
 *
 * @author wangheng
 */
public class RoundRectHelper {

    private ViewGroup mTargetView;
    private Paint mLayerPaint;
    private int mRadius = 0;

    private Bitmap mMaskBitmap = null;
    private Paint mMaskPaint;
    private RectF mMaskRect = null;
    private PorterDuffXfermode mMode;

    private Drawable mBackgroundDrawable;
    private Bitmap mBackgroundBitmap;
    private Paint mBackgroundPaint;


    public RoundRectHelper(Context context, AttributeSet attrs, ViewGroup targetView) {

        if(context == null){
            throw new IllegalArgumentException("context cannot equals null");
        }

        if(targetView == null){
            throw new IllegalArgumentException("targetView cannot equals null");
        }


        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRectLayout);

            mRadius = ta.getDimensionPixelOffset(R.styleable.RoundRectLayout_roundRectRadius,0);
            mBackgroundDrawable = ta.getDrawable(R.styleable.RoundRectLayout_roundRectBackground);

            ta.recycle();
        }

        mTargetView = targetView;

        mTargetView.setWillNotDraw(false);
        mTargetView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setStyle(Paint.Style.FILL);
        mMaskPaint.setColor(Color.WHITE);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        mLayerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLayerPaint.setXfermode(null);
    }

    /**
     * 得到蒙版RectF
     * @return 蒙版Rect
     */
    private RectF getMaskRect(){
        if(mMaskRect == null){
            mMaskRect = new RectF(0, 0, mTargetView.getWidth(), mTargetView.getHeight());
        }
        return mMaskRect;
    }

    /**
     * 得到蒙版Bitmap
     * @return 蒙版Bitmap
     */
    private Bitmap getMaskBitmap(){
        if(mMaskBitmap != null && !mMaskBitmap.isRecycled()){
            return mMaskBitmap;
        }

        RectF rectF = getMaskRect();

        mMaskBitmap = Bitmap.createBitmap(mTargetView.getWidth(),
                mTargetView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(mMaskBitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        c.drawRoundRect(rectF,mRadius,mRadius,paint);

        return mMaskBitmap;

    }

    private void drawBackground(Canvas canvas){
        if(mBackgroundDrawable == null){
            mBackgroundBitmap = null;
            return;
        }

        if(mBackgroundPaint == null) {
            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        if(mBackgroundBitmap == null || mBackgroundBitmap.isRecycled()) {
            mBackgroundDrawable.setBounds(0, 0, mTargetView.getWidth(), mTargetView.getHeight());
            mBackgroundBitmap = Bitmap.createBitmap(mTargetView.getWidth(),
                    mTargetView.getHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(mBackgroundBitmap);
            mBackgroundDrawable.draw(c);
        }
        canvas.drawBitmap(mBackgroundBitmap,0,0,mBackgroundPaint);
    }

    public void startRoundRect(Canvas canvas){
        canvas.saveLayer(getMaskRect(),mLayerPaint,Canvas.ALL_SAVE_FLAG);
        drawBackground(canvas);
    }

    public void completedRoundRect(Canvas canvas){

        mMaskPaint.setXfermode(mMode);
        canvas.drawBitmap(getMaskBitmap(),0,0, mMaskPaint);
        mMaskPaint.setXfermode(null);

        canvas.restore();
    }

    public Drawable getBackground() {
        return mBackgroundDrawable;
    }

    public void setBackground(Drawable drawable){
        this.mBackgroundDrawable = drawable;
    }
}
