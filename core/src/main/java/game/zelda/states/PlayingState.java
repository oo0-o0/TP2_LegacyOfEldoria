package game.zelda.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import game.zelda.GameContext;
import game.zelda.camera.CameraController;
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
    private CameraController cameraController;
    private ShapeRenderer shapeRenderer;
    private Map map;

    public PlayingState(GameContext gameContext)
    {
        this.gameContext = gameContext;
        this.player = new Player(100, 100);
        this.moveCommand = new MoveCommand(player, 250f);
        this.attackCommand = new AttackCommand(player);

        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();
        this.cameraController = new CameraController(viewportWidth, viewportHeight);
        this.shapeRenderer = new ShapeRenderer(); 
        this.map = new Map();
        this.player = new Player(100, 100, (TiledMapTileLayer) map.getLayers());
        this.moveCommand = new MoveCommand(player, 250f); 
        this.attackCommand = new AttackCommand(player);
        
    }

    @Override
    public void update(float deltaTime) 
    {
        handleInput(deltaTime);
        player.update(deltaTime);
        
        cameraController.update(player.getPosition(), 1080, 600); 
        player.updateColisionMap();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        map.renderMapOnScreen();

        batch.setProjectionMatrix(cameraController.getMainCamera().combined);
        batch.begin();
        player.render(batch);
        batch.end();

        renderMiniMap(batch);
    }

    private void renderMiniMap(SpriteBatch batch) 
    {
        int width = Gdx.graphics.getWidth() / 4;
        int height = Gdx.graphics.getHeight() / 4;
        int x = Gdx.graphics.getWidth() - width;
        int y = Gdx.graphics.getHeight() - height;
    
        batch.flush();
        Gdx.gl.glViewport(x, y, width, height);
    
        batch.setProjectionMatrix(cameraController.getMiniMapCamera().combined);
        batch.begin();
        player.render(batch);
        batch.end();
    
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); 
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
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