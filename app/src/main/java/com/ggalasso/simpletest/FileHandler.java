package com.ggalasso.simpletest;

import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FileHandler {

    //readMyXML().execute();
    private static FileHandler ourInstance = null;

    public static FileHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new FileHandler();
        }
        return ourInstance;
    }

    private FileHandler() {
        Log.i("INFO", "Instantiated File Handler");
    }

    public void getXMLData() {
        new readMyXML().execute();
    }

    private class readMyXML extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            BoardGameManager bgm = new BoardGameManager();
            Log.i("INFO", "REACHED doInBackground");
            //String downloadURL = "http://www.androidpeople.com/wp-content/uploads/2010/06/example.xml";
            String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
            try {
                URL url = new URL(downloadURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    bgm = serializer.read(BoardGameManager.class, is);

                    Log.i("EXCEPTION -- MY ERROR!!", "Passed Test!");
                } finally {
                    con.disconnect();
                }
                String idList = "";
                for(BoardGame bg: bgm.getBoardGames()) {
                    idList = idList + "," + bg.getObjectid();
                }
                Log.i("INFO XML DATA", "Concat List: " + idList);
                Log.i("INFO XML DATA", "Root Element from file handler:");

            } catch (Exception e) {
                Log.e("EXCEPTION -- MY ERROR!!", "exception" + e);
            }
            return "Executed from file handler.";
        }

    }



}