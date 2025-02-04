package com.bteam.fantasychess_client.ui;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bteam.fantasychess_client.Main;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TurnTimer extends Label {


    private final TextButton readyButton;
    private float timeLeft;
    private Runnable EndOfTimer;
    private boolean isRunning = false;


    public TurnTimer(Skin skin, float timeLeft,TextButton readyButton) {
        super("", skin);
        this.timeLeft = timeLeft;
        this.readyButton = readyButton;
        this.EndOfTimer = null;
        updateLabel();
    }


    public void updateTime(float delta) {
        if (!isRunning) return;

        if (timeLeft > 0){

            timeLeft -= delta;

            if (timeLeft <= 10 && !hasActions()) {
                clearActions();
                addAction(Actions.forever(
                    Actions.sequence(
                        Actions.run(() -> {
                            setFontScale(4f);
                            setSize(400, 200);
                            setPosition((getStage().getWidth() ) / 2, getStage().getHeight()-150 , Align.center);
                        }),
                        Actions.delay(0.5f),
                        Actions.run(() -> {
                            setFontScale(4f);
                            setAlignment(Align.center);
                            setSize(200,100);
                            setPosition((getStage().getWidth() - getWidth()) / 2, getStage().getHeight()-100);
                        }),
                        Actions.delay(0.5f)
                    )
                ));
                getStyle().fontColor = Color.RED;
            }
            if (timeLeft <= 0){
                timeLeft = 0;
                clearActions();
                setFontScale(4f);
                setSize(200, 100);
                setPosition((getStage().getWidth() - getWidth()) / 2, getStage().getHeight()-100);
                getStyle().fontColor = Color.WHITE;
                stopTime();


                if (EndOfTimer != null) {
                    EndOfTimer.run();
                } else if (!readyButton.isDisabled()) {
                    readyButton.toggle(); // button automatisch drücken
                }
                reset(30);
            }
            updateLabel();
        }
    }

    public void start(){
        isRunning = true;
    }

    public void stop(){
        isRunning = false;
    }

    public void setEndOfTimer(Runnable endOfTimer) {
        this.EndOfTimer = endOfTimer;
    }


    public void updateLabel() {
        setText(formatTime(timeLeft));
    }

    private String formatTime(float time) {
        int minutes = (int)(time / 60);
        int seconds = (int)(time % 60);
        String second ="";
        if(seconds <10) {
            second = "0" + seconds;
        }
        else {
            second = seconds + "";
        }
        String minute ="";
        if(minutes <10) {
            minute = "0" + minutes;
        }
        else {
            minute = minutes + "";
        }
        return minute + ":" + second;
    }

    public void reset(float defaultTime) {
        timeLeft = defaultTime;
        clearActions();
        getStyle().fontColor = Color.WHITE;
        setFontScale(4f);
        updateLabel();
        stop();
    }

    public void stopTime(){
        reset(30);
    }
}
