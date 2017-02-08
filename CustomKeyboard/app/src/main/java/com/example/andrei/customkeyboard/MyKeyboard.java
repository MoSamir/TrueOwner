package com.example.andrei.customkeyboard;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.Calendar;

public class MyKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;
    static double startTime;
    static double endTime;
    static long charactersText = 0 ;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        endTime = Calendar.getInstance().getTimeInMillis();
        Double inputDauration = (endTime - startTime) / 1000 ;
        Double averageTypeSpeed = (charactersText / inputDauration) ;
        String averageString = Double.toString(averageTypeSpeed);
        averageString = String.format("%.2f",averageTypeSpeed);
        
        Log.e("TAG","Input Duration = " + String.valueOf( (endTime-startTime)/1000 ));
        Log.e("TAG","Characters written = " + String.valueOf( charactersText));
        Log.e("TAG","Average speed = " + averageString);




        charactersText = 0 ;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        startTime = Calendar.getInstance().getTimeInMillis();

    }


    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        charactersText++;
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }



}
