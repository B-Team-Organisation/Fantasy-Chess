package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Usefull methods for ui building
 *
 * @author lukas
 */
public class UserInterfaceUtil {

    /**
     * Private constructor to hide it
     */
    private UserInterfaceUtil(){}

    /**
     * Makes it easy to attach a ChangeListener
     *
     * @param actor the {@link Actor} that should trigger the runnable
     * @param runnable the {@link Runnable} given to the actor
     */
    public static void onChange(Actor actor, Runnable runnable){
        actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
    }
}
