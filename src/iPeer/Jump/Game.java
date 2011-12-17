package iPeer.Jump;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
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
		JFrame jf = new JFrame("Jump!");

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
		addKeyListener(new KeyInputHandler()); // Listen for keypresses

		requestFocus(); // Be a thief and ask for focus.

		createBufferStrategy(2); // Accelerated graphics!
		bs = getBufferStrategy();
	}

	public void gameLoop() {
		Debug.p("Game running!");

		while (gameRunning) {
			starttime = (int)System.currentTimeMillis();
			//FPS.start();
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

	private void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 854,480);
		//String t = dirX+" | "+getWidth()+" | "+dirY+" | "+getHeight();
		String t = "BOUNCY TEXT DISCO, WOO!";
		int strLen = g.getFontMetrics().stringWidth(t);
		g.setColor(Color.RED);
		if (dirY == getHeight()) {
			moveup = false;
		}
		if (dirY-10 <= 0) { 
			moveup = true;
		}
		dirY = moveup ? dirY + 1 : dirY -1;
		if ((dirX + strLen) == getWidth()) {
			moveright = false;
		}
		if (dirX <= 0) { 
			moveright = true;
		}
		g.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
		dirX = moveright ? dirX + 1 : dirX -1;
		g.drawString(t, dirX, dirY);
		g.setColor(Color.WHITE);
		g.drawString(FPS.getFPS()+" fps", 0, getSize().height - 2);
		if (debugActive) {
			g.drawString(Integer.toString(renderTime)+" ms", 2, 12);
			g.drawString(Integer.toString(KeyInputHandler.getLastKey()), 2, 22);
		}
	}

	public void paint(Graphics g) {
		g.drawString("Hello World!", getSize().width / 2, getSize().height / 2);
	}

	Thread gameThread;
	private boolean gameRunning = true;
	private BufferStrategy bs;
	private int endtime, starttime= 0, renderTime;
	public static boolean debugActive = false;
	static int dirX = 50, dirY = 50;
	private boolean moveright = false, moveup = false;
}
