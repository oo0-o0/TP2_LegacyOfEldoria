package game.zelda.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.zelda.GameContext;
import game.zelda.player.commands.AttackCommand;
import game.zelda.player.commands.MoveCommand;
import game.zelda.player.Player;

public class PlayingState implements GameState 
{
    private Player player;
    private GameContext gameContext;
    private MoveCommand moveCommand;
    private AttackCommand attackCommand;

    public PlayingState(GameContext gameContext)
    {
        this.gameContext = gameContext;
        this.player = new Player(100, 100);
        this.moveCommand = new MoveCommand(player, 250f); 
        this.attackCommand = new AttackCommand(player);
    }

    @Override
    public void update(float deltaTime) 
    {
        handleInput(deltaTime);
        moveCommand.setDeltaTime(deltaTime);
        moveCommand.execute();
        player.update(deltaTime);
    }

    private void handleInput(float deltaTime) 
    {
        float dx = 0, dy = 0;

        // Player andando
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        // R de Run 
        if (Gdx.input.isKeyPressed(Input.Keys.R)) 
        {
            player.setRunning(true);
            moveCommand.setDeltaTime(deltaTime);
            moveCommand.execute();
        } 
        else 
        {
            player.setRunning(false);
        }

        // Player pula com enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) 
        {
            player.jump();
        }
    
        // Player ataca com espaço
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) 
        {
            attackCommand.execute();
        }
    
        if (dx != 0 || dy != 0) 
        {
            moveCommand.setDirection(dx, dy);
        } 
        else 
        {
            moveCommand.setDirection(0, 0);
            moveCommand.undo(); 
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, 1);
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