package iPeer.Dodge;

public class EntityPlayer extends Entity {
	
	private Game game;

	public EntityPlayer(Game game, String r, int x, int y) {
		super(r, x, y);
		this.game = game;
	}
	
	public void move(long d) {
		if ((dx > 0) && (x > game.getWidth())) {
			return;
		}
		if ((dx < game.getWidth()) && (x > 0)) {
			return;
		}
		super.move(d);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EntityObstacle) {
			game.notifyDeath();
		}
	}

}
