package com.example.admat.rpgini;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GameplayActivity extends AppCompatActivity {

    GameplayView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        Intent intent = getIntent();
        double seed = intent.getDoubleExtra("seed",1337);

        gameView = (GameplayView) findViewById(R.id.gameplay);
        gameView.setupBattle(seed);
        gameView.addButtonListeners(this, new OnGameEndListener() {
            @Override
            public void onGameEnd(boolean won) {
                if (won) {
                    //
                    Toast.makeText(getApplicationContext(),"Congrats!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Sux...",Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
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
