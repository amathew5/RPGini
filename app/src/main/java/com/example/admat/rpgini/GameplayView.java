package com.example.admat.rpgini;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by galax_000 on 11/20/2016.
 */
public class GameplayView extends View {


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
        Resources r = this.getContext().getResources();
    }
    //
}
