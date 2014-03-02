package TL;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Pedestrian {

	public final static int GO_DOWN = 0;
	public final static int GO_LEFT = 1;
	public final static int GO_RIGHT = 2;
	public final static int GO_UP = 3;
	public final static int DEFAULT_SPEED_LEFT = -1;
	public final static int DEFAULT_SPEED_RIGHT = 1;
	public final static int DEFAULT_SPEED_UP = -1;
	public final static int DEFAULT_SPEED_DOWN = 1;
	public final static int DEFAULT_WIDTH = 25;
	public final static int DEFAULT_HEIGHT = 25;
	private static Rectangle topLeft;
	private static Rectangle topRight;
	private static Rectangle botLeft;
	private static Rectangle botRight;
	private int i;
	public Rectangle rect;
	private int speedX, speedY;
	private int walktype;
	private long time;
	private int minus;

	public Pedestrian(int x, int y, int walktype) {
		this.walktype = walktype;
		i = 0; minus = 1;
		switch (walktype) {
		case GO_LEFT:
			speedX = DEFAULT_SPEED_LEFT;
			speedY = 0;
			break;
		case GO_RIGHT:
			speedX = DEFAULT_SPEED_RIGHT;
			speedY = 0;
			break;
		case GO_UP:
			speedX = 0;
			speedY = DEFAULT_SPEED_UP;
			break;
		case GO_DOWN:
			speedX = 0;
			speedY = DEFAULT_SPEED_DOWN;
			break;

		}
		rect = new Rectangle(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		time = 0;
	}
	
	public void turn(int walktype){
		this.walktype = walktype;
		switch (walktype) {
		case GO_LEFT:
			speedX = DEFAULT_SPEED_LEFT;
			speedY = 0;
			break;
		case GO_RIGHT:
			speedX = DEFAULT_SPEED_RIGHT;
			speedY = 0;
			break;
		case GO_UP:
			speedX = 0;
			speedY = DEFAULT_SPEED_UP;
			break;
		case GO_DOWN:
			speedX = 0;
			speedY = DEFAULT_SPEED_DOWN;
			break;

		}
	}

	public void update(long elapsedTime) {
		rect.x += speedX;
		rect.y += speedY;
		time += elapsedTime;
		if (time >= 50) {
			i += minus;
			time = 0;
			if (i < 0) {
				minus = 1;
				i += minus;
			} else if (i > 2) {
				minus = -1;
				i += minus;
			}
		}
		switch(walktype){
		case GO_DOWN:
			if(rect.contains(280, 284))
				turn(GO_LEFT);
			else if(rect.contains(420, 284))
				turn(GO_RIGHT);
			break;
		case GO_LEFT:
			if(rect.contains(414,280))
				turn(GO_UP);
			else if(rect.contains(414, 422))
				turn(GO_DOWN);
			break;
		case GO_UP:
			if(rect.contains(420, 412))
				turn(GO_RIGHT);
			else if(rect.contains(275, 412))
				turn(GO_LEFT);
			break;
		case GO_RIGHT:
			if(rect.contains(280, 422))
				turn(GO_DOWN);
			else if(rect.contains(280, 280))
				turn(GO_UP);
			break;
			
		}

	}

	public void setSpeed(int speedX, int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public void draw(Graphics2D g, Image pedestrian, ImageObserver IO) {
		int x = 30 * i;
		int y = 32 * walktype;

		g.drawImage(pedestrian, rect.x, rect.y, rect.x + rect.width, rect.y
				+ rect.height, x, y, x + 30, y + 32, IO);
	}

}
