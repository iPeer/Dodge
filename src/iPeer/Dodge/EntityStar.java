package iPeer.Dodge;

import java.util.Random;

public class EntityStar extends Entity {

	private Game game;
	private double moveSpeed = Game.baseMovement+new Random().nextInt(150);

	public EntityStar(Game game, String r, int x, int y) {
		super(r, x, y);

		this.game = game;
		dx = +moveSpeed;
	}

	public void move(long d) {
		if (x <= -31) { // Off screen
			if (!game.isOnTitle) {
				game.entityIsOffscreen(this);
			}
			game.removeEntity(this);
			return;
		}
		super.move(d);
	}

	@Override
	public void collidedWith(Entity other) {
		// Not handled here
	}

}
