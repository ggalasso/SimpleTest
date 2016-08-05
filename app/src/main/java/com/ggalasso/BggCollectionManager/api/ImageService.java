package com.ggalasso.BggCollectionManager.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.concurrent.ExecutionException;

// TODO : eqc 2016 July 10 | Make this a singleton pattern (maybe?)
public class ImageService {
    private String imgStorageDir;

    public String getImgStorageDir() {
        if (this.imgStorageDir == null){
            this.imgStorageDir = Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + BuildConfig.APPLICATION_ID
                    + "/img_t/";
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
        File imageLocation = getOutputMediaFile(fileName);

        if (imageLocation == null) {
            Log.d("BGCM-IS", "Error creating media file, something failed when generating file location.");
            return false;
        }

        try {
            FileOutputStream fos = new FileOutputStream(imageLocation);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            Log.d("BGCM-IS", "Saved: " + fileName);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d("BGCM-IS", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("BGCM-IS", "Error accessing file: " + e.getMessage());
        }
        return false;
    }

    public String getFileNameFromURL(String url) {
        int i = url.lastIndexOf("/");
        String fileName =  url.substring(i+1, url.length());
        return fileName;
    }

    public boolean getAndStoreImage(String url) {
        String file = getFileNameFromURL(url);
        Bitmap bm = getImage(url);
        return storeImage(bm,file);
    }

    public void deleteImageDirectory(){
        deleteFileOrDirectory(new File(getImgStorageDir()));
    }

    public void deleteImageFile(File file) {
        deleteFileOrDirectory(file);
    }

    private void deleteFileOrDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                Log.d("BGCM-IS", child.getName() + " was deleted");
                deleteFileOrDirectory(child);
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

        //TODO: Check to see if external storage is mounted and only proceed if it is.
        File mediaStorageDir;
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Log.d("BGCM-IS-File","ERROR - The media is not mounted" + Environment.getExternalStorageState());
            return null;
        } else {
            mediaStorageDir = new File(getImgStorageDir());
        }

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("BGCM-IS-File","ERROR!!!!: Couldn't make directory!");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        Log.d("BGCM-IS", "Media File (name - path) " + mediaFile.getName() + " - " + mediaFile.getPath());
        return mediaFile;
    }


}


