package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.bteam.common.entities.CharacterEntity;


/**
 * Stats Dialog for the Character Information
 *
 * @author albano
 */
public class CharacterStatsDialog extends Dialog {

     private Skin skin;
     private CharacterEntity character;

    /**
     * Constructor for the StatsDialog
     *
     * @param skin The UI skin used for styling the dialog.
     * @param character Reference to the actual Character selected
     */
     public CharacterStatsDialog(Skin skin, CharacterEntity character) {
         super("Character Stats", skin);
         this.skin = skin;

         String statsText = "Name: " + character.getCharacterBaseModel().getName() + "\n" +
             "Health: " + character.getHealth() + " / " + character.getCharacterBaseModel().getHealth() + "\n" +
             "Attack Power: " + character.getCharacterBaseModel().getAttackPower() + "\n" +
             "Movement Pattern: " + character.getCharacterBaseModel().getMovementDescription() + "\n" +
             "Attack Pattern: " + character.getCharacterBaseModel().getAttackDescription();

         Label statsLabel = new Label(statsText, skin);
         statsLabel.setAlignment(Align.left);
         statsLabel.setFontScale(1.0f);
         getContentTable().add(statsLabel).pad(10).row();
         button("Close");
    }


}
