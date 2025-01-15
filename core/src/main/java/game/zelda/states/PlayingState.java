package game.zelda.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import game.zelda.GameContext;
import game.zelda.player.commands.AttackCommand;
import game.zelda.player.commands.MoveCommand;
import game.zelda.player.Player;
import game.zelda.player.PlayerAnimationManager;
import game.zelda.map.Map;

public class PlayingState implements GameState 
{
    private Player player;
    private GameContext gameContext;
    private MoveCommand moveCommand;
    private AttackCommand attackCommand;
    private ShapeRenderer shapeRenderer;
    private Map map;

    public PlayingState(GameContext gameContext)
    {
        this.gameContext = gameContext;
        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();
        this.shapeRenderer = new ShapeRenderer(); 
        this.map = new Map();
        
        List<TiledMapTileLayer> collisionLayers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            collisionLayers.add((TiledMapTileLayer) map.getLayers().get(i));
        }
        
        this.player = new Player(100, 100, collisionLayers);
        this.moveCommand = new MoveCommand(player, 250f); 
        this.attackCommand = new AttackCommand(player);
        
    }

    @Override
    public void update(float deltaTime) 
    {
        handleInput(deltaTime);
        player.update(deltaTime);
        
       // player.updateColisionMap();
    }

    private void handleInput(float deltaTime) 
    {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        if (dx != 0 || dy != 0) 
        {
            moveCommand.setDirection(dx, dy);
            moveCommand.setDeltaTime(deltaTime);
            moveCommand.execute();
        } 
        else if (player.animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.ATTACK_NORMAL && player.animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.JUMP) 
        {
            player.stop(); 
        }

        // Pulo
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) 
        {
            player.jump();
        }

        // Ataque
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) 
        {
            player.attack();
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        Gdx.gl.glClearColor(0.5137f, 0.6431f, 0.2863f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        map.renderMapOnScreen();

        batch.begin();
        player.render(batch);
        batch.end();

    }

    @Override
    public void enter() 
    {
        Gdx.app.log("PlayingState", "Entering PlayingState.");
    }

    @Override
    public void exit() 
    {
        Gdx.app.log("PlayingState", "Exiting PlayingState.");
    }
}