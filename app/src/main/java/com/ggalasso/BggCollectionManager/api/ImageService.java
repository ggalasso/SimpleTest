package com.ggalasso.BggCollectionManager.api;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ggalasso.BggCollectionManager.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

// TODO : eqc 2016 July 10 | Make this a singleton pattern (maybe?)
public class ImageService {
    private String imgStorageDir;

    public String getImgStorageDir() {
        if (this.imgStorageDir == null){
            Log.d("BGCM-IS", "ImgStoreDir is null");
        } else {
            Log.d("BGCM-IS", "ImgStoreDir is NOT NULL");
        }
        if (this.imgStorageDir == null){
            this.imgStorageDir = Environment.getExternalStorageDirectory()
                    + "/Andoid/data/"
                    + BuildConfig.APPLICATION_ID
                    + "/img_t";
        }
        return imgStorageDir;
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

    public boolean storeImage(Bitmap image, String fileName) {
        File pic = getOutputMediaFile(fileName);

        if (pic == null) {
            Log.d("BGCM-IS", "Error creating media file, check storage permissions: ");
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pic);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            Log.d("BGCM-IS", "Saved: " + fileName);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("BGCM-IS", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("BGCM-IS", "Error accessing file: " + e.getMessage());
        }

        return true;
    }

    public String getFileNameFromURL(String url) {
        int i = url.lastIndexOf("/");
        String fileName =  url.substring(i+1, url.length());
        return fileName;
    }

    public boolean getAndStoreImage(String url) {
        String file = getFileNameFromURL(url);
        Log.d("BGCM-IS", "File name: " + file);
        return storeImage(getImage(url),file);

    }

    public void deleteImageDirectory(){
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

    /**
     * http://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage#answer-15662384
     * Create a File for saving an image or video
     **/
    private File getOutputMediaFile(String fileName) {
        Log.d("BGCM-IS-File","External Storage State: " + Environment.getExternalStorageState());

        File mediaStorageDir = new File(getImgStorageDir());

        //TODO: Check to see if external storage is mounted and only proceed if it is.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
      //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new De());
//        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
//        String timeStamp = df.parse()
//        Log.d("BGCM-IS", "timeStamp = " + timeStamp);
        File mediaFile;
//        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        Log.d("BGCM-IS", "Media File (name - path) " + mediaFile.getName() + " - " + mediaFile.getPath());
        return mediaFile;

    }
}


