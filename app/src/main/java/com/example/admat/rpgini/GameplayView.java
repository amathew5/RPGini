package com.example.admat.rpgini;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by galax_000 on 11/20/2016.
 */
public class GameplayView extends SurfaceView implements Runnable {

    protected EditText gameplayLog,gameplayStat;
    Thread gameplayThread = null;
    volatile boolean playing;
    OnGameEndListener gameEnd;

    private List<Enemy> enemyList;

    private MyDB db;

    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Bitmap characterBitmap;
    private Bitmap backgroundBitmap;
    private double scaledFactor = 1.0;
    private Rect source, destination;

    private double dt;
    private int fps, scaledCharacterWidth, enemyAttackMS, enemyAttackLast=0;

    private int realResolution = 0;
    private final int virtualResolution = 48;
    private int characterBitmapFrameWidth;
    private int characterBitmapFrameHeight;

    private String username,table;
    private int currentHealth, xpEarned=0;

    public GameplayView(Context context) {
        super(context);
        initGameplay();
    }
    public GameplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameplay();
    }
    public GameplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameplay();
    }

    private void initGameplay() {
        setFocusable(true);

        mPaint = new Paint();
        source = new Rect(0,0,16,16);
        destination = new Rect(0,0,1,1);

        /// Load Character Spritesheet
        Resources r = this.getContext().getResources();
        Drawable d = r.getDrawable(R.drawable.characters);

        Bitmap bitmap = Bitmap.createBitmap(128, 144, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, 128, 144);
        d.draw(canvas);

        characterBitmap = bitmap;
        characterBitmapFrameWidth = characterBitmap.getWidth()/8;
        characterBitmapFrameHeight = characterBitmap.getHeight()/8;

        /// Load Background
        backgroundBitmap = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        d = r.getDrawable(R.drawable.combat_background);
        d.setBounds(0,0,32,32);
        d.draw(new Canvas(backgroundBitmap));
    }

    public void setupBattle(double seed, String username, String table) {
        Random rando = new Random((long)(seed*100));

        int enemyCount = (rando.nextInt() % 3)+1;
        int powerLevel = rando.nextInt() % 200;
        powerLevel = powerLevel * powerLevel;
        powerLevel = powerLevel / 400;
        xpEarned=0;

        /// Setup Enemy List
        enemyList = new LinkedList<Enemy>();
        for(int i = 0; i < enemyCount; i++) {
            enemyList.add(new Enemy(rando.nextInt(48), ((rando.nextInt() % 20) > 15), powerLevel));
        }
        enemyAttackMS = (int)System.currentTimeMillis()+3000;
        enemyAttackLast = 0;

        //Need to set up player stats
        db = new MyDB(this.getContext());
        this.username = username;
        this.table = table;
        CurrentPlayerData.getInstance().loadData(db,username,table);
        currentHealth = CurrentPlayerData.getInstance().getHealth();

        /// Update Player Status
        final String status = "Level: " + CurrentPlayerData.getInstance().getLevel() +
                "\nXP: " + CurrentPlayerData.getInstance().getXp() +
                "\nHealth: " + currentHealth +
                "\nPhysical: " + CurrentPlayerData.getInstance().getPhysical() +
                "\nMagical: " + CurrentPlayerData.getInstance().getMagical();
        gameplayStat.setText(status);

    }

    public void addButtonListeners(GameplayActivity activity, OnGameEndListener gameEnd) {
        gameplayLog = (EditText) activity.findViewById(R.id.editText_gameplayLog);
        gameplayStat = (EditText) activity.findViewById(R.id.editText_gameplayStats);

        this.gameEnd = gameEnd;

        ((Button) activity.findViewById(R.id.button_strength)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameplayLog.getText().append("\nPhysical Hit: " + CurrentPlayerData.getInstance().getPhysical() + "!");

                Enemy e = enemyList.get(0);
                e.damageByStrength(CurrentPlayerData.getInstance().getPhysical());

                updateCombat();
            }
        });

        ((Button) activity.findViewById(R.id.button_magic)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameplayLog.getText().append("\nMagical Hit: " + CurrentPlayerData.getInstance().getMagical() +  "!");

                for (Enemy e : enemyList) {
                    e.damageByMagic(CurrentPlayerData.getInstance().getMagical());
                }

                updateCombat();
            }
        });

        ((Button) activity.findViewById(R.id.button_block)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameplayLog.getText().append("\nYou Blocked!");
            }
        });

        mHolder = getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                resume();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pause();
            }
        });

    }

    protected void updateCombat() {
        Iterator<Enemy> iter = enemyList.iterator();

        while (iter.hasNext()) {
            Enemy e = iter.next();

            if (e.getHp() <= 0) {
                xpEarned += e.getLvl()*5;
                iter.remove();
                gameplayLog.getText().append("\n"+e.getName()+" died!");
            }
        }

        if(enemyList.size() == 0) {
            /// We won!
            CurrentPlayerData.getInstance().addXp(xpEarned);
            Toast.makeText(getContext(),"You gained "+xpEarned+" XP!",Toast.LENGTH_SHORT).show();
            gameEnd.onGameEnd(true);
        }

        if(currentHealth < 1) {
            /// Boo...
            gameEnd.onGameEnd(false);
        }
    }

    @Override
    public void run() {
        long lastMs = System.currentTimeMillis();
        long currentMs = System.currentTimeMillis();
        int frameCounter = 0;
        long lastFpsMs = currentMs;

        while (playing) {
            currentMs = System.currentTimeMillis();
            dt = (double) (currentMs - lastMs) / 1000.0;

            doUpdate(dt);
            doDraw();

            lastMs = currentMs;
            if (currentMs - lastFpsMs > 1000) {
                fps = frameCounter;
                frameCounter=0;
                lastFpsMs = System.currentTimeMillis();
            } else {
                frameCounter++;
            }
        }
    }

    public void doDraw() {
        if(!mHolder.getSurface().isValid())
            return;
        Canvas c = mHolder.lockCanvas();
        if(c == null)
            return;

        c.drawColor(Color.argb(255, 26, 128, 182));
        drawBackground(c);
        //c.drawRect( 0, 0, c.getWidth(), c.getHeight(), mPaint);

        mPaint.setTextSize(scaledCharacterWidth/4);

        //c.drawRect(0,0,scaledCharacterWidth,scaledCharacterWidth,mPaint);
//        c.drawText("FPS: "+fps, 0,32, mPaint);
//        c.drawText(""+getMeasuredWidth()+"x"+getMeasuredHeight(), 256, 32, mPaint);



        for (Enemy e : enemyList) {
            if (enemyList.size() == 2) {
                drawEnemy(c,e,12+(enemyList.indexOf(e)*16),virtualResolution-24+enemyList.indexOf(e));
            } else {
                switch (enemyList.indexOf(e)) {
                    case 1:
                        drawEnemy(c,e,8,virtualResolution-24+enemyList.indexOf(e));
                        break;
                    case 2:
                        drawEnemy(c,e,32,virtualResolution-24+enemyList.indexOf(e));
                        break;
                    default:
                        drawEnemy(c,e,20,virtualResolution-24+enemyList.indexOf(e));
                        break;
                }
            }
        }

        drawHealthBar(c,0,(int)(virtualResolution * 9.0/10.0),virtualResolution,(int)(virtualResolution/9.0),(float)(currentHealth*1.0f/CurrentPlayerData.getInstance().getHealth()));

        mHolder.unlockCanvasAndPost(c);
    }

    public void doUpdate(double delta) {
        // We don't need this? Maybe put enemy attacks here?
        if (enemyAttackMS < (int)System.currentTimeMillis() && enemyList.size() > 0) {
            enemyAttackMS = (int)System.currentTimeMillis() + (3000/enemyList.size());
            Enemy enemy = (Enemy) enemyList.toArray()[enemyAttackLast++];
            enemyAttackLast = enemyAttackLast % enemyList.size();
            currentHealth -= enemy.getDamage();
            ///gameplayLog.getText().append("\n"+enemy.getName()+" hit you: " + enemy.getDamage() + "!"); /// TODO: FIX THIS! We can't touch the UI thread from here!
            //Toast.makeText(getContext(),enemy.getName()+ " did "+enemy.getDamage()+" damage to you!",Toast.LENGTH_SHORT).show();

            Log.d("DEBUG",enemy.getName()+ " did "+enemy.getDamage()+" damage to you!");
            updateCombat();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h, oldw,oldh);
        realResolution = w;
        scaledFactor = w/virtualResolution;
        scaledCharacterWidth = (int) (8 * scaledFactor);
        //Toast.makeText(getContext(),"Canvas resized to: "+w+"x"+h,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width,width);
    }

    private void drawEnemy(Canvas c, Enemy e, int x, int y) {
        float progress = e.getHp();
        progress /= e.getMaxHp();
        drawHealthBar(c,x,y-4,8,2,progress);

        drawCharacter(c,x,y,e.getFrame());


        c.drawText(""+e.getHp()+"/"+e.getMaxHp(),(float) (x*scaledFactor),(float) ((y-4)*scaledFactor),mPaint);
        c.drawText("Lvl: "+e.getLvl(),(float) (x*scaledFactor),(float) ((y-6)*scaledFactor),mPaint);
    }

    private void drawCharacter(Canvas c, int x, int y, int frameId) {
        int frameX = 8*(frameId % characterBitmapFrameWidth);
        int frameY = 8*(frameId / characterBitmapFrameWidth);
        source.set(         frameX,frameY,
                            frameX+8,frameY+8);
        x*=scaledFactor;
        y*=scaledFactor;
        destination.set(    x,  y,
                            x+scaledCharacterWidth,  y+scaledCharacterWidth  );

        c.drawBitmap(characterBitmap, source, destination, mPaint);
    }

    private void drawHealthBar(Canvas c, int x, int y, int w, int h, float progress) {
        x*=scaledFactor;
        y*=scaledFactor;
        w*=scaledFactor;
        h*=scaledFactor;

        mPaint.setColor(Color.argb(255,255,0,0));
        c.drawRect(x,y,x+w,y+h,mPaint);
        mPaint.setColor(Color.argb(255,0,255,0));
        c.drawRect(x,y,x+(w*progress),y+h,mPaint);
        mPaint.setColor(Color.argb(255,255,255,255));
    }

    private void drawBackground(Canvas c) {
        source.set(0,0,32,32);
        destination.set( 0,0, realResolution, realResolution );

        c.drawBitmap(backgroundBitmap, source, destination, mPaint);
    }

    public void resume() {
        playing = true;
        gameplayThread = new Thread(this);
        gameplayThread.start();

        //Toast.makeText(getContext(), "Resume "+System.currentTimeMillis(),Toast.LENGTH_SHORT).show();
    }

    public void pause() {
        playing = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            Log.e("RPIGini Thread Error:", e.toString());
        }
        //Toast.makeText(getContext(), "Pause "+System.currentTimeMillis(),Toast.LENGTH_SHORT).show();
    }
}
