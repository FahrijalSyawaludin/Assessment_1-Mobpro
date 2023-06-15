package org.d3if3040.logindanqrcode.ViewModel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.d3if3040.logindanqrcode.worker.ImageApiWorker;
import org.d3if3040.logindanqrcode.R;
import org.d3if3040.logindanqrcode.MainActivity; // Add this import statement

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class ImageApiActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private WorkManager workManager;
    private UUID workRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_api);

        imageView = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);
        workManager = WorkManager.getInstance(this);

        String imageUrl = "https://raw.githubusercontent.com/FahrijalSyawaludin/API/main/Screenshot_2019-06-26-15-34-11-156_com.miui.gallery.png";

        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageApiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Membuat data yang akan diteruskan ke Worker
        Data inputData = new Data.Builder()
                .putString(ImageApiWorker.KEY_IMAGE_URL, imageUrl)
                .build();

        // Membuat permintaan kerja satu kali
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ImageApiWorker.class)
                .setInputData(inputData)
                .build();

// Memulai kerja
        workRequestId = workRequest.getId();
        workManager.enqueue(workRequest);

// Memantau status kerja dan menampilkan hasilnya
        workManager.getWorkInfoByIdLiveData(workRequestId).observe(this, workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                // Menerima data hasil dari Worker
                String imageUrlResult = workInfo.getOutputData().getString(ImageApiWorker.KEY_IMAGE_URL);
                if (imageUrlResult != null) {
                    // Load the image using the URL
                    loadImageFromUrl(imageUrlResult);
                } else {
                    Toast.makeText(ImageApiActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadImageFromUrl(String imageUrl) {
        ImageLoaderTask task = new ImageLoaderTask();
        task.execute(imageUrl);
    }

    private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private String errorMessage;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                Toast.makeText(ImageApiActivity.this, "Failed to load image: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Share Message Here");
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
