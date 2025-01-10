package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.Main;

/**
 * A basic menu that opens on escape
 *
 * @author lukas
 * @version 1.0
 */
public class EscapeMenu extends Dialog {

    public EscapeMenu(Skin skin) {
        super("Options",skin);

        setModal(true);
        setMovable(false);
        setResizable(false);

        button("Continue","continue");
        getButtonTable().row();
        button("Abandon match!","abandon");
        key(Input.Keys.ESCAPE, "continue");
    }

    @Override
    public void result(Object obj) {
        if (obj.equals("abandon")) {
            Main.getScreenManager().navigateTo(Screens.MainMenu);
        } else if (obj.equals("continue")) {
            this.hide();
        }
    }
}
