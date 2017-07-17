package ru.esko63.mycabinet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements DownloadCallback {

    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        textView = (TextView) findViewById(R.id.textView);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(),"https://www.google.com");
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            textView.setText(result);
        } else {
            textView.setText("Ошибка подключения");
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            case Progress.ERROR:
                textView.setText("Ошибка");
                //Log.d("myLog", "Ошибка");
                break;
            case Progress.CONNECT_SUCCESS:
                textView.setText("Соединение установлено");
                //Log.d("myLog", "Соединение установлено");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                textView.setText("Получение входящих данных");
                //Log.d("myLog", "Получение входящих данных");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                textView.setText("Обрабатываются входящие данные");
                //Log.d("myLog", "Обрабатываются входящие данные");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                textView.setText("Данные успешно обработаны");
                //Log.d("myLog", "Данные успешно обработаны");
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
