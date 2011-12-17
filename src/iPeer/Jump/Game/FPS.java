package iPeer.Jump.Game;

public class FPS {

	public FPS() { }
	
	public final static void start () {
		starttime = (int)System.currentTimeMillis();
	}
	
	public final static void calcfps() {
		frametime = (int)System.currentTimeMillis() - starttime;
		frames++;
		if (frametime >= 1000) {
			Debug.p(frames+" fps");
			lastframes = frames;
			frametime = 0;
			frames = 0;
			starttime = (int)System.currentTimeMillis();
		}
	}
	
	public static String getFPS() {
		return Short.toString(lastframes);
	}
	
	public static int starttime, endtime, frametime;
	public static short frames;
	public static short lastframes;
	
}
