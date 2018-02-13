package com.ggalasso.BggCollectionManager.view;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggalasso.BggCollectionManager.R;
import com.ggalasso.BggCollectionManager.controller.BoardGameManager;
import com.ggalasso.BggCollectionManager.model.BoardGame;

import java.io.File;
import java.util.ArrayList;

public class GameList extends ListActivity {
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game_list);

        String username = getIntent().getStringExtra("UserName");

        Log.d("BGCM-GL", "Username = " + username);
        setTitle("Collection for " + username);

        //Main series of steps
        BoardGameManager bgm = BoardGameManager.getInstance();
        bgm.loadBoardGameCollection(username, ctx);
        setListAdapter(new GameAdapter(this, R.layout.game_item, bgm.getBoardGames()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BoardGame bg = (BoardGame) getListAdapter().getItem(position);
        Toast.makeText(this, "Clicked " + bg.getThumbnailURL() + " (" + id + ")", Toast.LENGTH_LONG).show();
        Log.d("BGCM-GL:", "Successfully added " + bg.getId());
    }

    class GameAdapter extends ArrayAdapter<BoardGame> {

        public GameAdapter(Context context, int resource, ArrayList<BoardGame> objects) {
            super(context, resource, objects);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.game_item, parent, false);
            } else {
                view = convertView;
            }

            BoardGame bg = getItem(position);

            TextView gameTitleView = (TextView) view.findViewById(R.id.gameTitleText);
            TextView gameTimeView = (TextView) view.findViewById(R.id.gameTimeText);
            TextView gameRatingView = (TextView) view.findViewById(R.id.gameRatingText);
            TextView gamePlayersView = (TextView) view.findViewById(R.id.gameNumPlayersText);
            ImageView thumbImg = (ImageView) view.findViewById(R.id.gameImageView);

            if (bg != null) {
                gameTitleView.setText(bg.getPrimaryName());
                gameTimeView.setText(bg.getMinMaxTimeToString());
                gameRatingView.setText(bg.getRatingToString());
                gamePlayersView.setText(bg.getMinMaxPlayersToString());

                if (bg.getThumbnailPath() != null || bg.getThumbnailPath() != "nofilepath") {
                    File imgFile = new File(bg.getThumbnailPath());
                    if (imgFile.exists()) {
                        Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        thumbImg.setImageBitmap(b);
                    } else {
                        thumbImg.setImageResource(R.drawable.bgg_logo);
                    }
                }
            } else {
                //TODO: No board game object was found, handle this somehow
            }

            return view;
        }
    }

}
