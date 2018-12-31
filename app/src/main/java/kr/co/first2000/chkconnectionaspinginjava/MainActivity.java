package kr.co.first2000.chkconnectionaspinginjava;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private InternetStateMonitor IMonitor;
    private Handler handler;

    private TextView Txt;
    private ImageView Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    @SuppressLint("HandlerLeak")
    private void init() {
        Txt = findViewById(R.id.NetworkTxt);
        Img = findViewById(R.id.NetworkImg);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case InternetStateValue.NETWORK_STATE_FINE:
                        Txt.setText("네트워크연결");
                        Img.setImageResource(R.drawable.ic_network_good);
                        break;
                    case InternetStateValue.NETWORK_STATE_WRONG:
                        Txt.setText("네트워크연결끊김");
                        Img.setImageResource(R.drawable.ic_network_lost);
                        break;
                }
            }
        };
        IMonitor = new InternetStateMonitor(handler);
        IMonitor.setDaemon(true);
        IMonitor.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMonitor.EndMonitoring();
    }
}
