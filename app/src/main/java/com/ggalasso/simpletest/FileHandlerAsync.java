package com.ggalasso.simpletest;

import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class FileHandlerAsync {

   private static FileHandlerAsync ourInstance = null;

    public static FileHandlerAsync getInstance() {
        if (ourInstance == null) {
            ourInstance = new FileHandlerAsync();
        }
        return ourInstance;
    }
    private FileHandlerAsync() { Log.i("INFO", "Instantiated File Handler"); }

    public GameIDManager getIDList() throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        AsyncTask<String, Void, GameIDManager> getGameIDTask = new readGameIDs().execute(downloadURL);
        try {
            return getGameIDTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BoardGameManager getDetail(String queryString) throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/thing?id=" + queryString + "&stats=1";
        AsyncTask<String, Void, BoardGameManager> getXMLTask = new readMyXML().execute(downloadURL);
        try {
            return getXMLTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class readMyXML extends AsyncTask<String, Void, BoardGameManager> {
        @Override
        protected BoardGameManager doInBackground(String... params) {
            BoardGameManager bgm = BoardGameManager.getInstance();
            Log.i("INFO", "REACHED doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    bgm = serializer.read(BoardGameManager.class, is, false);

                    Log.i("EXCEPTION -- MY ERROR!!", "Passed Test!");
                } finally {
                    con.disconnect();
                }
                Log.i("INFO XML DATA", "Root Element from file handler:");

            } catch (Exception e) {
                Log.e("EXCEPTION -- MY ERROR!!", "exception" + e);
            }
            return bgm;
        }
    }

    private class readGameIDs extends AsyncTask<String, Void, GameIDManager> {
        @Override
        protected GameIDManager doInBackground(String... params) {
            GameIDManager manager = GameIDManager.getInstance();
            Log.i("INFO", "REACHED doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    manager = serializer.read(GameIDManager.class, is, false);

                    Log.i("EXCEPTION -- MY ERROR!!", "Passed Test!");
                } finally {
                    con.disconnect();
                }
                Log.i("INFO XML DATA", "Root Element from file handler:");

            } catch (Exception e) {
                Log.e("EXCEPTION -- MY ERROR!!", "exception" + e);
            }
            return manager;
        }
    }
}