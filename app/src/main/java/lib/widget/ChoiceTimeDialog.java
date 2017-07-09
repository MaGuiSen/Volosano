package lib.widget;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.volosano.R;

import java.util.Calendar;

public class ChoiceTimeDialog {
    Integer hours[] = null;
    Integer minutes[] = null;
//    public static void main(String[] args){
//        Integer hours[] = null;
//        Integer minutes[] = null;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 24);
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int minute = cal.get(Calendar.MINUTE);
//        hours = new Integer[25 - hour];
//        minutes = new Integer[60];
//        for(int i = 0; i < (25 - hour);i++){
//            hours[i] = i + hour;
//            System.out.println(hours[i]);
//        }
//        System.out.println();
//        for(int i = 0; i < 60;i++){
//            minutes[i] = i;
//            System.out.println(minutes[i]);
//        }
//    }

    public BottomIndialog bottomIndialog;
    View dialogContentView;
    Activity context;
    TextView txtLeft, txtTitle;
    PickValueView pick_value_1;
    PickValueView pick_value_2;
    int value1 = 0;
    int value2 = 0;

    public ChoiceTimeDialog(Activity context) {
        this.context = context;
        init();
    }

    public void show() {
        bottomIndialog.show();
    }

    public void dismiss() {
        bottomIndialog.dismiss();
    }

    public void init() {
        dialogContentView = context.getLayoutInflater().inflate(R.layout.layout_choice_time, null);
        bottomIndialog = new BottomIndialog(context, dialogContentView, true, true);

        pick_value_1 = (PickValueView) dialogContentView.findViewById(R.id.pick_value_1);
        pick_value_2 = (PickValueView) dialogContentView.findViewById(R.id.pick_value_2);
        txtTitle = (TextView) dialogContentView.findViewById(R.id.txt_title);
        txtLeft = (TextView) dialogContentView.findViewById(R.id.txt_cancel);
        txtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialogContentView.findViewById(R.id.txt_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    listener.choice(value1, value2);
                }
            }
        });
        pick_value_2.setOnSelectedChangeListener(pickListener);
        pick_value_1.setOnSelectedChangeListener(pickListener);
        //获取当前时间的Hour 和 minute
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        hours = new Integer[24 - hour];
        minutes = new Integer[60];
        for(int i = 0; i < (24 - hour);i++){
            hours[i] = i + hour;
            System.out.println(hours[i]);
        }
        System.out.println();
        for(int i = 0; i < 60;i++){
            minutes[i] = new Integer(i);
            System.out.println(minutes[i]);
        }
        pick_value_1.setLeftStep(1);
        pick_value_2.setLeftStep(1);
        pick_value_1.setValueData(hours, hours[0]);
        pick_value_2.setValueData(minutes, minute);
    }

    PickValueView.onSelectedChangeListener pickListener = new PickValueView.onSelectedChangeListener() {
        @Override
        public void onSelected(PickValueView view, Object leftValue, Object middleValue, Object rightValue) {
            int left = (int) leftValue;
            if (view == pick_value_1) {
                value1 = left;
            }else if(view == pick_value_2 ){
                value2 = left;
            }
            txtTitle.setText("Time:" + value1+":"+value2);
        }
    };

    /**
     * 对话框点击
     */
    DialogClickListener listener;

    public void setDialogClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public interface DialogClickListener {
        void choice(int hour, int minute);
    }
}
