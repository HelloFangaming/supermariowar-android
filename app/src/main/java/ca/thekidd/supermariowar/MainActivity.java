package ca.thekidd.supermariowar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.andretietz.android.controller.ActionView;
import com.andretietz.android.controller.DirectionView;
import com.andretietz.android.controller.InputView;
import com.andretietz.android.controller.StartSelectView;

import org.libsdl.app.SDLActivity;

public class MainActivity extends SDLActivity implements InputView.InputEventListener {

    public static native int updateJoysticks();
    private View controllerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Old controller
        /*
        getLayoutInflater().inflate(R.layout.controller, mLayout, true);
        controllerView = mLayout.findViewById(R.id.controller);

        ActionView actionView = findViewById(R.id.viewAction);
        actionView.setOnButtonListener(this);
        InputView inputView = findViewById(R.id.viewDirection);
        inputView.setOnButtonListener(this);
        StartSelectView startSelectView = findViewById(R.id.viewStartSelect);
        startSelectView.setOnButtonListener(this);
        */

        //Improved controller
        getLayoutInflater().inflate(R.layout.controller_improved, mLayout, true);
        controllerView = mLayout.findViewById(R.id.controller_improved);

        //D Pad
        InputView inputView = findViewById(R.id.viewDpad);
        inputView.setOnButtonListener(this);

        //Quit Button
        findViewById(R.id.buttonQuit).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        keyEvent(BUTTON_SELECT, true);
                        ((ImageButton)v).setImageResource(R.drawable.action_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        keyEvent(BUTTON_SELECT, false);
                        ((ImageButton)v).setImageResource(R.drawable.action_usual);
                        break;
                }
                return false;
            }
        });

        //Start Button
        findViewById(R.id.buttonStart).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        keyEvent(BUTTON_START, true);
                        ((ImageButton)v).setImageResource(R.drawable.action_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        keyEvent(BUTTON_START, false);
                        ((ImageButton)v).setImageResource(R.drawable.action_usual);
                        break;
                }
                return false;
            }
        });

        //Jump Button
        findViewById(R.id.buttonJump).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        keyEvent(BUTTON_RIGHT, true);
                        ((ImageButton)v).setImageResource(R.drawable.action_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        keyEvent(BUTTON_RIGHT, false);
                        ((ImageButton)v).setImageResource(R.drawable.action_usual);
                        break;
                }
                return false;
            }
        });

        //Run Button (Tap one to run, tap again to stop running)
        findViewById(R.id.buttonRun).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        runToggle = !runToggle;
                        // PRESSED
                        if (runToggle) {
                            keyEvent(BUTTON_LEFT, true);
                            ((ImageButton) v).setImageResource(R.drawable.action_pressed);
                        }
                        // RELEASED
                        else
                        {
                            keyEvent(BUTTON_LEFT, false);
                            ((ImageButton)v).setImageResource(R.drawable.action_usual);
                        }
                        break;
                }
                return false;
            }
        });

        //Inventory Button
        findViewById(R.id.buttonInventory).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        keyEvent(BUTTON_UP, true);
                        ((ImageButton)v).setImageResource(R.drawable.action_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        keyEvent(BUTTON_UP, false);
                        ((ImageButton)v).setImageResource(R.drawable.action_usual);
                        break;
                }
                return false;
            }
        });

        mLayout.removeView(controllerView);

        if(getPackageManager().hasSystemFeature("android.hardware.touchscreen"))
            Toast.makeText(this, "Tap the screen to display the on-screen controller.", Toast.LENGTH_LONG).show();
    }

    boolean runToggle = false;

    //long lastTouch = -1;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //if(System.currentTimeMillis() <= lastTouch + 500)
            //return super.dispatchTouchEvent(ev);

        //lastTouch = System.currentTimeMillis();
        //if (ev.getY() < getWindowManager().getDefaultDisplay().getHeight() / 2) {
            //if(controllerView != null) {
                //if(controllerView.getParent() != null)
                    //mLayout.removeView(controllerView);
                //else
                if(controllerView.getParent() == null) {
                    mLayout.addView(controllerView);
            }
        //}

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("Main", "Key down: " + event.getDeviceId() + " " + event.getKeyCode());
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
            keyCode = KeyEvent.KEYCODE_BUTTON_START;
        if(keyCode == KeyEvent.KEYCODE_BACK)
            keyCode = KeyEvent.KEYCODE_BUTTON_SELECT;
        if (SDLActivity.isDeviceSDLJoystick(event.getDeviceId())) {
            // Note that we process events with specific key codes here
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                SDLActivity.onNativePadDown(event.getDeviceId(), keyCode);
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                SDLActivity.onNativePadUp(event.getDeviceId(), keyCode);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected String[] getLibraries() {
        return new String[]{
                "SDL2",
                "SDL2_image",
                "SDL2_mixer",
                "enet",
                "yaml-cpp",
                "main"
        };
    }

    private final int[] smwControls = {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_BUTTON_X, KeyEvent.KEYCODE_BUTTON_B, KeyEvent.KEYCODE_BUTTON_Y, KeyEvent.KEYCODE_BUTTON_A, KeyEvent.KEYCODE_BUTTON_START, KeyEvent.KEYCODE_BUTTON_SELECT
    };

    private boolean keyDown[] = new boolean[10];
    int LEFT = 0;
    int RIGHT = 1;
    int UP = 2;
    int DOWN = 3;
    int BUTTON_LEFT = 4;
    int BUTTON_RIGHT = 5;
    int BUTTON_UP = 6;
    int BUTTON_DOWN = 7;
    int BUTTON_START = 8;
    int BUTTON_SELECT = 9;

    @Override
    public void onInputEvent(View view, int buttons) {
        //D Pad
        keyEvent(LEFT, ((buttons&0xff)== DirectionView.DIRECTION_LEFT)||((buttons&0xff)== DirectionView.DIRECTION_DOWN_LEFT)||((buttons&0xff)== DirectionView.DIRECTION_UP_LEFT));
        keyEvent(RIGHT, ((buttons&0xff)== DirectionView.DIRECTION_RIGHT)||((buttons&0xff)== DirectionView.DIRECTION_DOWN_RIGHT)||((buttons&0xff)== DirectionView.DIRECTION_UP_RIGHT));
        keyEvent(UP, ((buttons&0xff)== DirectionView.DIRECTION_UP)||((buttons&0xff)== DirectionView.DIRECTION_UP_LEFT)||((buttons&0xff)== DirectionView.DIRECTION_UP_RIGHT));
        keyEvent(DOWN, ((buttons&0xff)== DirectionView.DIRECTION_DOWN)||((buttons&0xff)== DirectionView.DIRECTION_DOWN_RIGHT)||((buttons&0xff)== DirectionView.DIRECTION_DOWN_LEFT));
        /* Old Controller
        switch(view.getId()) {
         /*
            case R.id.viewDirection:
                keyEvent(LEFT, (16 & buttons) > 0);
                keyEvent(RIGHT, (1 & buttons) > 0);
                keyEvent(UP, (64 & buttons) > 0);
                keyEvent(DOWN, (4 & buttons) > 0);
                break;
            case R.id.viewAction:
                keyEvent(BUTTON_LEFT, (4 & buttons) > 0);
                keyEvent(BUTTON_RIGHT, (1 & buttons) > 0);
                keyEvent(BUTTON_UP, (8 & buttons) > 0);
                keyEvent(BUTTON_DOWN, (2 & buttons) > 0);
                break;
            case R.id.viewStartSelect:
                keyEvent(BUTTON_START, (1 & buttons) > 0);
                keyEvent(BUTTON_SELECT, (2 & buttons) > 0);
                break;
        }
        */
    }

    private void keyEvent(int key, boolean pressed) {
        boolean prevState = keyDown[key];
        if(pressed != prevState) {
            keyDown[key] = pressed;
            if(pressed) {
                onNativePadDown(-2, smwControls[key]);
            } else {
                onNativePadUp(-2, smwControls[key]);
            }
        }
    }
}
