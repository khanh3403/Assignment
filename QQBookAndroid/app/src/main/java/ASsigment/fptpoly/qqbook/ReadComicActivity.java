package ASsigment.fptpoly.qqbook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadComicActivity extends AppCompatActivity {
    private static final String TAG = "ReadComicActivity";
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);
        Button btn = findViewById(R.id.btnBack_read);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pdfView = findViewById(R.id.pdfView);
        String pdfUrl = InforActivity.Constants.BASE_URL+"/pdf/" + getIntent().getStringExtra("images");
        displayFromUrl(pdfUrl);
    }

    private void displayFromUrl(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL pdfURL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) pdfURL.openConnection();
                    connection.connect();
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    pdfView.fromStream(inputStream).load();
                } catch (IOException e) {
                    Log.e(TAG, "Error while loading PDF", e);
                }
            }
        }).start();
    }
}
