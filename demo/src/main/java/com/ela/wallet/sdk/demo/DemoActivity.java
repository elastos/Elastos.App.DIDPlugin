package com.ela.wallet.sdk.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        textView = findViewById(R.id.textView);
        textView.setText("调用结果会toast提示");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn1:
                String fromAddress = "ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4";
//                DidLibrary.Chongzhi(fromAddress, 100000000);
//                break;
//            case R.id.btn2:
//                DidLibrary.testTixian();
//                break;
//            case R.id.btn3:
//                DidLibrary.testZhuanzhang();
//                break;
//            case R.id.btn4:
//                DidLibrary.testSHoukuan();
                break;
        }
    }
}
