package com.ggalasso.BggCollectionManager.view;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ggalasso.BggCollectionManager.R;
import com.ggalasso.BggCollectionManager.api.XMLApi;
import com.ggalasso.BggCollectionManager.controller.BoardGameManager;
import com.ggalasso.BggCollectionManager.controller.GameIdManager;
import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryTable;
import com.ggalasso.BggCollectionManager.db.MechanicInGameTable;
import com.ggalasso.BggCollectionManager.db.MechanicTable;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;


import java.util.ArrayList;
import java.util.Map;

/// TODO : Create Layout for Main Activity

public class MainActivity extends ListActivity {
    Context ctx = this;
    private ArrayList<BoardGame> bgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String username = getIntent().getStringExtra("UserName");



        Log.d("BGCM-MA","Username = " + username);
        setTitle("Collection for " + username);


        BoardGameTable bgtCon = getBoardGameCollection(username);
        bgList = bgtCon.fetchAllBoardGames();

        ListView gameListView = getListView();
        setListAdapter(new GameAdapter(this, R.layout.game_item, bgList));

        bgtCon.destroyEverything();
    }


    @NonNull
    private BoardGameTable getBoardGameCollection(String username) {
        BoardGameTable bgtCon = new BoardGameTable(ctx);
        CategoryTable catCon = new CategoryTable(ctx);
        CategoryInGameTable cigtCon = new CategoryInGameTable(ctx);
        MechanicTable metCon = new MechanicTable(ctx);
        MechanicInGameTable migtCon = new MechanicInGameTable(ctx);

//        bgtCon.destroyEverything();

        XMLApi xapi = new XMLApi(GameIdManager.class, "https://boardgamegeek.com/xmlapi2/collection?username="+ username +"&own=1");
        GameIdManager gim = (GameIdManager)xapi.getAPIManager();
        String download2 = "https://boardgamegeek.com/xmlapi2/thing?id=" + gim.getIdListString() + "&stats=1";
        xapi = new XMLApi(BoardGameManager.class, download2);
        BoardGameManager bgm = (BoardGameManager)xapi.getAPIManager();

        bgm.getIdListString();

        bgtCon.syncBoardGameCollection(bgm.getBoardGames());

        ArrayList<BoardGame> bgList = bgtCon.fetchAllBoardGames();

        ArrayList<Link> caLinks = bgm.getCategoryLinks();
        for (Link link : caLinks) {
            Log.d("BCGM-MA", "Category link is: " + link.getValue() + " id: " + link.getId() + " and type: " + link.getType());
        }

//        catCon.delete("1002");
//        catCon.delete("1001");
//        catCon.delete("2145");

        Map<String, String> uniqueCategoriesMap = bgm.getUniqueCategories();
        catCon.syncCategories(uniqueCategoriesMap);

        Map<String, ArrayList<String>> categoriesInGame = bgm.getAllBoardGameCategories();
        cigtCon.insertAllCatergoriesInGame(categoriesInGame);

        Map<String, String> uniqueMechanicMap = bgm.getUniqueMechanics();
        metCon.syncMechanics(uniqueMechanicMap);

        Map<String, ArrayList<String>> mechanicsInGame = bgm.getAllBoardGameMechanics();
        migtCon.insertAllMechanicsInGame(mechanicsInGame);

        return bgtCon;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            gameTitleView.setText(bg.getPrimaryName());
            gameTimeView.setText(Integer.toString(bg.getPlayTime()));
            gameRatingView.setText(bg.getRatingToString());
            gamePlayersView.setText(bg.getMinPlayers() + "-" + bg.getMaxPlayers());

            return view;
        }
    }

}
