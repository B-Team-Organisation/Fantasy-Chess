package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerStatusDTO;
import com.bteam.fantasychess_client.Main;

import static com.bteam.common.constants.PacketConstants.PLAYER_ABANDONED;

/**
 * A basic menu that opens on escape
 *
 * @author lukas
 * @version 1.0
 */
public class EscapeMenu extends Dialog {

    public EscapeMenu(Skin skin) {
        super("Options", skin);

        setModal(true);
        setMovable(false);
        setResizable(false);

        button("Continue", "continue");
        getButtonTable().row();
        button("Abandon match!", "abandon");
        key(Input.Keys.ESCAPE, "continue");
    }

    @Override
    public void result(Object obj) {
        if (obj.equals("abandon")) {
            Gdx.app.postRunnable(this::sendAbandonPacket);
            Main.getScreenManager().navigateTo(Screens.MainMenu);
        } else if (obj.equals("continue")) {
            this.hide();
        }
    }

    private void sendAbandonPacket() {
        var dto = PlayerStatusDTO.abandoned(Main.getWebSocketService().getUserid());
        var packet = new Packet(dto, PLAYER_ABANDONED);
        Main.getWebSocketService().send(packet);
    }
}
