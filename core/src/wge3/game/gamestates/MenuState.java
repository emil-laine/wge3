package wge3.game.gamestates;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import wge3.game.GameStateManager;

public final class MenuState extends GameState {
    
    private Stage stage;
    private Skin skin;
    
    private TextField mapNameField;
    private TextButton newGameButton;
    
    public MenuState(GameStateManager gsm) {
        super(gsm);
        init();
    }

    @Override
    public void init() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        
        int buttonWidth = 200;
        int buttonHeight = 50;
        int x = Gdx.graphics.getWidth() / 2 - (buttonWidth / 2);
        int y = Gdx.graphics.getHeight() - 200;
        
        Label menuLabel = new Label("WGE3 EXPERIMENTAL HQ", skin);
        menuLabel.setPosition(x, y);
        stage.addActor(menuLabel);
        
        String mapName;
        if (gsm.getNextMap() != null) mapName = gsm.getNextMap();
        else mapName = "";
        
        mapNameField = new TextField(mapName, skin);
        mapNameField.setPosition(x, y - buttonHeight*2);
        mapNameField.setSize(buttonWidth, buttonHeight);
        mapNameField.setMessageText("Enter map name");
        stage.addActor(mapNameField);
        
        newGameButton = new TextButton("NEW GAME", skin);
        newGameButton.setPosition(x, y - buttonHeight*4);
        newGameButton.setSize(buttonWidth, buttonHeight);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startGame();
            }
        });
        stage.addActor(newGameButton);
        
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.setPosition(x, y - buttonHeight*6);
        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);
    }
    
    public void startGame() {
        if (!Gdx.files.internal("maps/" + mapNameField.getText() + ".tmx").exists()) {
            gsm.setState(0);
            return;
        }
        
        gsm.setNextMap(mapNameField.getText());
        gsm.setState(1);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        handleInput();
    }

    @Override
    public void draw(Batch batch) {
        stage.draw();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(ENTER)) startGame();
        else if (Gdx.input.isKeyPressed(ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
