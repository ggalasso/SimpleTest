package com.ggalasso.BggCollectionManager.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;


public class ImageService {

    public Bitmap getImage(String url) {
        try {
            return callImageService(url);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap callImageService(String url) throws ExecutionException, InterruptedException {
        Log.i("BGCM-IS", "Attempting to download data from: " + url);
        AsyncTask<String, Void, Bitmap> getImageTask = new getImageFromURL().execute(url);
        try {
            Bitmap result = getImageTask.get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getImageFromURL extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(params[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("BGCM-IS", "Error getting bitmap from: " + params[0], e);
            }
            return bm;
        }
    }
}
//    private class getImageFromURL extends AsyncTask<String, Void, Drawable> {
//        @Override
//        protected Drawable doInBackground(String... params) {
//            try {
//                InputStream is = (InputStream) new URL(params[0]).getContent();
//                Drawable d = Drawable.createFromStream(is, "src name");
//                return d;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//    }
//}

