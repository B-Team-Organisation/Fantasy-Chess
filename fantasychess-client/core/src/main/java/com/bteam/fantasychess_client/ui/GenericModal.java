package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A modal dialog that displays a message with an "Ok" button and executes a callback when the button is pressed.
 * Extends the {@link Dialog} class from LibGDX.
 *
 * @author Marc
 */
public class GenericModal extends Dialog {
    /**
     * The callback to execute when the "Ok" button is pressed.
     */
    Runnable callback;

    /**
     * Creates a new {@code GenericModal} dialog.
     *
     * @param title    The title of the dialog.
     * @param content  The content/message to display in the dialog.
     * @param skin     The {@link Skin} to use for styling the dialog.
     * @param callback The {@link Runnable} to execute when the "Ok" button is pressed.
     */
    public GenericModal(String title, String content, Skin skin, Runnable callback) {
        super(title, skin);

        setModal(true);
        setMovable(false);
        setResizable(false);
        text(content);

        this.callback = callback;
        button("Ok", "ok");
    }

    /**
     * Creates and shows a {@code GenericModal} dialog on the specified stage.
     *
     * @param title    The title of the dialog.
     * @param content  The content/message to display in the dialog.
     * @param skin     The {@link Skin} to use for styling the dialog.
     * @param callback The {@link Runnable} to execute when the "Ok" button is pressed.
     * @param stage    The {@link Stage} on which to display the dialog.
     */
    public static void Build(String title, String content, Skin skin, Runnable callback, Stage stage) {
        new GenericModal(title, content, skin, callback).show(stage);
    }

    /**
     * Called when a button in the dialog is clicked.
     * If the "Ok" button is clicked, the associated callback is executed,
     * and the dialog is hidden.
     *
     * @param obj The object associated with the clicked button (e.g., "ok").
     */
    @Override
    public void result(Object obj) {
        if (obj.equals("ok")) {
            callback.run();
            this.hide();
        }
    }
}
