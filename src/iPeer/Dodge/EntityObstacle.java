package iPeer.Dodge;

import java.util.Random;

public class EntityObstacle extends Entity {

	private Game game;
	private double moveSpeed = Game.baseMovement+new Random().nextInt(100);

	public EntityObstacle(Game game, String r, int x, int y) {
		super(r, x, y);

		this.game = game;
		dx = +moveSpeed;
	}

	public void move(long d) {
		if (x <= -31) { // Off screen
			if (!game.isOnTitle) {
				game.addScore(10);
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
