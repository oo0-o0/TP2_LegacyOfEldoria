package game.zelda.player.commands;

import com.badlogic.gdx.math.Vector2;
import game.zelda.player.Player;

public class MoveCommand implements Command 
{
    private Player player;
    private Vector2 direction;
    private float speed;
    private float deltaTime;

    public MoveCommand(Player player, float speed) 
    {
        this.player = player;
        this.speed = speed;
        this.direction = new Vector2(0, 0);
    }

    public void setDirection(float dx, float dy) 
    {
        direction.set(dx, dy);
    }

    public void setDeltaTime(float deltaTime) 
    {
        this.deltaTime = deltaTime;
    }

    @Override
    public void execute() 
    {
        Vector2 velocity = new Vector2(direction).scl(speed * deltaTime);
        player.move(velocity.x, velocity.y);
    }

    @Override
    public void undo() 
    {
        player.stop();
    }
}