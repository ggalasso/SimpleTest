package com.ggalasso.BggCollectionManager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ggalasso.BggCollectionManager.R;

/**
 * Created by ggalasso on 6/16/2016.
 */
public class LoginActivity extends AppCompatActivity{
    Button loginButton;
    EditText usernameTextBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        usernameTextBox = (EditText)findViewById(R.id.usernameTextBox);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d("BGCM-LA","We Clickity Clicked the button: text = " + usernameTextBox.getText().toString());
                Intent usernameIntent = new Intent(view.getContext(), MainActivity.class);
                usernameIntent.putExtra("UserName",usernameTextBox.getText().toString());
                startActivity(usernameIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
