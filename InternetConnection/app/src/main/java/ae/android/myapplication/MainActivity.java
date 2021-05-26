package ae.android.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    static final String TAG = "Internet";
    static final String CHECK_DOMAIN = "i.ua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isNetworkConnected = isNetworkConnected();
                        boolean isInternetAvailable = isInternetAvailable();
                        Log.d(TAG, "isNetworkConnected:" + isNetworkConnected + ". isInternetAvailable:" + isInternetAvailable);
                        if (isNetworkConnected && isInternetAvailable) {
                            boolean connectGoogle = connectGoogle();
                            Log.d(TAG, "connectGoogle:" + connectGoogle);
                        }
                    }
                }).start();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @WorkerThread
    public boolean isInternetAvailable() {
        try {
            String checkDomain = "i.ua";
            InetAddress ipAddr = InetAddress.getByName(checkDomain);
            Log.d(TAG, "isInternetAvailable. checkDomain:" + checkDomain + ". ipAddr:" + ipAddr.toString());
            return !ipAddr.equals("");
        } catch (NetworkOnMainThreadException e) {
            Log.d(TAG, "isInternetAvailable. NetworkOnMainThreadException:" + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.d(TAG, "isInternetAvailable. Exception:" + e.getMessage());
            return false;
        }
    }

    @Nullable
    public String getServerIp() {
        try {
            InetAddress ipAddr = InetAddress.getByName(CHECK_DOMAIN);
            Log.d(TAG, "getServerIp. domain:" + CHECK_DOMAIN + ". ipAddr:" + ipAddr.toString());
            return Arrays.toString(ipAddr.getAddress());
        } catch (NetworkOnMainThreadException e) {
            Log.d(TAG, "isInternetAvailable. NetworkOnMainThreadException:" + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.d(TAG, "isInternetAvailable. Exception:" + e.getMessage());
            return null;
        }
    }

    public static boolean connectGoogle() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(10000);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.d(TAG, "connectGoogle. IOException:" + e.getMessage());
            return false;
        }
    }
}