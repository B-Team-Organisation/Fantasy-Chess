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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.List;
import java.util.ArrayList;


import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;




// every lobby in lower case for search handling
// distance algo 2


// Bug1: after pressing create and cancel to lobby, search panel doenst dissapear .
// --Fix1: no mistakes in the writing, just mid search
  //-> fixed, suggestions label, ( maybe link to input later?),

// Fix2: event handler -> Lukas
// Problem1: Refresh Button ?
//



/** First screen of the application. Displayed after the application is created. */
public class MainMenu extends ScreenAdapter {

    private final OrthographicCamera camera;


    private final ExtendViewport extendViewport;
    private User user1;
    private Stage stage;
    private Skin skin;
    private Table centerContent;
    private List<Lobby> allLobbies;
    private Label noLobbyFoundTable;
    private TextField lobbyNameInput;

    // just for testing , waiting for adnan implementation
    public static class Lobby {

        String status ; // maybe not necessary
        String lobbyName;
        // String lobbyid?


        public Lobby(String lobbyName) {
            this.lobbyName = lobbyName;
            status= "open";
        }
    }

// test
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

        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);



        Table table = new Table();
        table.setFillParent(true);

        Label usernameLabel = new Label(" "+ user1.username, skin);
        usernameLabel.setFontScale(4f);
        usernameLabel.setAlignment(Align.left);

        Label titleLabel = new Label("FantasyChess", skin);
        // titleLabel.setColor(Color.WHITE);
        titleLabel.setFontScale(6f);

        Table topContent = new Table();
        topContent.setBackground(skin.getDrawable("round-dark-gray"));

        noLobbyFoundTable = new Label("No results found", skin);
        noLobbyFoundTable.setVisible(false);

        lobbyNameInput = new TextField("Search lobby name", skin);
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

        onChange(createLobby, this::createLobbyDialog);// this instead of ()->



        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String searchText = lobbyNameInput.getText().toLowerCase();
                filterLobbies(searchText);
            }
        });

        topContent.add(lobbyNameInput).growX().pad(10);
        topContent.add(refreshButton).pad(10);
        topContent.add(createLobby).pad(10);

        centerContent = new Table();
        centerContent.setBackground(skin.getDrawable("round-dark-gray"));
        centerContent.top().pad(0);

        ScrollPane scrollPane = new ScrollPane(centerContent, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);



        table.add(usernameLabel).expandX().left().padTop(0);
        table.row();
        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(topContent).width(1000).padBottom(20);
        table.row();
        table.add(scrollPane).width(1000).height(520).top();
        table.row();
        table.add(noLobbyFoundTable).padTop(10);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        allLobbies = showLobbies();
        loadLobbies(allLobbies);

        //mal schauen

        lobbyNameInput.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String searchText = lobbyNameInput.getText().toLowerCase();
                filterLobbies(searchText);
            }
        });


    }

    /**
     * Opens a dialog for creating a new lobby. The dialog has a text field for the User Lobby Name ,set
     * to "{username}´s Lobby" by default. The dialog has two Buttons, the "Cance"-Button closes the dialog,
     * the "Create"-Button is only active if your inout is not empty, and will direct you to the game screen
     * in your own lobby if clicked.
     */
    private void createLobbyDialog() {
        Dialog dialog = new Dialog("Create New Lobby", skin) {
            @Override
            protected void result(Object object) {
                if ("create".equals(object)) {
                    System.out.println("Lobby created!");

                       goToGameScreen();

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

        TextButton createButton = new TextButton("Create", skin);
        createButton.setDisabled(false);

        // to change @lukas
        lobbyNameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isFieldEmpty = lobbyNameField.getText().isEmpty();

                createButton.setDisabled(isFieldEmpty);// -> maybe min number of letters?


                if (isFieldEmpty) {
                    createButton.setDisabled(true);
                    createButton.setColor(Color.DARK_GRAY); // darj grey for disabled
                } else {
                    createButton.setColor(Color.WHITE); // normal color
                }
            }
        });


        dialog.button(createButton, "create").pad(18);
        dialog.button("Cancel", null).pad(18);

        dialog.key(Input.Keys.ENTER, "create");
        dialog.key(Input.Keys.ESCAPE, null);

        dialog.show(stage).setSize(600, 300);
        dialog.setPosition((stage.getWidth() - dialog.getWidth()) / 2, (stage.getHeight() - dialog.getHeight()) / 2);
    }


    /**
     * Function to be directed to GameScreen
     */
    private void goToGameScreen() {
        Gdx.app.postRunnable(() -> {
            ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(skin));
        });
    }

    /**
     * filters the lobbies with the input live
     * @param searchText input
     */
    private void filterLobbies(String searchText) {
        var filteredLobbies = new ArrayList<Lobby>();

        for (Lobby lobby : allLobbies) {
            if (lobby.lobbyName.toLowerCase().contains(searchText)) {
                filteredLobbies.add(lobby);
            }
        }

        if (filteredLobbies.isEmpty()) {
            var suggestions = getSuggestions(searchText);

            if (!suggestions.isEmpty()) {
                noLobbyFoundTable.setText("No lobby found. Did you mean: " + String.join(", ", suggestions) + "?");
             // link ? maybe after mvp

            } else {
                noLobbyFoundTable.setText("No lobby found");
            }
            noLobbyFoundTable.setVisible(true);
        } else {
            noLobbyFoundTable.setVisible(false);
        }

        loadLobbies(filteredLobbies);
    }

    private void loadLobbies(List<Lobby> lobbies) {
        centerContent.clearChildren();

        if (lobbies.isEmpty()) {
            centerContent.add(noLobbyFoundTable).expandX().center().padTop(10);
        } else {
            for (Lobby lobby : lobbies) {
                Button lobbyMember = new Button(skin);
                lobbyMember.setBackground(skin.getDrawable("round-light-gray"));
                lobbyMember.pad(5);
                lobbyMember.left();

                lobbyMember.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Creating lobby " + lobby.lobbyName);
                    }
                });

                Label lobbyLabel = new Label("Lobby name: " + lobby.lobbyName + ", Status: " + lobby.status, skin);
                lobbyMember.add(lobbyLabel).expandX().left().padRight(10);

                centerContent.add(lobbyMember).growX().padBottom(10);
                centerContent.row();
            }
        }
    }

    private List<String> getSuggestions(String searchText) {
        var suggestions = new ArrayList<String>();

        for (Lobby lobby : allLobbies) {
            int distance = levenshteinDistance(searchText, lobby.lobbyName.toLowerCase());
            if (distance <= 2) { // distance =2 , can be increased with few lobbies, lowered with many
                suggestions.add(lobby.lobbyName);
            }
        }
        return suggestions;
    }
    // 1 per 1
    private int levenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1))
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }


    // testdata,to check with online data
    private List<Lobby> showLobbies() {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(new Lobby("Ayakoji"));
        lobbies.add(new Lobby("Hana"));
        lobbies.add(new Lobby("Luxort"));
        lobbies.add(new Lobby("Zhuxin"));
        lobbies.add(new Lobby("Beras"));
        lobbies.add(new Lobby("Ino"));
        lobbies.add(new Lobby("Kalavoi"));
        lobbies.add(new Lobby("Albert"));
        lobbies.add(new Lobby("Sius"));
        lobbies.add(new Lobby("Demonzone"));
        lobbies.add(new Lobby("Angels"));
        lobbies.add(new Lobby("Classrom of"));
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

