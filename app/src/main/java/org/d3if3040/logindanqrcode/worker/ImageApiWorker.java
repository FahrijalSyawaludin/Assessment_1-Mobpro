package org.d3if3040.logindanqrcode.worker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageApiWorker extends Worker {

    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_RESULT = "result";

    public ImageApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String imageUrl = getInputData().getString(KEY_IMAGE_URL);

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            // Read the input stream into a byte array
            byte[] imageBytes = readInputStreamToByteArray(inputStream);

            // Resize the image
            Bitmap bitmap = resizeImage(imageBytes, 800, 600);

            // Pass the URL and resized bitmap to the activity
            Data outputData = new Data.Builder()
                    .putString(KEY_IMAGE_URL, imageUrl)
                    .build();

            // Return the result
            return Result.success(outputData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private byte[] readInputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    private Bitmap resizeImage(byte[] imageBytes, int desiredWidth, int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);

        // Calculate the inSampleSize value
        options.inSampleSize = calculateInSampleSize(options, desiredWidth, desiredHeight);

        // Decode the bitmap with the calculated inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap resizedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);

        return resizedBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int desiredWidth, int desiredHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > desiredHeight || width > desiredWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // the height and width larger than the desired dimensions
            while ((halfHeight / inSampleSize) >= desiredHeight
                    && (halfWidth / inSampleSize) >= desiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
