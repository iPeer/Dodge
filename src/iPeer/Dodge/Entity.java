package iPeer.Dodge;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {

	protected double x, y, dx ,dy;
	protected Sprite sprite;
	
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();
	
	public Entity(String r, int x, int y) {
		this.sprite = SpriteManager.get().getSprite(r);
		this.x = x;
		this.y = y;
	}
	
	public void move (long d) {
		 x -= (dx) / 100;
		 y += (d * dy) / 1000;
	}
	
	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}
	
	public void setVerticleMovement(double dy) {
		this.dy = dy;
	}
	
	public double getHorizontalMovement() {
		return dx;
	}
	
	public double getVerticleMovement() {
		return dy;
	}
	
	public void draw(Graphics g) {
		sprite.draw(g, (int)x, (int)y);
	}
	
	public void doLogic() { }
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public boolean collidesWith(Entity other) {
		me.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int)other.x, (int)other.y, other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	}
	
	public abstract void collidedWith(Entity other);
	
}
