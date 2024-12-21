package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.fantasychess_client.Main;

/**
 * A class that manages the graphical user interface for displaying
 * player and opponent character statistics in the form of sidebars.
 * The Stats Overview for Character is displayed in form of a dialog.
 */
public class StatsOverview {

    private final Skin skin;
    private final Stage stage;
    private final Table player1Content;
    private final Table player2Content;

    /**
     * Constructor for creating a StatsOverview.
     *
     * @param skin  The {@link Skin} used for styling the UI components.
     * @param stage The {@link Stage} to which the sidebars and dialogs are added.
     */
    public StatsOverview(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;

        Table player1Shell = createSidebar("Your Characters");
        player1Content = new Table();
        ScrollPane player1ScrollPane = createScrollPane(player1Content);
        player1Shell.add(player1ScrollPane).expand().fill().pad(10).row();
        player1Shell.setSize(400, 420);
        player1Shell.setPosition(50, stage.getHeight() - 450);

        Table player2Shell = createSidebar("Opponent Characters");
        player2Content = new Table();
        ScrollPane player2ScrollPane = createScrollPane(player2Content);
        player2Shell.add(player2ScrollPane).expand().fill().pad(10).row();
        player2Shell.setSize(400, 400);
        player2Shell.setPosition(stage.getWidth() - 450, stage.getHeight() - 450);

        stage.addActor(player1Shell);
        stage.addActor(player2Shell);
    }

    /**
     * Updates the content of the sidebars with the latest character statistics.
     * <p>
     * Clears the current content and populates the sidebars with the characters
     * for Player 1 (friendly characters) and Player 2 (enemy characters).
     */
    public void updateSidebars() {
        player1Content.clearChildren();
        player2Content.clearChildren();

        for (CharacterEntity character : Main.getGameStateService().getFriendlyCharacters()) {
            player1Content.add(createCharacterStat(character)).padBottom(10).row();
        }

        for (CharacterEntity character : Main.getGameStateService().getEnemyCharacters()) {
            player2Content.add(createCharacterStat(character)).padBottom(10).row();
        }
    }

    /**
     * Creates a sidebar container with a rounded background and a title header.
     *
     * @param title The title text for the sidebar.
     * @return A {@link Table} representing the sidebar.
     */
    private Table createSidebar(String title) {
        Table sidebar = new Table();
        sidebar.setBackground(skin.getDrawable("round-gray"));

        Label header = new Label(title, skin, "default");
        header.setColor(Color.WHITE);
        header.setFontScale(1.5f);
        sidebar.add(header).padTop(10).padBottom(10).center().row();

        return sidebar;
    }

    /**
     * Creates a scrollable container for the sidebar content.
     *
     * @param content The {@link Table} containing the sidebar content.
     * @return A {@link ScrollPane} wrapping the provided content.
     */
    private ScrollPane createScrollPane(Table content) {
        ScrollPane scrollPane = new ScrollPane(content, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setStyle(new ScrollPane.ScrollPaneStyle() {{
            background = null;
            hScrollKnob = null;
            vScrollKnob = null;
        }});
        return scrollPane;
    }

    /**
     * Creates a table row displaying the statistics of a character.
     *
     * @param character The {@link CharacterEntity} to display.
     * @return A {@link Table} row with the characters name and health bar.
     */
    private Table createCharacterStat(CharacterEntity character) {
        Table row = new Table();
        row.setBackground(skin.newDrawable("white", Color.LIGHT_GRAY));

        Table nameContainer = new Table();
        nameContainer.setBackground(skin.newDrawable("white", Color.WHITE));
        Label nameLabel = new Label(character.getCharacterBaseModel().getName(), skin);
        nameLabel.setColor(Color.WHITE);
        nameLabel.setAlignment(Align.center);
        nameContainer.add(nameLabel).pad(5).expandX().fillX();

        nameLabel.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showCharacterStatsDialog(character);
            }
        });

        float healthPercentage = character.getHealth() / (float) character.getCharacterBaseModel().getHealth();
        Table healthBarContainer = new Table();
        ProgressBar healthBar = createHealthBar(healthPercentage);

        String healthText = character.getHealth() + " / " + character.getCharacterBaseModel().getHealth() + " HP";
        Label healthLabel = new Label(healthText, skin);
        healthLabel.setColor(Color.WHITE);

        healthBarContainer.add(healthLabel).expandX().center().padBottom(2).row();
        healthBarContainer.add(healthBar).width(200).height(15).padBottom(5).row();

        row.add(nameContainer).width(150).height(50).pad(5);
        row.add(healthBarContainer).width(200).height(50).pad(5);

        return row;
    }

    /**
     * Creates a progress bar to visually represent the health of a character.
     *
     * @param healthPercentage A value between 0 and 1 indicating the character's health percentage.
     * @return A {@link ProgressBar} styled to show the health percentage.
     */
    private ProgressBar createHealthBar(float healthPercentage) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = skin.newDrawable("white", Color.DARK_GRAY);
        style.background.setMinWidth(300);
        style.background.setMinHeight(15);
        style.knobBefore = skin.newDrawable("white", getHealthColor(healthPercentage));
        style.knobBefore.setMinWidth(300);
        style.knobBefore.setMinHeight(15);
        style.knob = null;

        ProgressBar healthBar = new ProgressBar(0, 1, 0.01f, false, style);
        healthBar.setValue(healthPercentage);
        healthBar.setSize(300, 15);
        healthBar.invalidate();

        return healthBar;
    }

    /**
     * Determines the color of the health bar based on the character's health percentage.
     *
     * @param healthPercentage A value between 0 and 1 indicating the character's health percentage.
     * @return A {@link Color} representing the health bar's color.
     */
    private Color getHealthColor(float healthPercentage) {
        if (healthPercentage > 0.7f) return Color.GREEN;
        if (healthPercentage > 0.4f) return Color.YELLOW;
        if (healthPercentage > 0.2f) return Color.ORANGE;
        return Color.RED;
    }

    /**
     * Displays a dialog showing detailed statistics of a character.
     *
     * @param character The {@link CharacterEntity} whose statistics are displayed.
     */
    public void showCharacterStatsDialog(CharacterEntity character) {
        Dialog statsDialog = new Dialog("Character Stats", skin);

        String statsText = "Name: " + character.getCharacterBaseModel().getName() + "\n" +
            "Health: " + character.getHealth() + " / " + character.getCharacterBaseModel().getHealth() + "\n" +
            "Attack Power: " + character.getCharacterBaseModel().getAttackPower() + "\n" +
            "Movement Pattern: " + character.getCharacterBaseModel().getMovementDescription() + "\n" +
            "Attack Pattern: " + character.getCharacterBaseModel().getAttackDescription();

        Label statsLabel = new Label(statsText, skin);
        statsLabel.setAlignment(Align.left);
        statsLabel.setFontScale(1.0f);
        statsDialog.getContentTable().add(statsLabel).pad(10).row();
        statsDialog.button("Close");
        statsDialog.show(stage);
    }
}
