package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy{
	protected int healthPoints = 0;
	protected int damagePoints = 0;
	protected Vector2 position = new Vector2();
	protected String imgPath = "";
}

