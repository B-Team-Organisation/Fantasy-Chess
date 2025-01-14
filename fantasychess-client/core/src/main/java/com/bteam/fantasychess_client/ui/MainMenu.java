package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.dto.CreateLobbyDTO;
import com.bteam.common.dto.JoinLobbyDTO;
import com.bteam.common.dto.JoinLobbyResultDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.models.LobbyModel;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.data.mapper.LobbyMapper;
import com.bteam.fantasychess_client.data.mapper.PlayerInfoMapper;

import java.util.List;
import java.util.*;
import java.util.logging.Level;

import static com.bteam.common.constants.PacketConstants.*;
import static com.bteam.fantasychess_client.Main.*;
import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainMenu extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;
    private final Skin skin;
    private final String defaultSearchString = "Search Lobby name!";
    private Stage stage;
    private Table centerContent;
    private List<LobbyModel> allLobbies = new ArrayList<>();
    private Label noMatchingLobbyLabel;
    private TextField lobbyNameInput;
    private String username;

    public MainMenu(Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920, 1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        stage = new Stage(extendViewport);

        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = createTitleLabel();
        Label usernameLabel = createUserNameLabel();

        Table topContent = new Table();
        topContent.setBackground(skin.getDrawable("round-dark-gray"));

        noMatchingLobbyLabel = new Label("No matching lobby found!", skin);
        noMatchingLobbyLabel.setVisible(false);

        lobbyNameInput = new TextField(defaultSearchString, skin);
        addNameFilter(lobbyNameInput);
        lobbyNameInput.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    if (lobbyNameInput.getText().equals(defaultSearchString)) {
                        lobbyNameInput.setText("");
                    }
                } else {
                    if (lobbyNameInput.getText().isEmpty()) {
                        lobbyNameInput.setText(defaultSearchString);
                    }
                }
            }
        });
        lobbyNameInput.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.FORWARD_DEL || keycode == Input.Keys.DEL) {
                    lobbyNameInput.setText("");
                    filterLobbies("");
                    return true;
                }
                return false;
            }
        });
        onChange(lobbyNameInput, () -> {
            filterLobbies(lobbyNameInput.getText());
        });

        TextButton refreshButton = new TextButton("Refresh lobbies", skin);
        onChange(refreshButton, () -> {
            Gdx.app.postRunnable(() -> Main.getWebSocketService().send(new Packet(null, LOBBY_ALL)));
            stage.setKeyboardFocus(null);
        });
        TextButton createLobby = new TextButton("Create Lobby", skin);
        onChange(createLobby, this::createLobbyDialog);

        topContent.add(lobbyNameInput).growX().pad(10);
        topContent.add(refreshButton).pad(10);
        topContent.add(createLobby).pad(10);

        centerContent = new Table();
        centerContent.setBackground(skin.getDrawable("round-dark-gray"));
        centerContent.top().pad(0);

        ScrollPane scrollPane = new ScrollPane(centerContent, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);


        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(topContent).width(1000).padBottom(20);
        table.row();
        table.add(scrollPane).width(1000).height(520).top();
        table.row();
        table.add(usernameLabel).padTop(10);
        table.row();

        table.add(noMatchingLobbyLabel).padTop(10);

        getWebSocketService().addPacketHandler(LOBBY_INFO, this::onLobbyInfo);
        getWebSocketService().addPacketHandler(LOBBY_CREATED, this::onLobbyCreated);
        getWebSocketService().addPacketHandler(LOBBY_JOINED, this::onLobbyJoined);
        getWebSocketService().addPacketHandler(LOBBY_CLOSED, getLobbyService()::onLobbyClosed);
        getWebSocketService().addPacketHandler(PLAYER_JOINED, this::onPlayerJoined);

        Gdx.app.postRunnable(() -> Main.getWebSocketService().send(new Packet(null, LOBBY_ALL)));

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates the label that shows the game title
     *
     * @return the {@link Label}
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label("FantasyChess", skin, "title");
        titleLabel.setFontScale(0.6f);
        return titleLabel;
    }

    /**
     * Creates and opens a menu
     * <p>
     * Gives the player the possibility to exit a match and go back to the main menu
     */
    public void openEscapeMenu() {
        new EscapeMenu(skin).show(stage);
    }


    /**
     * Creates the label that shows the user his name
     *
     * @return the {@link Label}
     */
    private Label createUserNameLabel() {
        username = Gdx.app.getPreferences("userinfo").getString("username");
        Label usernameLabel = new Label("Username: " + username, skin);
        usernameLabel.setFontScale(1f);
        usernameLabel.setAlignment(Align.center);
        return usernameLabel;
    }

    /**
     * Makes the {@link TextField} only accept letters, digits and other defined chars as input
     *
     * @param textField the {@link TextField} to apply the filter to
     */
    private void addNameFilter(TextField textField) {
        textField.setTextFieldFilter(new TextField.TextFieldFilter() {
            private final String otherAcceptedChars = "' !";

            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isLetterOrDigit(c) || otherAcceptedChars.indexOf(c) >= 0;
            }
        });
    }

    /**
     * Opens a dialog for creating a new lobby.
     * <p>
     * By default, the lobby name is set to "{username}´s Lobby".
     * The "Cancel"-Button closes the dialog,
     * the "Create"-Button is only active if your inout is not empty, and will direct you to the game screen
     * in your own lobby if clicked.
     */
    private void createLobbyDialog() {
        TextField lobbyNameField = new TextField(username + "'s Lobby", skin);
        addNameFilter(lobbyNameField);

        Dialog dialog = new Dialog("Lobby creation", skin) {
            @Override
            protected void result(Object object) {
                if ("create".equals(object)) {
                    Gdx.app.postRunnable(() -> {
                        Packet packet = new Packet(new CreateLobbyDTO(lobbyNameField.getText()), LOBBY_CREATE);
                        Main.getWebSocketService().send(packet);
                    });
                }
            }
        };
        dialog.setResizable(false);
        dialog.setMovable(false);

        Table contentTable = dialog.getContentTable();
        contentTable.defaults().pad(20);

        TextButton createButton = new TextButton("Create", skin);

        Label lobbyNameLabel = new Label("Lobby Name:", skin);

        lobbyNameField.setMessageText("Enter lobby name");
        onChange(lobbyNameField, () -> {
            createButton.setDisabled(lobbyNameField.getText().length() < 4);
        });

        contentTable.add(lobbyNameLabel).center();
        contentTable.add(lobbyNameField).width(400).height(50).center().row();

        dialog.button(createButton, "create").pad(18);
        dialog.button("Cancel", null).pad(18);

        dialog.key(Input.Keys.ESCAPE, null);

        dialog.show(stage);
        dialog.setPosition((stage.getWidth() - dialog.getWidth()) / 2, (stage.getHeight() - dialog.getHeight()) / 2);
    }


    /**
     * Function to be directed to GameScreen
     */
    private void goToGameScreen() {
        getScreenManager().navigateTo(Screens.Game);
    }

    /**
     * filters the lobbies with the input live
     *
     * @param input content of the input field
     */
    private void filterLobbies(String input) {
        String normalizedInput = input.toLowerCase();

        List<LobbyModel> filteredLobbies = new ArrayList<LobbyModel>();

        if (allLobbies.isEmpty()) {
            return;
        }

        List<Map.Entry<LobbyModel, Integer>> lobbiesWithDistances = new ArrayList<>();

        for (LobbyModel lobby : allLobbies) {
            String normalizedLobbyName = lobby.getLobbyName().toLowerCase();
            int levenshteinDistance = calculateLevenshteinDistance(normalizedInput, normalizedLobbyName);
            if (normalizedLobbyName.contains(normalizedInput) || levenshteinDistance <= 2) {
                lobbiesWithDistances.add(new AbstractMap.SimpleEntry<>(lobby, levenshteinDistance));
            }
        }

        lobbiesWithDistances.sort(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<LobbyModel, Integer> entry : lobbiesWithDistances) {
            filteredLobbies.add(entry.getKey());
        }

        noMatchingLobbyLabel.setVisible(filteredLobbies.isEmpty());

        loadLobbies(filteredLobbies);
    }

    /**
     * Loads the given lobbies into the lobby list
     *
     * @param lobbies the list of lobbies to be loaded
     */
    private void loadLobbies(List<LobbyModel> lobbies) {
        centerContent.clearChildren();

        if (lobbies.isEmpty()) {
            centerContent.add(noMatchingLobbyLabel).expandX().center().padTop(10);
        } else {
            for (LobbyModel lobby : lobbies) {
                Button lobbyMember = new Button(skin);
                lobbyMember.setBackground(skin.getDrawable("round-light-gray"));
                lobbyMember.pad(5);
                lobbyMember.left();

                lobbyMember.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Main.getLogger().log(Level.SEVERE, "Creating lobby " + lobby.getLobbyName());
                        Gdx.app.postRunnable(() -> {
                            var packet = new Packet(new JoinLobbyDTO(lobby.getLobbyId()), LOBBY_JOIN);
                            Main.getWebSocketService().send(packet);
                            getLobbyService().setCurrentLobby(lobby);
                        });
                    }
                });

                Label lobbyLabel = new Label("Lobby name: " + lobby.getLobbyName() + ", Status: " + lobby.getGameState(), skin);
                lobbyMember.add(lobbyLabel).expandX().left().padRight(10);

                centerContent.add(lobbyMember).growX().padBottom(10);
                centerContent.row();
            }
        }
    }


    /**
     * Calculates the levenshtein distance between s1 and s2
     * <p>
     * The result can be used in combination with a threshold to filter the shown lobbies.
     *
     * @param s1 the first String of the comparison
     * @param s2 the second String of the comparison
     * @return the levenshtein distance between {@code s1} and {@code s2}
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        // Destroy screen's assets here.
    }

    /**
     * Method that handles receiving a LOBBY_INFO package
     *
     * @param packetJson the packet in Json format
     */
    private void onLobbyInfo(String packetJson) {
        Gdx.app.postRunnable(() -> {
            JsonValue data = new JsonReader().parse(packetJson).get("data");
            allLobbies = LobbyMapper.lobbiesFromJson(data.get("lobbies"));
            lobbyNameInput.setText(defaultSearchString);
            loadLobbies(allLobbies);
        });
    }

    /**
     * Method that handles receiving a LOBBY_CREATED package
     *
     * @param packetJson the packet as Json format
     */
    private void onLobbyCreated(String packetJson) {
        Gdx.app.postRunnable(() -> {
            JsonValue data = new JsonReader().parse(packetJson).get("data");
            LobbyModel lobby = LobbyMapper.lobbyFromJson(data);
            getLobbyService().setCurrentLobby(lobby);
            goToGameScreen();
        });
    }

    /**
     * Method that handles receiving a LOBBY_JOINED package
     *
     * @param packetJson the packet as Json format
     */
    private void onLobbyJoined(String packetJson) {
        Gdx.app.postRunnable(() -> {
            JsonValue data = new JsonReader().parse(packetJson).get("data");
            var packet = new JoinLobbyResultDTO(data.getString("result"));
            if (packet.isSuccess()) goToGameScreen();
            else {
                Main.getLogger().log(Level.SEVERE, "Failed to join lobby with result:" + packet.getResult());
                getLobbyService().setCurrentLobby(null);
            }
        });
    }

    private void onPlayerJoined(String packetJson) {
        var player = PlayerInfoMapper.fromDTO(packetJson);
        getLobbyService().addPlayer(player);
    }
}

