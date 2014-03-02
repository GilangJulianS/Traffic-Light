package TL;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Pedestrian {

	public final static int GO_LEFT = 0;
	public final static int GO_RIGHT = 1;
	public final static int DEFAULT_SPEED_LEFT = -1;
	public final static int DEFAULT_SPEED_RIGHT = 1;
	public final static int DEFAULT_WIDTH = 25;
	public final static int DEFAULT_HEIGHT = 25;
	public static int i = 0;
	public Rectangle rect;
	private int speedX;
	private long time;
	
	public Pedestrian(int x, int y) {

			speedX = DEFAULT_SPEED_LEFT;
			rect = new Rectangle(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			time = 0;
	}

	public void update(long elapsedTime){
		rect.x += speedX;
		time += elapsedTime;
		if(time >= 100){
			i++;
			time = 0;
			if(i>3)
				i=0;
		}
		
	}
	
	public void setSpeed(int speed){
		speedX = speed;
	}

	public void draw(Graphics2D g, Image pedestrian, ImageObserver IO) {
		
		if (i == 0) {
		g.drawImage(pedestrian, rect.x, rect.y, rect.x + rect.width, rect.y+rect.height,
				0, 33, 30, 64/*pedestrian.getWidth(null), pedestrian.getHeight(null)*/, IO);
		} else if (i == 1) {
			g.drawImage(pedestrian, rect.x, rect.y, rect.x + rect.width, rect.y+rect.height,
					30, 33, 60, 64/*pedestrian.getWidth(null), pedestrian.getHeight(null)*/, IO);
		} else if (i == 2) {
			g.drawImage(pedestrian, rect.x, rect.y, rect.x + rect.width, rect.y+rect.height,
					60, 33, 90, 64/*pedestrian.getWidth(null), pedestrian.getHeight(null)*/, IO);
		} else if (i == 3) {
			g.drawImage(pedestrian, rect.x, rect.y, rect.x + rect.width, rect.y+rect.height,
				30, 33, 60, 64/*pedestrian.getWidth(null), pedestrian.getHeight(null)*/, IO);
		}
	}

}
