package norites;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

public abstract class Sprite {
	protected float x;
	protected float y;
	protected float vx;
	protected float vy;
	protected int width;
	protected int height;
	protected int type;
	protected boolean isUsing;
	protected Animation sprite;

	public Sprite(float x, float y) {
		this.x = x;
		this.y = y;
		isUsing = true;
	}

	public abstract void update();

	public void draw(Graphics g) {
		if(isUsing) {
			sprite.draw(x, y);
		}
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isUsing() {
		return isUsing;
	}

	public void delete() {
		isUsing = false;
	}

	public int getType() {
		return type;
	}
}
