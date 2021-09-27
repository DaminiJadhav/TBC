package com.truckbhejob2b.truckbhejocustomer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    String uri;
    Toolbar toolbar;
    String extension;
    String orderId;
    private String TAG="WebActivity";
    File saveFile;
    CustomProgressDialog customProgressDialog;
    TextView mTxtViewToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.view));
        mTxtViewToolbar = findViewById(R.id.toolbar_title);
        mTxtViewToolbar.setText(getString(R.string.view));

        setSupportActionBar(toolbar);
         customProgressDialog = new CustomProgressDialog(WebViewActivity.this);

        webView = findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            uri = extras.getString(RestKeys.URI);
            orderId = extras.getString(RestKeys.ORDER_ID);
         //   Log.d("TAG", "onCreate: "+uri);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
         extension = uri.substring(uri.lastIndexOf("."));
//        webView.loadUrl(uri);

        if (extension.equals(".pdf")){

            String url = "https://docs.google.com/viewer?embedded=true&url="+uri;
            webView.loadUrl(url);

        }else {
            webView.loadUrl(uri);

        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress ==100)
                    customProgressDialog.hide();
                else
                    customProgressDialog.show();
            }
        });
    }
    private void newDownload(String url) {
        final DownloadTask downloadTask = new DownloadTask(WebViewActivity.this);
        downloadTask.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.ic_action_download:
               // Log.d("Web View", "onOptionsItemSelected: ");
                newDownload(uri);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class DownloadTask extends AsyncTask<String,Integer,String>{
        private Context context;
        public DownloadTask(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... strings) {

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();


               String fileN = orderId+extension;
               File firstTime =  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"TBC");
               if (!firstTime.exists()){
                   firstTime.mkdir();
                    saveFile = new File(firstTime, "TBC" + fileN );
                   output = new FileOutputStream(saveFile);

               }
               // File filename = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"TBM", fileN);
                saveFile = new File(firstTime, "TBC" + fileN );
                output = new FileOutputStream(saveFile);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WebViewActivity.this,"Downloading File please wait...",Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s !=null)
                Toast.makeText(WebViewActivity.this,"Error While Downloading File..",Toast.LENGTH_LONG).show();
            else
            Toast.makeText(WebViewActivity.this,"File Download Successfully.."+saveFile,Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
