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
		g = new Game();
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

		requestFocus();

		createBufferStrategy(2); // Accelerated graphics!
		bs = getBufferStrategy();

		//initEntities();
	}

	public void gameLoop() {
		Debug.p("Game running!");
		while (gameRunning) {
			//FPS.start();
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
		g.setColor(Color.WHITE);
		Font defaultfont = g.getFont();
		if (!isOnTitle) {
			if (!paused && !playerisdead && !victory) {

				Random r = new Random();
				if ((r.nextInt(shipchance) >= shiptrigger) && (entitycount < maxships)) {
					entitycount++;
					Entity e;
					if (r.nextInt(10) == 0) {
						e = new EntityStar(this, "sprites/star.png", getWidth() + 5, new Random().nextInt(getHeight()));
					}
					else {
						e = new EntityObstacle(this, "sprites/obstacle.png", getWidth() + 5, new Random().nextInt(getHeight()));
					}
					entities.add(e);
				}
				if (!entities.isEmpty()) {
					for (int i = 0;i < entities.size(); i++) {
						Entity e = (Entity)entities.get(i);
						e.move(System.currentTimeMillis());
						if (e.getX() <= -31) {
							removeEntity(e);
							entityIsOffscreen(e);
							if (e instanceof EntityObstacle) {
								addScore(10);
							}

						}
						else {
							e.draw(g);
						}
					}
				}

				/*for (int i = 0; i < entities.size(); i++) {
					Entity e = (Entity)entities.get(i);
					e.move(tickStart);
				}


				if (runLogic) {
					for (int e = 0;e < entities.size();e++) {
						Entity entity = (Entity)entities.get(e);
						entity.doLogic();
					}
				}*/

			}
			if (victory || playerisdead) {
				g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 40));
				String t, t1, t2, t3, t4;
				t = "nope";
				t1 = "NOPE";
				t2 = "To play again, press R!";
				t3 = "Or if you prefer, you can return to the title screen by pressing M.";
				t4 = "Score: "+Integer.toString(score);
				if (victory) {
					g.setColor(Color.GREEN);
					t = "Congratulations!";
					t1 = "You win!";
				}
				else {
					g.setColor(Color.RED);
					t = "GAME OVER!";
					t1 = "You lose!";
				}
				g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, 80);
				g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 30));
				g.drawString(t1, (this.getWidth() - g.getFontMetrics().stringWidth(t1)) / 2, 120);
				g.setFont(defaultfont);
				g.drawString(t2, (this.getWidth() - g.getFontMetrics().stringWidth(t2)) / 2, 180);
				g.drawString(t3, (this.getWidth() - g.getFontMetrics().stringWidth(t3)) / 2, 200);
				g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));
				g.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
				g.drawString(t4, (this.getWidth() - g.getFontMetrics().stringWidth(t4)) / 2, 150);
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
			for (int i = 1; i < entities.size(); i++) {
				Entity e = (Entity)entities.get(i);
				if ((e.getX() <= this.getWidth()+1) && (!victory) && (!playerisdead)) { e.draw(g); }
			}
			if ((!victory) && (!playerisdead)) {
				Entity pe = (Entity)entities.get(0);
				pe.draw(g);
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
			g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));
			if (!victory && !playerisdead) {
				g.drawString("Score: ", (this.getWidth() - g.getFontMetrics().stringWidth("Score: "+score)) / 2, 35);
				g.drawString(Integer.toString(score), g.getFontMetrics().stringWidth("Score: ") + (this.getWidth() - g.getFontMetrics().stringWidth("Score: "+score)) / 2, 35);
			}
			entities.removeAll(removeList);
			removeList.clear();
			g.setFont(defaultfont);
			if (paused) {
				g.setColor(Color.YELLOW);
				g.setFont(new Font(g.getFont().getName(), Font.BOLD, 40));
				if (flash == 0)
					flash = 100;
				if (flash > 49) {
					String t = "PAUSED!";
					String t1 = "Press P to resume";
					g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, 80);
					g.setFont(defaultfont);
					g.drawString(t1, (this.getWidth() - g.getFontMetrics().stringWidth(t1)) / 2, 90);
				}
				flash--;
			}
		}
		else {
			Random r = new Random();
			if (r.nextInt(100) == 0) {
				Entity e;
				if (r.nextInt(10) == 0) {
					e = new EntityStar(this, "sprites/star.png", getWidth() + 5, new Random().nextInt(getHeight()));
				}
				else {
					e = new EntityObstacle(this, "sprites/obstacle.png", getWidth() + 5, new Random().nextInt(getHeight()));
				}
				entities.add(e);
			}
			if (!entities.isEmpty()) {
				for (int i = 0;i < entities.size(); i++) {
					Entity e = (Entity)entities.get(i);
					e.move(System.currentTimeMillis());
					if (e.getX() <= -31) {
						removeEntity(e);
					}
					else {
						e.draw(g);
					}
				}
			}
			entities.removeAll(removeList);
			removeList.clear();
			int rw = 350, rh = 100, aw = 10, ah = 10;
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect((this.getWidth() - rw) / 2, 50, rw, rh, aw, ah);
			Color c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
			Color c2 = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
			Color c3 = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
			g.setColor(c);
			g.drawRoundRect((this.getWidth() - (rw-4)) / 2, 52, rw-5, rh-5, aw, ah);
			g.setColor(c2);
			g.drawRoundRect((this.getWidth() - (rw-3)) / 2, 51, rw-3, rh-3, aw, ah);
			g.setColor(c3);
			g.drawRoundRect((this.getWidth() - (rw-1)) / 2, 50, rw-1, rh-1, aw, ah);
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font(g.getFont().getName(), Font.BOLD, 60));
			g.drawString("DODGE!", (this.getWidth() - g.getFontMetrics().stringWidth("DODGE!")) / 2, 120);
			g.setFont(defaultfont);
			g.drawString("iPeer's first game ever!", (this.getWidth() - g.getFontMetrics().stringWidth("iPeer's first game ever!")) / 2, 136);
			String t = "Controls";
			g.setFont(new Font(defaultfont.getFontName(), Font.BOLD, 15));
			g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, 200);
			t = "Objective";
			g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, 330);
			g.setFont(defaultfont);
			String[] controls = {"Start Game: S", "Restart: R", "Movement: Arrows Keys", "Quit: Q/ESC", "Pause: P", "Title Screen: M"};
			int posy = 220;
			for (int str = 0; str < controls.length; str++) {
				t = controls[str];
				g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, posy);
				posy += 12;
			}
			String[] objectives = {"This game is loosely based in the game \"Asteroids\", only this time you're against ships, any you don't have a gun.", "Only your quick reflexes and expert dodging skills will be able to save you.", "", "For every Asteroid you successfully dodge, you earn points. Picking up the stars floating about with in the earns you even more!", "The more points you have, the more you earn!"};
			int posy2 = 350;
			for (int str = 0; str < objectives.length; str++) {
				t = objectives[str];
				g.drawString(t, (this.getWidth() - g.getFontMetrics().stringWidth(t)) / 2, posy2);
				posy2 += 12;
			}
		}
		g.setFont(defaultfont);
		g.setColor(Color.WHITE);
		g.drawString(FPS.getFPS(), 0, getSize().height - 2);
		g.drawString(SIMPLES_Text, (getSize().width - g.getFontMetrics().stringWidth(SIMPLES_Text)), 10);
		if (debugActive) {
			g.drawString("R: "+Integer.toString(renderTime)+" ms K: "+KeyInputHandler.getLastKey()+" F: "+FPS.frametime+" P: "+flash, 2, 12);
			g.drawString("E: "+entities.size()+" W: "+this.getWidth()+" H: "+this.getHeight()+" O: "+entitycount+" V: "+VERSION+" M: "+maxships+" E: "+entitycount, 2, 22);
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
					g.drawString("["+(entities.size() - (i-1))+" more]", 2, screenpos +10);
				}
				screenpos += 10;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initPlayer() {
		player = new EntityPlayer(this, "sprites/player.png", 30, (this.getHeight() - 32) / 2);
		entities.add(player);
	}
	void notifyDeath() {
		playerisdead = true;
	}

	public void addScore(int s) {
		score += Math.floor(s + (score*.1));
	}

	public void updateLogic() {
		runLogic = true;
	}

	public void restart() {
		entities.clear();
		score = 0;
		initPlayer();
		paused = false;
		victory = false;
		playerisdead = false;
		player.setHorizontalMovement(0);
		player.setVerticleMovement(0);
		moveSpeed = 150;
		baseMovement = 105;
		shiptrigger = 95;
	}

	@SuppressWarnings("unchecked")
	public void removeEntity(Entity o) {
		removeList.add(o);
	}

	public void entityIsOffscreen(Entity en) {
		dodgedentitycount++;

		if (dodgedentitycount == maxships) {
			notifyWin();
		}
		moveSpeed *= 1.02;
		for (int i = 1; i < entities.size(); i++) {
			Entity e = (Entity)entities.get(i);
			e.setHorizontalMovement(e.getHorizontalMovement() * 1.02);
		}
		baseMovement *= 1.02;
		if (baseMovement > 200) {
			shiptrigger = 90;
		}
		if (baseMovement > 400) {
			shiptrigger = 75;
		}

	}

	public void notifyWin() {
		victory = true;
	}

	public void startNewGame() {
		entities.clear();
		entitycount = 0;
		baseMovement = 105;
		shiptrigger = 95;
		initPlayer();
		isOnTitle = false;
	}

	public void returnToTitle() {
		playerisdead = Game.victory = Game.paused = false;
		entities.clear();
		isOnTitle = true;
		score = 0;
		player.setHorizontalMovement(0);
		player.setVerticleMovement(0);
		moveSpeed = 150;
		baseMovement = 105;
		shiptrigger = 95;
	}

	Thread gameThread;
	private boolean gameRunning = true, ignoreCollisions = false;
	private BufferStrategy bs;
	private int shipchance = 100, endtime, starttime = 0, renderTime, score, entitycount, dodgedentitycount = 0, maxships = 1000;
	static int flash = 100;
	public static boolean debugActive = false, paused = false, playerisdead = false, victory = false;
	static int dirX = 50, dirY = 50;
	boolean runLogic;
	Entity player;
	@SuppressWarnings("rawtypes")
	private ArrayList entities = new ArrayList(), removeList = new ArrayList();
	public boolean uppressed = false, downpressed = false, leftpressed = false, rightpressed = false, isOnTitle = true;
	private int moveSpeed = 150, shiptrigger = 95;
	public static int baseMovement = 105;
	static Game g;
	private String SIMPLES_Text = "SIMPLE Studios";
	private double VERSION = 1.0;
}
