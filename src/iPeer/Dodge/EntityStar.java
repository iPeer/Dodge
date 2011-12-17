package iPeer.Dodge;

public class EntityStar extends Entity {

	private Game game;
	private double moveSpeed = 105;
	
	public EntityStar(Game game, String r, int x, int y) {
		super(r, x, y);
		
		this.game = game;
		dx = +moveSpeed;
	}

	public void move(long d) {
		if (x < -31) { // Off screen
			game.removeEntitity(this);
			game.entityIsOffscreen(this);
			return;
		}
		super.move(d);
	}
	
	@Override
	public void collidedWith(Entity other) {
		// Not handled here
	}

}
