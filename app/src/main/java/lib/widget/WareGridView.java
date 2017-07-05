package lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mags on 2017/7/5.
 */

public class WareGridView extends View {
    private final Paint paint;
    private final Context context;
    public WareGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setColor(0x80ffffff);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        int topBottomDy = 10*2;//上下起始结束间隔
        int leftRightDx = 10*2;//左右起始结束间隔
        int lineHorizontalNum = 6;//横向条数
        int lineVerticalNum = 17;//竖直条数
        int spaceX = (width - leftRightDx) / (lineVerticalNum - 1);//横向宽度  为 (width - leftRightDx) / (lineVerticalNum - 1)
        int spaceY = (height - topBottomDy) / (lineHorizontalNum - 1);//竖直宽度  为 (height - topBottomDy) / (lineHorizontalNum - 1)

        for(int i=0;i<lineHorizontalNum;i++){
            if(i == 1 || i == lineHorizontalNum-2 ){
                paint.setStrokeWidth(5);
            }else{
                paint.setStrokeWidth(1);
            }
            canvas.drawLine(0,  i*spaceY + topBottomDy/2,  width, i*spaceY + topBottomDy/2, paint);
        }
        for(int j=0;j<lineVerticalNum;j++){
            if((j+2)%3 == 0){
                paint.setStrokeWidth(5);
            }else{
                paint.setStrokeWidth(1);
            }
            canvas.drawLine(j*spaceX + leftRightDx/2, 0, j*spaceX + leftRightDx/2, height , paint);
        }
    }
}
