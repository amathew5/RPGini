package com.example.admat.rpgini;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GameplayActivity extends AppCompatActivity {

    private MyDB db;
    private String username,table;

    GameplayView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        gameView = (GameplayView) findViewById(R.id.gameplay);
        gameView.addButtonListeners(this, new OnGameEndListener() {
            @Override
            public void onGameEnd(boolean won) {
                if (won) {
                    if(CurrentPlayerData.getInstance().checkLevelUp()) {
                        Toast.makeText(getApplicationContext(),"You leveled up!",Toast.LENGTH_SHORT).show();
                    }
                    CurrentPlayerData.getInstance().saveData(db,username,table);
                } /*else {
                    /// TODO: Find out why the below errors, but not the above.
                    //Toast.makeText(getApplicationContext(),"You passed out.",Toast.LENGTH_SHORT).show();
                }*/

                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        double seed = intent.getDoubleExtra("seed",1337);

        username = intent.getStringExtra("username");
        table = intent.getStringExtra("table");
        db = new MyDB(getApplicationContext());

        gameView = (GameplayView) findViewById(R.id.gameplay);
        gameView.setupBattle(seed,username,table);
    }

//    // This method executes when the player starts the game
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Tell the gameView resume method to execute
//        gameView.resume();
//    }
//
//    // This method executes when the player quits the game
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // Tell the gameView pause method to execute
//        gameView.pause();
//    }
}
