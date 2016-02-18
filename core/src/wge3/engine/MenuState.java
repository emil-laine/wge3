/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
                exitGame();
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
    
    public void exitGame() {
        Gdx.app.exit();
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
        if (Gdx.input.isKeyPressed(Keys.ENTER)) startGame();
        else if (Gdx.input.isKeyPressed(Keys.Q)) exitGame();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
