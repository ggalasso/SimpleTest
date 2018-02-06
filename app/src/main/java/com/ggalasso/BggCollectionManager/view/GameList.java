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
    //private ListView simpleListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //simpleListView = (ListView) findViewById(android.R.id.list);

        String username = getIntent().getStringExtra("UserName");

        Log.d("BGCM-MA", "Username = " + username);
        setTitle("Collection for " + username);

        //Main series of steps
        BoardGameManager bgm = BoardGameManager.getInstance();
        bgm.loadBoardGameCollection(username, ctx);
        //GameAdapter bgAdapter = new GameAdapter(this, R.layout.game_item, bgm.getBoardGames());
        setListAdapter(new GameAdapter(this, R.layout.game_item, bgm.getBoardGames()));
        //simpleListView.setAdapter(bgAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BoardGame bg = (BoardGame) getListAdapter().getItem(position);
        Toast.makeText(this, "Clicked " + bg.getPrimaryName() + " (" + id + ")", Toast.LENGTH_LONG).show();
        Log.d("BGCM-OH SHIT", "Successfully added " + bg.getId());

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


            //http://stackoverflow.com/questions/4181774/show-image-view-from-file-path#answer-4182060



 /*           Button expand2 = (Button) view.findViewById(R.id.button2);
            expand2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View expandView) {
                    //TextView tone = (TextView) view.findViewById(R.id.textView2);
                    View pview = (View) expandView.getParent();
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

            ImageView expand = (ImageView) view.findViewById(R.id.expandArrow);
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View expandView) {
                    //TextView tone = (TextView) view.findViewById(R.id.textView2);
                    View pview = (View) expandView.getParent();
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
*/
            return view;
        }
    }

}
