package iPeer.Dodge;

public class EntityPlayer extends Entity {
	
	private Game game;

	public EntityPlayer(Game game, String r, int x, int y) {
		super(r, x, y);
		this.game = game;
	}
	
	public void move(long d) {
		if (y <= 0) {
			y = 0;
			game.uppressed = false;
		}
		if (y >= game.getHeight()-32) {
			y = game.getHeight()-32;
			game.downpressed = false;
		}
		if (x <= 0) {
			x = 0;
			game.leftpressed = false;
		}
		if (x >= game.getWidth()-32) {
			x = game.getWidth()-32;
			game.rightpressed = false;
		}
		super.move(d);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EntityObstacle) {
			game.notifyDeath();
		}
		if (other instanceof EntityStar) {
			game.addScore(100);
			game.removeEntitity(other);
		}
	}

}
