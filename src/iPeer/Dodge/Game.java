package iPeer.Dodge;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game extends Canvas {

	public static void main(String[] a) {
		Game g = new Game();
		g.gameLoop();
	}

	public Game() {
		JFrame jf = new JFrame("Dodge!");

		JPanel jp = (JPanel)jf.getContentPane();
		jp.setPreferredSize(new Dimension(854-10,480-10));
		jp.setLayout(null);

		setBounds(0, 0, 854, 480);
		jp.add(this);

		setIgnoreRepaint(true); // We don't want awt repainting

		jf.pack();
		jf.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension r = tk.getScreenSize();
		jf.setLocation((r.width - jf.getWidth()) / 2, (r.height - jf.getHeight()) / 2);
		jf.setVisible(true);

		// Check for when window closes.
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Debug.p("Exit!");
				System.exit(0);
			}
		});
		addKeyListener(new KeyInputHandler(this)); // Listen for keypresses

		requestFocus(); // Be a thief and ask for focus.

		createBufferStrategy(2); // Accelerated graphics!
		bs = getBufferStrategy();

		initEntities();
	}

	public void gameLoop() {
		Debug.p("Game running!");
		while (gameRunning) {
			//FPS.start();
			tickStart = System.currentTimeMillis();
			starttime = (int)System.currentTimeMillis();
			Graphics2D g = (Graphics2D)bs.getDrawGraphics();
			draw(g);
			g.dispose();
			bs.show();
			FPS.calcfps();
			endtime = (int)System.currentTimeMillis();
			renderTime = endtime - starttime;
			try {
				Thread.sleep(1000/60);
			} 
			catch (Exception e) {
				Debug.p("Unable to complete gameLoop");
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 854,480);
		//String t = dirX+" | "+getWidth()+" | "+dirY+" | "+getHeight();
		//String t = "BOUNCY TEXT DISCO, WOO!";
		//int strLen = g.getFontMetrics().stringWidth(t);
		g.setColor(Color.RED);
		Font defaultfont = g.getFont();

		if (!paused) {

			for (int i = 0; i < entities.size(); i++) {
				Entity e = (Entity)entities.get(i);
				e.move(tickStart);
			}


			if (runLogic) {
				for (int e = 0;e < entities.size();e++) {
					Entity entity = (Entity)entities.get(e);
					entity.doLogic();
				}
			}

		}
		player.setVerticleMovement(0);
		player.setHorizontalMovement(0);
		if ((uppressed) && (!downpressed)) {
			player.setVerticleMovement(moveSpeed);
		}
		if ((!uppressed) && (downpressed)) {
			player.setVerticleMovement(-moveSpeed);
		}
		if ((!leftpressed) && (rightpressed)) {
			player.setHorizontalMovement(-moveSpeed);
		}
		if ((leftpressed) && (!rightpressed)) {
			player.setHorizontalMovement(moveSpeed);
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity e = (Entity)entities.get(i);
			e.draw(g);
		}
		for (int p = 0; p < entities.size(); p++) { //collision
			for (int i = p+1; i < entities.size(); i++) {
				Entity pl = (Entity)entities.get(p);
				Entity ob = (Entity)entities.get(i);

				if (pl.collidesWith(ob) && !ignoreCollisions) {
					pl.collidedWith(ob);
					ob.collidedWith(pl);
				}
			}
		}
		g.setColor(Color.WHITE);
		g.drawString(FPS.getFPS(), 0, getSize().height - 2);
		g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));
		g.drawString("Score: ", (this.getWidth() - g.getFontMetrics().stringWidth("Score: "+score)) / 2, 35);
		g.drawString(Integer.toString(score), g.getFontMetrics().stringWidth("Score: ") + (this.getWidth() - g.getFontMetrics().stringWidth("Score: "+score)) / 2, 35);
		entities.removeAll(removeList);
		removeList.clear();
		if (debugActive) {
			g.setFont(defaultfont);
			g.drawString("R: "+Integer.toString(renderTime)+" ms K: "+KeyInputHandler.getLastKey()+" F: "+FPS.frametime, 2, 12);
			g.drawString("E: "+entities.size()+" W: "+this.getWidth()+" H: "+this.getHeight(), 2, 22);
			g.drawString("-- Entities --", 75, 42);
			int screenpos = 52;
			for (int i = 0;i < (entities.size() < 41 ? entities.size() : 41);i++) {
				int x, y;
				double xd, yd;
				Entity e = (Entity)entities.get(i);

				x = e.getX();
				y = e.getY();
				xd = e.getHorizontalMovement();
				yd = e.getVerticleMovement();
				g.drawString("ID: "+i+" | X: "+x+" Y: "+y+" XD: "+xd+" YD: "+yd, 2, screenpos);
				if ((i == 40) && entities.size() > 41) {
					g.drawString("["+(entities.size() - i)+" more]", 2, screenpos +10);
				}
				screenpos += 10;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initEntities() {
		// player
		player = new EntityPlayer(this, "sprites/player.png", 30, (this.getHeight() - 32) / 2);
		entities.add(player);
		int safespawn = this.getWidth() + 10;
		for (int i = 0; i < new Random().nextInt(250)+100; i++) {
			if (new Random().nextInt(10) == 0 || stars < 3) {
				Entity s = new EntityStar(this, "sprites/star.png", safespawn+new Random().nextInt(50), new Random().nextInt(this.getHeight()));
				entities.add(s);
				stars++;
			}
			Entity o = new EntityObstacle(this, "sprites/obstacle.png", safespawn, new Random().nextInt(this.getHeight()));
			safespawn += new Random().nextInt(70)+32;
			Debug.p(safespawn);
			entities.add(o);
			obstaclecount++;
		}
	}

	public void notifyDeath() {
		paused = true;
	}

	public void addScore(int s) {
		score += s;
	}

	public void updateLogic() {
		runLogic = true;
	}

	public void restart() {
		entities.clear();
		score = 0;
		initEntities();
		paused = false;
	}

	@SuppressWarnings("unchecked")
	public void removeEntitity(Entity o) {
		removeList.add(o);
	}

	public void entityIsOffscreen(Entity en) {
		if (en instanceof EntityObstacle) {
			obstaclecount--;

			if (obstaclecount == 0) {
				notifyWin();
			}
		}

		for (int e = 0; e < entities.size(); e++) {		
			Entity o = (Entity)entities.get(e);
			o.setHorizontalMovement(o.getHorizontalMovement() * 1.02);
		}
		moveSpeed *= 1.02;

	}

	public void notifyWin() {
		paused = true;
	}

	Thread gameThread;
	private boolean gameRunning = true, ignoreCollisions = false;
	private BufferStrategy bs;
	private int endtime, starttime = 0, renderTime, score, obstaclecount, stars;
	public static boolean debugActive = false, paused = false;
	static int dirX = 50, dirY = 50;
	boolean runLogic;
	private long tickStart;
	Entity player;
	@SuppressWarnings("rawtypes")
	private ArrayList entities = new ArrayList(), removeList = new ArrayList();
	public boolean uppressed = false, downpressed = false, leftpressed = false, rightpressed = false;;
	private int moveSpeed = 150;
}
