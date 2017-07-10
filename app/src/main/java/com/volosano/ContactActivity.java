package com.volosano;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.ToastUtil;

public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.edit_msg)
    EditText editMsg;
    @Bind(R.id.edit_email)
    EditText editEmail;
    @Bind(R.id.txt_tip)
    TextView txtTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        txtTip.setVisibility(View.GONE);
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @OnClick({R.id.img_back, R.id.txt_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.txt_send:
                if(isNetworkConnected(this)){
                    //设置成功
                    editMsg.setText("");
                    editEmail.setText("");
                    txtTip.setVisibility(View.VISIBLE);
                }else{
                    //提示无网络
                    ToastUtil.show("Network is disconnected.");
                }
                break;
        }
    }
}
