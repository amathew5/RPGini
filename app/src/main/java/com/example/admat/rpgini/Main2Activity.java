package com.example.admat.rpgini;

import android.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {

    private EditText cn;
    private TextView phys;
    private TextView magi;
    private TextView health;
    private TextView level;
    private TextView xp;
    private Button cont;
    private Button newgame;
    private MyDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /// Request permission for the GPS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        database = new MyDB(getApplicationContext());
        cn = (EditText) findViewById(R.id.editText3);
        phys = (TextView) findViewById(R.id.textView4);
        magi = (TextView) findViewById(R.id.textView5);
        health = (TextView) findViewById(R.id.textView6);
        level = (TextView) findViewById(R.id.textView7);
        xp = (TextView) findViewById(R.id.textView8);
        cont = (Button) findViewById(R.id.button3);
        newgame = (Button) findViewById(R.id.button4);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String table = intent.getStringExtra("table");

        String c = database.getCharName(username,table);
        cn.setText(c.toCharArray(),0,c.length());
        int p = database.getPhysical(username,table);
        int m = database.getMagical(username,table);
        int h = database.getHealth(username,table);
        int l = database.getLevel(username,table);
        int x = database.getXP(username,table);
        phys.setText("Physical: " + p);
        magi.setText("Magical: " + m);
        health.setText("Health: " + h);
        level.setText("Level: " + l);
        xp.setText("XP: " + x);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cn.getText().toString().equals("")){
                    Toast t = Toast.makeText(getApplicationContext(), "You need to have a name for your character!",Toast.LENGTH_LONG);
                    t.show();
                }
                else{
                    database.setCharName(username, cn.getText().toString(), table);
                    Intent toGame = new Intent(Main2Activity.this,MapsActivity.class);
                    //Intent toGame = new Intent(Main2Activity.this,GameplayActivity.class);
                    toGame.putExtra("username",username);
                    toGame.putExtra("table",table);
                    startActivity(toGame);
                }
            }
        });

        final AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete.setTitle("Are you sure you want to start a new game? (All progress will be lost)");
                confirmDelete.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                database.setCharName(username, "Harambe", table);
                                database.setPhysical(username, 20, table);
                                database.setMagical(username, 20, table);
                                database.setHealth(username, 100, table);
                                database.setLevel(username, 1, table);
                                database.setXP(username, 0, table);

                                cn.setText("Harambe".toCharArray(),0,"Harambe".length());
                                phys.setText("Physical: " + 20);
                                magi.setText("Magical: " + 20);
                                health.setText("Health: " + 100);
                                level.setText("Level: " + 1);
                                xp.setText("XP: " + 0);

                                newgame.setEnabled(false);
                            }
                        });

                confirmDelete.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = confirmDelete.create();
                alert.show();
            }
        });
    }
}
