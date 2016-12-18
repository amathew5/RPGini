package com.example.admat.rpgini;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button continueButton;
    private AccessTokenTracker accesstokenTracker;
    private String name,email,password;
    private AccessToken accessToken;
    private EditText eulogin;
    private EditText eplogin;
    private Button emaillogin;
    private SQLiteDatabase db;
    private DbHelper dbhelper;
    private Button emailLO;
    private String table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        continueButton = (Button)findViewById(R.id.button2);
        eulogin = (EditText) findViewById(R.id.editText);
        eplogin = (EditText) findViewById(R.id.editText2);
        emaillogin = (Button) findViewById(R.id.emailloginbutton);
        emailLO = (Button) findViewById(R.id.button5);

        final String[] projectionemail = {
                DbHelper.EmailUserEntry._ID,
                DbHelper.EmailUserEntry.COLUMN_NAME_USERNAME,
                DbHelper.EmailUserEntry.COLUMN_NAME_PASSWORD,
                DbHelper.EmailUserEntry.COLUMN_NAME_PHYSICAL,
                DbHelper.EmailUserEntry.COLUMN_NAME_MAGICAL,
                DbHelper.EmailUserEntry.COLUMN_NAME_HEALTH,
                DbHelper.EmailUserEntry.COLUMN_NAME_LEVEL,
                DbHelper.EmailUserEntry.COLUMN_NAME_XP,
                DbHelper.EmailUserEntry.COLUMN_NAME_CHARNAME
        };

        final String selectionemail = DbHelper.EmailUserEntry.COLUMN_NAME_USERNAME + " = ?";

        final String[] projectionfb = {
                DbHelper.FBUserEntry._ID,
                DbHelper.FBUserEntry.COLUMN_NAME_USERNAME,
                DbHelper.FBUserEntry.COLUMN_NAME_PHYSICAL,
                DbHelper.FBUserEntry.COLUMN_NAME_MAGICAL,
                DbHelper.FBUserEntry.COLUMN_NAME_HEALTH,
                DbHelper.FBUserEntry.COLUMN_NAME_LEVEL,
                DbHelper.FBUserEntry.COLUMN_NAME_XP,
                DbHelper.FBUserEntry.COLUMN_NAME_CHARNAME
        };

        final String selectionfb = DbHelper.FBUserEntry.COLUMN_NAME_USERNAME + " = ?";

        dbhelper = new DbHelper(getApplicationContext());
        db = dbhelper.getWritableDatabase();

        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eulogin.getText().toString().equals("") || eplogin.getText().toString().equals("")){
                    Toast t = Toast.makeText(getApplicationContext(),"Please input something!",Toast.LENGTH_LONG);
                    t.show();
                }
                else {
                    String[] selectionArgs = {eulogin.getText().toString()};
                    Cursor c = db.query(DbHelper.EmailUserEntry.TABLE_NAME, projectionemail, selectionemail, selectionArgs, null, null, null);
                    if (c.moveToFirst()) {//if there is something returned
                        password = c.getString(c.getColumnIndex(DbHelper.EmailUserEntry.COLUMN_NAME_PASSWORD));
                        if (password.equals(eplogin.getText().toString())) {
                            table = "emailusers";
                            email = eulogin.getText().toString();
                            info.setText("Hi " + email);
                            eulogin.setText("");
                            eplogin.setText("");
                            eulogin.setEnabled(false);
                            eplogin.setEnabled(false);
                            continueButton.setEnabled(true);
                            loginButton.setEnabled(false);
                            emaillogin.setVisibility(View.INVISIBLE);
                            emailLO.setVisibility(View.VISIBLE);
                        } else {
                            Toast t = Toast.makeText(getApplicationContext(), "Sorry that login is incorrect!", Toast.LENGTH_LONG);
                            t.show();
                            eulogin.setText("");
                            eplogin.setText("");
                        }
                    } else { //if there is nothing returned
                        CurrentPlayerData.reset();
                        ContentValues values = new ContentValues();
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_USERNAME, eulogin.getText().toString());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_PASSWORD, eplogin.getText().toString());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_PHYSICAL, CurrentPlayerData.getInstance().getPhysical());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_MAGICAL, CurrentPlayerData.getInstance().getMagical());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_HEALTH, CurrentPlayerData.getInstance().getHealth());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_LEVEL, CurrentPlayerData.getInstance().getLevel());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_XP, CurrentPlayerData.getInstance().getXp());
                        values.put(DbHelper.EmailUserEntry.COLUMN_NAME_CHARNAME, CurrentPlayerData.getInstance().getName());
                        email = eulogin.getText().toString();
                        db.insertWithOnConflict(DbHelper.EmailUserEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                        info.setText("Hi " + eulogin.getText().toString() + "\n" + "Your account has been created!");
                        table = "emailusers";
                        eulogin.setText("");
                        eplogin.setText("");
                        eulogin.setEnabled(false);
                        eplogin.setEnabled(false);
                        continueButton.setEnabled(true);
                        loginButton.setEnabled(false);
                        emaillogin.setVisibility(View.INVISIBLE);
                        emailLO.setVisibility(View.VISIBLE);
                    }
                    c.close();
                }
            }
        });

        emailLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eulogin.setEnabled(true);
                eplogin.setEnabled(true);
                loginButton.setEnabled(true);
                continueButton.setEnabled(false);
                info.setText("");
                emaillogin.setVisibility(View.VISIBLE);
                emailLO.setVisibility(View.INVISIBLE);
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                continueButton.setEnabled(true);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                JSONObject json = response.getJSONObject();
                                Log.v("MainActivity", response.toString());
                                if(object != null){
                                    try {
                                        if(json != null){
                                            email = json.getString("email");
                                            name = json.getString("name");
                                            info.setText("Hi " + name + "\n" + "Email: " + email);
                                            String[] selectionArgs = { email };
                                            Cursor c = db.query(DbHelper.FBUserEntry.TABLE_NAME,projectionfb,selectionfb,selectionArgs,null,null,null);
                                            if (c.moveToFirst()) {
                                                table = "fbusers";
                                            }
                                            else{
                                                ContentValues values = new ContentValues();
                                                CurrentPlayerData.reset();
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_USERNAME, email);
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_PHYSICAL, CurrentPlayerData.getInstance().getPhysical());
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_MAGICAL, CurrentPlayerData.getInstance().getMagical());
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_HEALTH, CurrentPlayerData.getInstance().getHealth());
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_LEVEL, CurrentPlayerData.getInstance().getLevel());
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_XP, CurrentPlayerData.getInstance().getXp());
                                                values.put(DbHelper.FBUserEntry.COLUMN_NAME_CHARNAME, CurrentPlayerData.getInstance().getName());
                                                db.insertWithOnConflict(DbHelper.FBUserEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                                                table = "fbusers";
                                            }
                                            c.close();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                eulogin.setText("");
                eplogin.setText("");
                eplogin.setEnabled(false);
                eulogin.setEnabled(false);
                emaillogin.setEnabled(false);
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();
                accesstokenTracker.startTracking();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        accesstokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    info.setText("");
                    continueButton.setEnabled(false);
                    eplogin.setEnabled(true);
                    eulogin.setEnabled(true);
                    emaillogin.setEnabled(true);
                }
            }
        };

        if(isLoggedIn()){ //Put this here because what if the log in session is still active?
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            JSONObject json = response.getJSONObject();
                            Log.v("MainActivity", response.toString());
                            if(object != null){
                                try {
                                    if(json != null){
                                        email = json.getString("email");
                                        name = json.getString("name");
                                        info.setText("Hi " + name + "\n" + "Email: " + email);
                                        table = "fbusers";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            eplogin.setEnabled(false);
            eulogin.setEnabled(false);
            emaillogin.setEnabled(false);
            continueButton.setEnabled(true);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,email");
            request.setParameters(parameters);
            request.executeAsync();
            accesstokenTracker.startTracking();
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("table",table);
                intent.putExtra("username",email);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
