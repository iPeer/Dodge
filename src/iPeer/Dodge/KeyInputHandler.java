package iPeer.Dodge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputHandler implements KeyListener {

	private Game game;
	
	public KeyInputHandler(Game game) {
		this.game = game;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		lastKey = e.getKeyCode();
		if (lastKey == 114) {
			Game.debugActive = !Game.debugActive;
		}
		if (lastKey == 80) {
			Game.paused = !Game.paused;
		}
		if (lastKey == 82) {
			game.restart();
		}
		if (lastKey == 81) {
			Debug.p("Exit!");
			System.exit(0);
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			game.uppressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			game.downpressed = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT){ 
			game.leftpressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){ 
			game.rightpressed = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		lastKey = e.getKeyCode();
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			game.uppressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			game.downpressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){ 
			game.leftpressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){ 
			game.rightpressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		lastKey = e.getKeyChar();
		if (e.getKeyChar() == 27) {
			Debug.p("Exit!");
			System.exit(0);
		}

	}
	
	public static int getLastKey() {
		return lastKey;
	}
	public static int lastKey;

}
