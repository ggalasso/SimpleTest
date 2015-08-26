package com.ggalasso.simpletest;

import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class FileHandlerAsync {

    //readMyXML().execute();
    private static FileHandlerAsync ourInstance = null;

    public static FileHandlerAsync getInstance() {
        if (ourInstance == null) {
            ourInstance = new FileHandlerAsync();
        }
        return ourInstance;
    }

    private FileHandlerAsync() {
        Log.i("INFO", "Instantiated File Handler");
    }
    private Integer myInt;

    public Integer getMyInt() {
        return myInt;
    }

    public void setMyInt(Integer myInt) {
        this.myInt = myInt;
    }

    public void getXMLData() {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        AsyncTask<String, Void, Integer> myAsync = new readMyXML().execute(downloadURL);
        try {
            Integer test = myAsync.get();
            Log.d("My Integer", String.format("Integer value = %d", test));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("My Integer", "End");
    }

    private class readMyXML extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            //BoardGameManager bgm = new BoardGameManager();
            InputStream is = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            Log.i("INFO", "REACHED doInBackground");
            //String downloadURL = "http://www.androidpeople.com/wp-content/uploads/2010/06/example.xml";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    //bgm = serializer.read(BoardGameManager.class, is);

                    Log.i("EXCEPTION -- MY ERROR!!", "Passed Test!");
                } finally {
                    con.disconnect();
                }
//                String idList = "";
//                for(BoardGame bg: bgm.getBoardGames()) {
//                    idList = idList + "," + bg.getObjectid();
//                }
//                Log.i("INFO XML DATA", "Concat List: " + idList);
                Log.i("INFO XML DATA", "Root Element from file handler:");

            } catch (Exception e) {
                Log.e("EXCEPTION -- MY ERROR!!", "exception" + e);
            }
            return 626;
        }

//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//
//            setMyInt(integer);
//
//            myMethod(integer);
//        }
    }

//    private Integer myMethod(Integer myValue){
//        return myInt;
//    }



}