package kr.co.first2000.chkconnectionaspinginjava;

import android.os.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class InternetStateMonitor extends Thread{

    private String TAG = "INTERNET_MONITOR => ";
    private Handler handler;
    private boolean stillChecking = false;

//    전에보낸 Message판독.
//    Wrong를 보냈으면 1
//    Fine을 보냈으면 2
//    처음시작시 0
    private int switcher = 0;

    public InternetStateMonitor(Handler handler) {
        this.handler = handler;
        this.stillChecking = true;
    }

    public void EndMonitoring() {
        this.stillChecking = false;
    }

    @Override
    public void run() {
        super.run();

//        소켓과 구글DNS서버주소 지정.
        InetSocketAddress sockAddr = new InetSocketAddress("8.8.8.8", 53);

        while (stillChecking) {
            try {
//                ping 요청 후 TIMEOUT: 1500 을 지날시 IOException/InterruptedIOException/SocketTimeoutException 발생
                Socket soc = new Socket();
                soc.connect(sockAddr, 500);
                soc.close();
                if (switcher == 1 || switcher == 0) {
                    handler.sendEmptyMessage(InternetStateValue.NETWORK_STATE_FINE);
                    switcher = 2;
                }
            } catch (IOException e) {
                if (switcher == 2 || switcher == 0) {
                    handler.sendEmptyMessage(InternetStateValue.NETWORK_STATE_WRONG);
                    switcher = 1;
                }
            }
//            1s 텀
            try{ sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
