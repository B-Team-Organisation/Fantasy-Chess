package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.ui.GameScreen;

import java.util.List;

/**
 * A custom table for displaying character stats in a sidebar or dialog.
 *
 * @author albano
 */
public class CharacterStatsTable extends Table {

    private Skin skin;
    private List<CharacterEntity> characters;
    private String title;
    private GameScreen gameScreen;
    private ScrollPane scrollPane;
    private Table contentTable;

    /**
     * Constructor for creating a StatsOverview.
     *
     * @param title The title of the stats table ("Your Characters" or "Opponent Characters").
     * @param skin  The {@link Skin} used for styling the UI components.
     */
    public CharacterStatsTable(String title, Skin skin, GameScreen gameScreen) {
        this.skin = skin;
        this.title = title;
        this.gameScreen = gameScreen;

        setBackground(skin.getDrawable("round-gray"));

        contentTable = new Table();

        scrollPane = createScrollPane(contentTable);

        Label header = new Label(title, skin, "default");
        header.setColor(Color.WHITE);
        header.setFontScale(1.5f);
        header.setAlignment(Align.center);

        add(header).padTop(10).padBottom(10).center().row();
        add(scrollPane).expand().fill().pad(10);
    }

    /**
     * Updates the content of the table with the provided characters.
     *
     * @param characters The list of {@link CharacterEntity} to display in the table.
     */
    public void updateContent(List<CharacterEntity> characters, String title) {
        this.characters = characters;
        this.title = title;
        refreshTable();
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
        scrollPane.setFadeScrollBars(false);

        return scrollPane;
    }



    /**
     * Refreshes the table layout based on the current data.
     */
    private void refreshTable() {
        contentTable.clearChildren();

        if (characters.isEmpty()) {
            Label noCharactersLabel = new Label("No characters available", skin);
            noCharactersLabel.setColor(Color.LIGHT_GRAY);
            noCharactersLabel.setAlignment(Align.center);
            contentTable.add(noCharactersLabel).padTop(10).padBottom(10).center().row();
        } else {
            for (CharacterEntity character : characters) {
                contentTable.add(createCharacterStat(character)).padBottom(10).row();
            }
        }
    }

    /**
     * Override the act method to automatically refresh the table.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (characters != null && !characters.isEmpty()) {
            refreshTable();
        }
            if (title.equals("Your Characters")) {
            updateContent(Main.getGameStateService().getFriendlyCharacters(), title);
        } else if (title.equals("Opponent's Characters")) {
            updateContent(Main.getGameStateService().getEnemyCharacters(), title);
        }
    }

    /**
     * Creates a table row displaying the statistics of a character.
     *
     * @param character The {@link CharacterEntity} to display.
     * @return A {@link Table} row with the character's name and health bar.
     */
    private Table createCharacterStat(CharacterEntity character) {
        Table characterEntry = new Table();
        characterEntry.setBackground(skin.newDrawable("white", Color.LIGHT_GRAY));

        characterEntry.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                gameScreen.updateSelectedCharacter(character);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                gameScreen.resetSelection();
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                 CharacterStatsDialog statsDialog = new CharacterStatsDialog(skin,character);
                 statsDialog.show(gameScreen.getStage());
            }
        });

        Table nameContainer = new Table();
        nameContainer.setBackground(skin.newDrawable("white", Color.WHITE));
        Label nameLabel = new Label(character.getCharacterBaseModel().getName(), skin);
        nameLabel.setColor(Color.WHITE);
        nameLabel.setAlignment(Align.center);
        nameContainer.add(nameLabel).pad(5).expandX().fillX();

        float healthPercentage = character.getHealth() / (float) character.getCharacterBaseModel().getHealth();
        Table healthBarContainer = new Table();
        ProgressBar healthBar = createHealthBar(healthPercentage);

        String healthText = character.getHealth() + " / " + character.getCharacterBaseModel().getHealth() + " HP";
        Label healthLabel = new Label(healthText, skin);
        healthLabel.setColor(Color.WHITE);

        healthBarContainer.add(healthLabel).expandX().center().padBottom(2).row();
        healthBarContainer.add(healthBar).width(200).height(15).padLeft(50).padRight(50).padBottom(5).row();

        characterEntry.add(nameContainer).width(150).height(50).pad(5);
        characterEntry.add(healthBarContainer).width(200).height(50).pad(5);

        return characterEntry;
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

}
