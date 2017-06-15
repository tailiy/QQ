package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private TextView tv;
    private Button re;
    private Button back;


    private String urlstr =  "https://daohang.qq.com/?fr=hmpage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        mWebView = (WebView) findViewById(R.id.WebView);
        tv = (TextView) findViewById(R.id.text);
        re = (Button) findViewById(R.id.refresh);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(urlstr);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            InputStream is = conn.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
                            final StringBuffer buffer = new StringBuffer();

                            String line = br.readLine();
                            while(line!=null){
                                buffer.append(line);
                                line = br.readLine();

                            }

                            Gson gson = new Gson();
                            final InfoBean infoBean =gson.fromJson(buffer.toString(), InfoBean.class);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText((CharSequence) infoBean);

                                }
                            });


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        back = (Button) findViewById(R.id.back);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(urlstr);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    Toast toast =  Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            return false;
        }
        return false;
    }
}
