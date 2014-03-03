package TL;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Random;

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
	public boolean waitToCross, crossed, crossing, finish;
	private Random rand = new Random();

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
		waitToCross = false;
		crossed = false;
		crossing = false;
		finish = false;
	}
	
	public void turn(int walktype){
		this.walktype = walktype;
		walk();
	}
	
	public void walk(){
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
		
		time += elapsedTime;
		if(!waitToCross){
			rect.x += speedX;
			rect.y += speedY;
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
			if(rect.contains(440,245) || rect.contains(440, 450) || rect.contains(260, 245) || rect.contains(260, 450)){
				if(crossed && !finish){
					int a = rand.nextInt(2);
					if(a == 0){
						turn(GO_UP);
					}
					else{
						turn(GO_DOWN);
					}
					finish = true;
				}
				if(crossing){
					crossed = true;
					crossing = false;
				}
			}
			if((rect.contains(420,245) || rect.contains(420, 450)) && !crossing && !crossed){
				waitToCross = true;
				i = 1;
				turn(GO_LEFT);
				setSpeed(0, 0);
			}
			else if((rect.contains(280, 245) || rect.contains(280, 450)) && !crossing && !crossed){
				waitToCross = true;
				i = 1;
				turn(GO_RIGHT);
				setSpeed(0, 0);
			}
			if(rect.contains(350,450) || rect.contains(350,245)){
				crossing = true;
			}
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
