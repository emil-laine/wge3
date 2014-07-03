package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public final class MenuState extends GameState {
    
    Stage stage;
    Skin skin;

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        
        int buttonWidth = 200;
        int buttonHeight = 50;
        int x = Gdx.graphics.getWidth() / 2 - (buttonWidth / 2);
        int y = Gdx.graphics.getHeight() - 200;
        
        Label menuLabel = new Label("WGE3 EXPERIMENTAL HQ", skin);
        menuLabel.setPosition(x, y);
        stage.addActor(menuLabel);
        
        TextField mapNameField = new TextField("Enter map name", skin);
        mapNameField.setPosition(x, y - buttonHeight*2);
        mapNameField.setSize(buttonWidth, buttonHeight);
        stage.addActor(mapNameField);
        
        TextButton newGameButton = new TextButton("NEW GAME", skin);
        newGameButton.setPosition(x, y - buttonHeight*4);
        newGameButton.setSize(buttonWidth, buttonHeight);
        stage.addActor(newGameButton);
        
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.setPosition(x, y - buttonHeight*6);
        exitButton.setSize(buttonWidth, buttonHeight);
        stage.addActor(exitButton);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(Batch batch) {
        stage.draw();
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
