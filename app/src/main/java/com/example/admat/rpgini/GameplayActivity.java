package com.example.admat.rpgini;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameplayActivity extends AppCompatActivity {

    GameplayView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        Intent intent = getIntent();
        double seed = intent.getDoubleExtra("seed",1337);

        gameView = (GameplayView) findViewById(R.id.gameplay);
        gameView.addButtonListeners(this);
        gameView.setupBattle(seed);
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
