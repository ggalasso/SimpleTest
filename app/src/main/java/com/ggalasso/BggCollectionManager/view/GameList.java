package com.ggalasso.BggCollectionManager.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggalasso.BggCollectionManager.R;
import com.ggalasso.BggCollectionManager.model.BoardGame;

import java.io.File;
import java.util.ArrayList;

public class GameList extends AppCompatActivity {

    private ExpandableListView simpleExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.game_item);

        //gl.setListAdapter(new MainActivity.GameAdapter(this, R.layout.game_item, bgm.getBoardGames()));
    }

    class GameAdapter extends ArrayAdapter<BoardGame> {

        public GameAdapter(Context context, int resource, ArrayList<BoardGame> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            gameTitleView.setText(bg.getPrimaryName());
            gameTimeView.setText(bg.getMinMaxTimeToString());
            gameRatingView.setText(bg.getRatingToString());
            gamePlayersView.setText(bg.getMinMaxPlayersToString());

            //http://stackoverflow.com/questions/4181774/show-image-view-from-file-path#answer-4182060
            if (bg.getThumbnailPath() != null) {
                File imgFile = new File(bg.getThumbnailPath());
                if (imgFile.exists()) {
                    Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    thumbImg.setImageBitmap(b);
                } else {
                    //TODO: Generic image to display if thumbnail not available
                }
            }

            Button btn = (Button)view.findViewById(R.id.btnExpand);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View btnview) {
                    //TextView tone = (TextView) view.findViewById(R.id.textView2);
                    View pview = (View) btnview.getParent();
                    View topview = (View) pview.getParent();
                    TextView tone = (TextView) topview.findViewById(R.id.textView2);
                    LinearLayout la = (LinearLayout) topview.findViewById(R.id.laextend) ;
                    if(la.getVisibility() == View.GONE) {
                        la.setVisibility(View.VISIBLE);
                    } else {
                        la.setVisibility(View.GONE);
                    }
                    // TextView ttwo = (TextView) view.findViewById(R.id.textView3);

                    tone.setText("Hellow");
                    // ttwo.setText("World");
                }
            });
            return view;
        }
    }

}
