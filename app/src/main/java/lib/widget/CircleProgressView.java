package lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressBar";

    private int mMaxProgress = 100;

    private int mProgress = 30;

    private final int mCircleLineStrokeWidth = 8;
    private final int mCircleLineStrokeOutWidth = 26;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;

    private String mTxtHint1;

    private String mTxtHint2;

    private int progressColor = 0xffffffff;
    private String showText = "";

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    public void setProgressColor(int progressColor){
        this.progressColor = progressColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        float dw = (mCircleLineStrokeOutWidth - mCircleLineStrokeWidth) / 2;
        mRectF.left = mCircleLineStrokeWidth / 2 + dw; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2 + dw; // 左上角y
        mRectF.right = width - mCircleLineStrokeWidth / 2 - dw; // 左下角x
        mRectF.bottom = height - mCircleLineStrokeWidth / 2 - dw; // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);

        // 绘制圆圈，进度条 填充
        mRectF.left = mCircleLineStrokeOutWidth / 2; // 左上角x
        mRectF.top = mCircleLineStrokeOutWidth / 2; // 左上角y
        mRectF.right = width - mCircleLineStrokeOutWidth / 2; // 左下角x
        mRectF.bottom = height - mCircleLineStrokeOutWidth / 2; // 右下角y
        mPaint.setStrokeWidth(mCircleLineStrokeOutWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置线条尾部有圆角效果
        mPaint.setColor(progressColor);
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        String text = TextUtils.isEmpty(showText)?mProgress+"min":showText;
        int textHeight = height / 4-2;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length())-5;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

        if (!TextUtils.isEmpty(mTxtHint1)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint1;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            mPaint.setColor(Color.rgb(0x99, 0x99, 0x99));
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
        }

        if (!TextUtils.isEmpty(mTxtHint2)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint2;
            textHeight = height / 8;
            mPaint.setTextSize(textHeight);
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgress(int progress, String showText) {
        this.mProgress = progress;
        this.showText = showText;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }
}