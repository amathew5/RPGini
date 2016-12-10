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
import android.widget.Toast;

/**
 * Created by galax_000 on 11/20/2016.
 */
public class GameplayView extends SurfaceView implements Runnable {

    Thread gameplayThread = null;
    volatile boolean playing;

    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Bitmap characterBitmap;
    private double scaledFactor = 1.0;
    private Rect source, destination;

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

        mHolder = getHolder();
        mPaint = new Paint();

        source = new Rect(0,0,16,16);
        destination = new Rect(128,128,256,256);

        Resources r = this.getContext().getResources();
        Drawable d = r.getDrawable(R.drawable.characters);

        Bitmap bitmap = Bitmap.createBitmap(128, 144, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, 128, 144);
        d.draw(canvas);

        characterBitmap = bitmap;
    }

    @Override
    public void run() {
        long lastMs = System.currentTimeMillis();
        long currentMs = System.currentTimeMillis();
        double dt = 0.0;

        while (playing) {
            currentMs = System.currentTimeMillis();
            dt = (double) (currentMs - lastMs) / 1000.0;
            doUpdate(dt);
            doDraw();

            lastMs = currentMs;
        }
    }

    public void doDraw() {
        if(!mHolder.getSurface().isValid())
            return;
        Canvas c = mHolder.lockCanvas();

        c.drawColor(Color.argb(255, 26, 128, 182));
        //c.drawRect( 0, 0, c.getWidth(), c.getHeight(), mPaint);

        mPaint.setTextSize(24);
        c.drawText("TEST TEXT", 0,0, mPaint);
        c.drawText("TEST TEXT", 0,32, mPaint);

        drawCharacter(c,128,128,6);
        drawCharacter(c,128,512,12);
        drawCharacter(c,128,768,18);

        mHolder.unlockCanvasAndPost(c);
    }

    public void doUpdate(double delta) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h, oldw,oldh);
        Toast.makeText(getContext(),"Canvas resized to: "+w+"x"+h,Toast.LENGTH_LONG).show();
    }

    private void drawCharacter(Canvas c, int x, int y, int frameId) {
        final int characterBitmapFrameWidth = characterBitmap.getWidth()/8;
        final int characterBitmapFrameHeight = characterBitmap.getHeight()/8;
        int frameX = 8*(frameId % characterBitmapFrameWidth);
        int frameY = 8*(frameId / characterBitmapFrameWidth);
        source.set(frameX,frameY,frameX+8,frameY+8);
        destination.set(x,y,x+128,y+128);
        c.drawBitmap(characterBitmap, source, destination, mPaint);
    }

    public void resume() {
        playing = true;
        gameplayThread = new Thread(this);
        gameplayThread.start();
    }

    public void pause() {
        playing = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            Log.e("RPIGini Thread Error:", e.toString());
        }
    }
}
