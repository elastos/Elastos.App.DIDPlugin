package com.ela.wallet.sdk.demo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.utils.DidEntry;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private Button button;
    private Button btn_back;
    private Button btn_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(mWebviewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baidu.com");
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_game = findViewById(R.id.btn_game);
        btn_game.setOnClickListener(this);

        //set true to print logs when debug;set false when release.
        DidEntry.openLog(true);
        //init the sdk library
        DidEntry.init(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                break;
            case R.id.btn_game:
                //example html
                webView.loadUrl("file:///android_asset/apidemo.html");
                break;
            case R.id.button:
                //internal UI in the library
                DidEntry.launch(MainActivity.this);
                break;
        }
    }

    private WebViewClient mWebviewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.toLowerCase().startsWith("http")) {
                view.loadUrl(url);
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

    };
}
