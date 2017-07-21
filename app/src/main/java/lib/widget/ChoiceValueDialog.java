package lib.widget;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.volosano.R;

public class ChoiceValueDialog {
    public BottomIndialog bottomIndialog;
    View dialogContentView;
    Activity context;
    TextView txtLeft, txtTitle;
    PickValueView pick_value_1;
    int value1 = 0;

    Integer values[] = {1,3,5,10,15,20,30,50,60};
    public ChoiceValueDialog(Activity context) {
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
        dialogContentView = context.getLayoutInflater().inflate(R.layout.layout_choice_value, null);
        bottomIndialog = new BottomIndialog(context, dialogContentView, true, true);

        pick_value_1 = (PickValueView) dialogContentView.findViewById(R.id.pick_value_1);
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
                    listener.choice(value1);
                }
            }
        });

        pick_value_1.setLeftStep(1);
        pick_value_1.setOnSelectedChangeListener(pickListener);
        pick_value_1.setValueData(values, values[0]);
    }

    PickValueView.onSelectedChangeListener pickListener = new PickValueView.onSelectedChangeListener() {
        @Override
        public void onSelected(PickValueView view, Object leftValue, Object middleValue, Object rightValue) {
            int left = (int) leftValue;
            if (view == pick_value_1) {
                txtTitle.setText(left+"Min");
                value1 = left;
            }
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
        void choice(int timeLong);
    }
}
