package org.d3if3040.logindanqrcode.ViewModel;

// ImageApiActivity.java
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.d3if3040.logindanqrcode.MainActivity;
import org.d3if3040.logindanqrcode.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageApiActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_api);

        imageView = findViewById(R.id.image_view);

        // Replace "API_IMAGE_URL" with the actual URL of the image API
        String imageUrl = "API_IMAGE_URL";
        loadImageFromUrl(imageUrl);

        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageApiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadImageFromUrl(String imageUrl) {
        ImageLoaderTask task = new ImageLoaderTask();
        task.execute(imageUrl);
    }

    private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

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
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                Toast.makeText(ImageApiActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

