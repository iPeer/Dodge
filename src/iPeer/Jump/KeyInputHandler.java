package iPeer.Jump;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		lastKey = e.getKeyCode();
		Debug.p("Key Pressed: "+e.getKeyCode());
		if (lastKey == 114) {
			Game.debugActive = !Game.debugActive;
		}
		if (lastKey == 39) {
			Game.dirX++;
		}
		if (lastKey == 37) {
			Game.dirX--;
		}
		if (lastKey == 40) {
			Game.dirY++;
		}
		if (lastKey == 38) {
			Game.dirY--;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		lastKey = e.getKeyCode();

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
