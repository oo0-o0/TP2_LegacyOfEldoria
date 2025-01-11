package game.zelda.player.commands;

import game.zelda.player.Player;

public class AttackCommand implements Command 
{
    private Player player;

    public AttackCommand(Player player) 
    {
        this.player = player;
    }

    @Override
    public void execute() 
    {
        player.attack();
    }

    @Override
    public void undo() 
    {
    }
}