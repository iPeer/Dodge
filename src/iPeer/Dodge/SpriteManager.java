package iPeer.Dodge;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteManager {

	private static SpriteManager s = new SpriteManager();

	@SuppressWarnings("rawtypes")
	private HashMap sprites = new HashMap();

	public static SpriteManager get() {
		return s;
	}

	@SuppressWarnings("unchecked")
	public Sprite getSprite(String r) {
		if (sprites.get(r) != null) {
			return (Sprite)sprites.get(r);
		}

		BufferedImage i = null;
		try {
			URL url = this.getClass().getClassLoader().getResource(r);
			if (url == null) {
				Debug.p("Cannot load URL: "+url);
				System.exit(0);
			}

			i = ImageIO.read(url);
		}
		catch (IOException e) { ; }

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(i.getWidth(), i.getHeight(), Transparency.BITMASK);

		image.getGraphics().drawImage(i, 0, 0, null);

		Sprite sprite = new Sprite(image);
		sprites.put(r, sprite);

		return sprite;

	}

}
