package com.ggalasso.BggCollectionManager.api;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;


public class ImageService {
    private String imgStorageDir;

    public String getImgStorageDir() {
        return imgStorageDir;
    }

    public void setImgStorageDir(Context ctx) {
        this.imgStorageDir = Environment.getExternalStorageDirectory()
                + "/Andoid/data/"
                + ctx.getString(ctx.getApplicationInfo().labelRes)
                + "/img_t";
    }

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

    public void storeImage(Context ctx, Bitmap image) {
        File pic = getOutputMediaFile(ctx);

    }

    public void deleteImageDirectory(Context ctx){
        if (getImgStorageDir() == null){
            setImgStorageDir(ctx);
        }

        deleteRecursive(new File(getImgStorageDir()));
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                Log.d("BGCM-IS", child.getName() + " was deleted");
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private File getOutputMediaFile(Context ctx) {
        Log.d("BGCM-IS-File","External Storage State: " + Environment.getExternalStorageState());

        if (getImgStorageDir() == null){
            setImgStorageDir(ctx);
        }

        File mediaStorageDir = new File(getImgStorageDir());

        return null;
    }
}


