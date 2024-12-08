package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.bteam.fantasychess_client.Main;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;



public class TurnTimer extends Label {

    private final TextButton readyButton;
    private float startTime;
    private float timeLeft;
    private Runnable EndOfTimer;



    TurnTimer(Skin skin, float startTime,TextButton readyButton) {
        super("", skin);
        this.startTime = startTime;
        this.timeLeft = startTime;
        this.readyButton = readyButton;
        this.EndOfTimer = ()-> this.sendMoves();
        updateLabel();
    }

    public void updateTime(float delta) {
        if (timeLeft > 0){

            timeLeft -= delta;

            if (timeLeft <= 10 && !hasActions()) {
                clearActions();
                addAction(Actions.forever(
                Actions.sequence(
                    Actions.run(() -> {
                            setFontScale(5f);
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
                }
            }
        updateLabel();
        }
    }


    public void setTime(float newTime) {
        timeLeft = newTime;
        updateLabel();
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

    //fürs erste
    public void reset() {
        timeLeft = startTime;
        updateLabel();
    }
    public void stopTime(){
        getStage().getActors().removeValue(this, true);
    }

    public void sendMoves(){
        Main.getCommandManagementService().sendCommandsToServer();
        //probiere mal !!!!!

        if( readyButton != null && !readyButton.isChecked()){
            readyButton.setDisabled(true);
            readyButton.setText("READY");
        }
    }


}
