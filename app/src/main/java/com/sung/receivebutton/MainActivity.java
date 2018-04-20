package com.sung.receivebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = 0;

        final ReceiveSelectButton btn = findViewById(R.id.btn);
        final TextView start = findViewById(R.id.start);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = mode + 1;
                if (mode > 4){
                    mode = 0;
                }
                btn.updateStatus(mode);
            }
        });
        btn.addOnProgressCompleteListener(new ReceiveSelectButton.OnProgressComplete() {
            @Override
            public void onFullProgress() {
                Toast.makeText(MainActivity.this, "进度完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopProgress() {
                start.setText("start countting");
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode != ReceiveSelectButton.SELECT_STATUS_CONTINUE){
                    Toast.makeText(MainActivity.this, "不在进行中哦", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (btn.isCountting()){
                    btn.stopCountTime();
                }else {
                    btn.startCountTime();
                }

                start.setText(btn.isCountting() ? "pause countting" : "start countting");
            }
        });
    }
}
