package cn.wang.widget.roundrectlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * 圆角帮助类 - 创建四个Path
 *
 * @author wangheng
 */
@Deprecated
public class RoundRectHelper2Deprecated {

    private ViewGroup mTargetView;
    private Paint mLayerPaint;
    private int mRadius = 0;

    private Paint mMaskPaint;
    private RectF mMaskRect = null;
    private PorterDuffXfermode mMode;

    private int mBackgroundColor = Color.WHITE;


    public RoundRectHelper2Deprecated(Context context, AttributeSet attrs, ViewGroup targetView) {

        if(context == null){
            throw new IllegalArgumentException("context cannot equals null");
        }

        if(targetView == null){
            throw new IllegalArgumentException("targetView cannot equals null");
        }


        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRectLayout);

            mRadius = ta.getDimensionPixelOffset(R.styleable.RoundRectLayout_roundRectRadius,0);
            mBackgroundColor = ta.getColor(R.styleable.RoundRectLayout_roundRectBackground,Color.WHITE);

            ta.recycle();
        }

        mTargetView = targetView;

//        mTargetView.setWillNotDraw(false);
        mTargetView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setStyle(Paint.Style.FILL);
        mMaskPaint.setColor(Color.WHITE);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

        mLayerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLayerPaint.setXfermode(null);
    }

    private int mOldTargetWidth;
    private int mOldTargetHeight;

    /**
     * 得到蒙版RectF
     * @return 蒙版Rect
     */
    private RectF getMaskRect(){

        if(mMaskRect == null || isTargetViewSizeChanged()){
            mMaskRect = new RectF(0, 0, mTargetView.getWidth(), mTargetView.getHeight());
        }
        return mMaskRect;
    }

    /**
     * TargetView的大小是否发生了改变
     * @return TargetView的大小是否发生了改变
     */
    private boolean isTargetViewSizeChanged(){
        return mTargetView.getWidth() != mOldTargetWidth || mTargetView.getHeight() != mOldTargetHeight;
    }

    public void startRoundRect(Canvas canvas){
        canvas.saveLayer(getMaskRect(),mLayerPaint,Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(mBackgroundColor);
    }


    public void completedRoundRect(Canvas canvas){

        mMaskPaint.setXfermode(mMode);
//        canvas.drawBitmap(getMaskBitmap(),0,0, mMaskPaint);
        drawTopLeft(canvas);
        drawTopRight(canvas);
        drawBottomLeft(canvas);
        drawBottomRight(canvas);
        mMaskPaint.setXfermode(null);

        canvas.restore();

        mOldTargetWidth = mTargetView.getWidth();
        mOldTargetHeight = mTargetView.getHeight();
    }

    private Path mTopLeftPath ;
    private Path mTopRightPath ;
    private Path mBottomLeftPath ;
    private Path mBottomRightPath ;

    private void drawTopLeft(Canvas canvas) {
        if(mTopLeftPath == null){
            if (mRadius > 0) {
                mTopLeftPath = new Path();
                mTopLeftPath.moveTo(0, mRadius);
                mTopLeftPath.lineTo(0, 0);
                mTopLeftPath.lineTo(mRadius, 0);
                mTopLeftPath.arcTo(new RectF(0, 0, mRadius * 2, mRadius * 2),
                        -90, -90);
                mTopLeftPath.close();
            }
        }
        if(mTopLeftPath != null){
            canvas.drawPath(mTopLeftPath, mMaskPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if(mTopRightPath == null) {
            if (mRadius > 0) {
                int width = mTargetView.getWidth();
                mTopRightPath = new Path();
                mTopRightPath.moveTo(width - mRadius, 0);
                mTopRightPath.lineTo(width, 0);
                mTopRightPath.lineTo(width, mRadius);
                mTopRightPath.arcTo(new RectF(width - 2 * mRadius, 0, width,
                        mRadius * 2), 0, -90);
                mTopRightPath.close();
            }
        }
        if(mTopRightPath != null){
            canvas.drawPath(mTopRightPath, mMaskPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if(mBottomLeftPath == null) {
            if (mRadius > 0) {
                int height = mTargetView.getHeight();
                mBottomLeftPath = new Path();
                mBottomLeftPath.moveTo(0, height - mRadius);
                mBottomLeftPath.lineTo(0, height);
                mBottomLeftPath.lineTo(mRadius, height);
                mBottomLeftPath.arcTo(new RectF(0, height - 2 * mRadius,
                        mRadius * 2, height), 90, 90);
                mBottomLeftPath.close();
            }
        }
        if(mBottomLeftPath != null){
            canvas.drawPath(mBottomLeftPath, mMaskPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if(mBottomRightPath == null) {
            if (mRadius > 0) {
                int height = mTargetView.getHeight();
                int width = mTargetView.getWidth();
                mBottomRightPath = new Path();
                mBottomRightPath.moveTo(width - mRadius, height);
                mBottomRightPath.lineTo(width, height);
                mBottomRightPath.lineTo(width, height - mRadius);
                mBottomRightPath.arcTo(new RectF(width - 2 * mRadius, height - 2
                        * mRadius, width, height), 0, 90);
                mBottomRightPath.close();
            }
        }
        if(mBottomRightPath != null){
            canvas.drawPath(mBottomRightPath, mMaskPaint);
        }
    }
}
