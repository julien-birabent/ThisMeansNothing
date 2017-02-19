package com.julienbirabent.thismeansnothing.controller;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

/**
 * Created by Julien on 2017-02-18.
 */

public class TrackController extends MediaController {


    public TrackController(Context context) {
        super(context);
    }

    @Override
    public void hide() {

    }

    // Pour pouvoir revenir en arrière quand le controller est entrain de jouer
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                ((Activity) getContext()).finish();
            }
            return super.dispatchKeyEvent(event);
        }
}
