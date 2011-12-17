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
		if (debugActive) {
			g.setColor(Color.WHITE);
			g.drawString(FPS.getFPS()+" fps", 0, getSize().height - 2);
			g.drawString(Integer.toString(renderTime)+" ms", 2, 12);
			g.drawString(Integer.toString(FPS.frametime), 2, 22);
			g.drawString(Integer.toString(KeyInputHandler.getLastKey()), 2, 32);
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

}
