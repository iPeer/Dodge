package iPeer.Dodge;

public class EntityObstacle extends Entity {

	private Game game;
	private double moveSpeed = 105;
	
	public EntityObstacle(Game game, String r, int x, int y) {
		super(r, x, y);
		
		this.game = game;
		Debug.p(dx+" | "+moveSpeed+" | "+(dx = +moveSpeed));
		dx = +moveSpeed;
	}

	public void move(long d) {
		if (x < -31) { // Off screen
			Debug.p("OffScreen");
			game.addScore();
			game.removeEntitity(this);
			game.obstacleIsOffscreen();
			return;
		}
		super.move(d);
	}
	
	@Override
	public void collidedWith(Entity other) {
		// Not handled here
	}

}
