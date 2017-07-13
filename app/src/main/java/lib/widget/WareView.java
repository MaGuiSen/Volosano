package lib.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mags on 2017/7/5.
 */

public class WareView extends View {
    private final Paint paint;
    private final Context context;
    List<PointRun> pointList = new ArrayList<>();
    boolean isRun = false;
    public WareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setColor(0xff5d9a00);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
        //设置阴影效果，注意setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setShadowLayer(12f, 0, 0, 0xff5d9a00);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void pause(){
        if(!isRun){
            return;
        }
        isRun = false;
        postInvalidate();
    }

    public void start(){
        if(isRun){
            return;
        }
        isRun = true;
        postInvalidate();
    }


    public void stop(){
        if(!isRun){
            return;
        }
        isRun = false;
        pointList.clear();
        postInvalidate();
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

        //能取的y点： 1 和 lineHorizontalNum-2
        //给数组添加两个相同的点，然后移动
        int size = pointList.size();
        if(size == 0){
            pointList.add(0, new PointRun(spaceX, 1*1.0f*spaceY + topBottomDy/2.0f, 1));
            pointList.add(0, new PointRun(0, 1*1.0f*spaceY + topBottomDy/2.0f, 1));
        }else{
            //划线
            for(int i=0;i<size;i++){
                PointRun start = pointList.get(i);
                if(i + 1>size - 1){
                    break;
                }
                PointRun end = pointList.get(i+1);
                canvas.drawLine(start.x, start.y, end.x, end.y, paint);
            }
        }

        size = pointList.size();
        //处理起始点空缺
        PointRun currFirst = pointList.get(0);
        if(currFirst.x <= spaceX){
            canvas.drawLine(0, currFirst.y, currFirst.x, currFirst.y, paint);
        }

        //给右侧第一个点加圆圈
        PointRun currLast = pointList.get(size - 1);
        canvas.drawCircle(currLast.x, currLast.y, 5, paint);

        //注意重新计算
        size = pointList.size();
        //画完，开始移动
        for(int i=0;i<size;i++){
            PointRun point = pointList.get(i);
            //移动为当前间距的1/10
            point.runX(spaceX/5);
        }
        //开始判断是否移除和添加
        size = pointList.size();
        if(size >= 2){
            PointRun first = pointList.get(0);
            PointRun second = pointList.get(1);
            if(first.x >= spaceX) {
                if (first.index == second.index) {
                    int random = new Random().nextInt(2);
                    if (random == 1) {
                        //添加同级
                        pointList.add(0, new PointRun(0, first.index * 1.0f * spaceY + topBottomDy / 2.0f, first.index));
                    } else {
                        if (first.index == 1) {
                            //添加竖直 向下
                            int yIndex = lineHorizontalNum - 2;
                            pointList.add(0, new PointRun(first.x, yIndex * 1.0f * spaceY + topBottomDy / 2.0f, yIndex));
                        } else {
                            //添加竖直 向上
                            int yIndex = 1;
                            pointList.add(0, new PointRun(first.x, yIndex * 1.0f * spaceY + topBottomDy / 2.0f, yIndex));
                        }
                    }
                } else {
                    //添加同级
                    pointList.add(0, new PointRun(0, first.index * 1.0f * spaceY + topBottomDy / 2.0f, first.index));
                }
            }
        }
        //注意重新计算长度
        size = pointList.size();
        //判断 去最后一条
        PointRun last = pointList.get(size - 1);
        if(last.isOut(width+spaceX)){
            pointList.remove(size - 1);
        }
        if(isRun) {
            postInvalidateDelayed(100);
        }
    }

    class PointRun{
        public float x;
        public float y;
        public int index;

        public PointRun(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        public boolean isOut(float width){
            return this.x > width;
        }

        public void runX(float space){
            this.x += space;
        }

        public void runY(float space){
            this.y += space;
        }
    }
}
