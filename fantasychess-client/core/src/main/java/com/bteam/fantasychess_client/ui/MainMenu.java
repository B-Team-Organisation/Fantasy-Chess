package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.List;
import java.util.ArrayList;


import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

/** First screen of the application. Displayed after the application is created. */
public class MainMenu extends ScreenAdapter {

    private final OrthographicCamera camera;


    private final ExtendViewport extendViewport;
    private User user1;
    private User user2;
    private Stage stage;
    private Skin skin;

    // just for testing , waiting for adnan implementation
    public static class Lobby {

        String status ; // maybe not necessary
        String opponentName;
       // String lobbyname?


        public Lobby(String opponentName) {
            this.opponentName = opponentName;
            status= "open";
        }
    }


    public static class User {
        String username;

        public User(String username) {
            this.username = username;
        }
    }


    public MainMenu(Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }
    @Override

    public void show() {
         user1 = new User("Albano");
         user2 = new User("Lana");



        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);





        Table table = new Table();
        table.setFillParent(true);

        Label usernameLabel = new Label("Username", skin);
        usernameLabel.setFontScale(4f);
        usernameLabel.setAlignment(Align.left);

        Label titleLabel = new Label("FantasyChess", skin);
       // titleLabel.setColor(Color.WHITE);
        titleLabel.setFontScale(6f);

        Table topContent = new Table();
        topContent.setBackground(skin.getDrawable("round-dark-gray"));

        TextField lobbyNameInput = new TextField("Search lobby name", skin);
        lobbyNameInput.setColor(Color.LIGHT_GRAY);

        lobbyNameInput.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    if (lobbyNameInput.getText().equals("Search lobby name")) {
                        lobbyNameInput.setText("");
                        lobbyNameInput.setColor(Color.WHITE);
                    }
                } else {
                    if (lobbyNameInput.getText().isEmpty()) {
                        lobbyNameInput.setText("Search Lobby Name");
                        lobbyNameInput.setColor(Color.LIGHT_GRAY);
                    }
                }
            }
        });

        TextButton refreshButton = new TextButton("Refresh lobbies", skin);
        TextButton createLobby = new TextButton("Create Lobby", skin);

        createLobby.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobbyDialog();
            }
        });

        onChange(refreshButton, () -> {
            System.out.println("I want to refresh lobbies");
        });

        topContent.add(lobbyNameInput).growX().pad(10);
        topContent.add(refreshButton).pad(10);
        topContent.add(createLobby).pad(10);

        Table centerContent = new Table();
        centerContent.setBackground(skin.getDrawable("round-dark-gray"));
        centerContent.top().pad(0);

        List<Lobby> lobbiesList = showLobbies();
        for (Lobby lobby : lobbiesList) {
            Button lobbyMember = new Button(skin);
            lobbyMember.setBackground(skin.getDrawable("round-light-gray"));
            lobbyMember.pad(5);
            lobbyMember.left();

            lobbyMember.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Entering lobby " + lobby.opponentName);
                }
            });

            Label lobbyLabel = new Label("Opponent: " + lobby.opponentName + ", Status: " + lobby.status, skin);
            lobbyMember.add(lobbyLabel).expandX().left().padRight(10);

            centerContent.add(lobbyMember).growX().padBottom(10);
            centerContent.row();
        }
        table.add(usernameLabel).expandX().left().padTop(0);
        table.row();
        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(topContent).width(1000).padBottom(20);
        table.row();
        table.add(centerContent).width(1000).height(520).top();
        table.row();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void createLobbyDialog() {
        Dialog dialog = new Dialog("Create Lobby", skin) {
            @Override
            protected void result(Object object) {
                if ("create".equals(object)) {
                    System.out.println("Lobby created!");
                }
            }
        };

        Table contentTable = dialog.getContentTable();
        contentTable.defaults().pad(10);

        Label lobbyNameLabel = new Label("Lobby Name:", skin);
        lobbyNameLabel.setFontScale(1.5f);
        TextField lobbyNameField = new TextField(user1.username + "'s Lobby", skin);
        lobbyNameField.setMessageText("Enter lobby name");
        lobbyNameField.setSize(400, 50);

        contentTable.add(lobbyNameLabel).padBottom(20).colspan(2).center().row();
        contentTable.add(lobbyNameField).width(400).height(50).center().row();

        dialog.button("Create", "create").pad(20);
        dialog.button("Cancel", null).pad(20);

        dialog.key(Input.Keys.ENTER, "create");
        dialog.key(Input.Keys.ESCAPE, null);
        dialog.show(stage).setSize(600, 300);
        dialog.setPosition((stage.getWidth() - dialog.getWidth()) / 2, (stage.getHeight() - dialog.getHeight()) / 2);
    }


    private List<Lobby> showLobbies() {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(new Lobby("Ayakoji" ));
        lobbies.add(new Lobby("Hana"));
        lobbies.add(new Lobby("Luxort"));
        lobbies.add(new Lobby("Zhuxin"));
        return lobbies;
    }



   // private user1 = new User("Xene");
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        extendViewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        // Destroy screen's assets here.
    }


}
