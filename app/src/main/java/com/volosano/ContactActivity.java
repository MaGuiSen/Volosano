package com.volosano;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    }

    @OnClick({R.id.img_back, R.id.txt_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.txt_send:
                break;
        }
    }
}
