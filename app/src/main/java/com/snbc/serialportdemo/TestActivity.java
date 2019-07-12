package com.snbc.serialportdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestActivity";
    private EditText mSendMsgEt;
    private Button mSendMsgBtn;
    private TextView mReceiveMsgTv;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private StringBuffer mStringBuffer = new StringBuffer();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mStringBuffer.append((String) (msg.obj));
                    mReceiveMsgTv.setText(mStringBuffer);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mSendMsgEt = (EditText) findViewById(R.id.send_msg_et);
        mSendMsgBtn = (Button) findViewById(R.id.send_msg_btn);
        mReceiveMsgTv = (TextView) findViewById(R.id.receive_msg_tv);
        mSendMsgBtn.setOnClickListener(this);

        SerialPortHelp.openSericalPort("/dev/ttysWK0", 9600, 0);
        mOutputStream = SerialPortHelp.mOutputStream;
        mInputStream = SerialPortHelp.mInputStream;

        new Thread(new Runnable() {
            @Override
            public void run() {
                readData();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_msg_btn:

                String s = mSendMsgEt.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(TestActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mOutputStream.write(s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void readData() {

        String readData = null;

        while (true) {
            try {
                if (mInputStream != null) {
                    byte[] buffer = new byte[4];
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        readData = buffer.toString();
                        Log.i(TAG, "readData: " + readData);
                        mHandler.obtainMessage(0, readData);
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                readData();
            }
        }
    }


}
