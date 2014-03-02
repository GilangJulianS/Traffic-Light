package TL;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Car {

	public final static int GO_LEFT = 0;
	public final static int GO_RIGHT = 1;
	public final static int GO_UP = 2;
	public final static int GO_DOWN = 3;
	public final static int DEFAULT_WIDTH_P = 17;
	public final static int DEFAULT_HEIGHT_P = 32;
	public final static int DEFAULT_WIDTH_L = 32;
	public final static int DEFAULT_HEIGHT_L = 17;
	public final static int DEFAULT_SPEED_DOWN = 2;
	public final static int DEFAULT_SPEED_UP = -2;
	public final static int DEFAULT_SPEED_LEFT = -2;
	public final static int DEFAULT_SPEED_RIGHT = 2;
	public Rectangle rect, colliBound;
	protected int speedX;
	protected int speedY;
	public int type, imageCode;
	public boolean isModified;
	
	
	public Car(int x, int y, int type, int imageCode) {
		this.type = type;
		isModified = false;
		this.imageCode = imageCode;
		switch(type){
		case GO_LEFT:
			speedX = DEFAULT_SPEED_LEFT;
			speedY = 0;
			rect = new Rectangle(x, y, DEFAULT_WIDTH_L, DEFAULT_HEIGHT_L);
			colliBound = new Rectangle(x-10, y, DEFAULT_WIDTH_L+20, DEFAULT_HEIGHT_L);
			break;
		case GO_RIGHT:
			speedX = DEFAULT_SPEED_RIGHT;
			speedY = 0;
			rect = new Rectangle(x, y, DEFAULT_WIDTH_L, DEFAULT_HEIGHT_L);
			colliBound = new Rectangle(x-10, y, DEFAULT_WIDTH_L+20, DEFAULT_HEIGHT_L);
			break;
		case GO_UP:
			speedX = 0;
			speedY = DEFAULT_SPEED_UP;
			rect = new Rectangle(x, y, DEFAULT_WIDTH_P, DEFAULT_HEIGHT_P);
			colliBound = new Rectangle(x, y-10, DEFAULT_WIDTH_P, DEFAULT_HEIGHT_P+20);
			break;
		case GO_DOWN:
			speedX = 0;
			speedY = DEFAULT_SPEED_DOWN;
			rect = new Rectangle(x, y, DEFAULT_WIDTH_P, DEFAULT_HEIGHT_P);
			colliBound = new Rectangle(x, y-10, DEFAULT_WIDTH_P, DEFAULT_HEIGHT_P+20);
			break;
		default: break;
		}
	}
	
	public void update(){
		rect.x += speedX;
		rect.y += speedY;
		colliBound.x += speedX;
		colliBound.y += speedY;
	}
	
	public void resetSpeed(){
		isModified = false;
		switch(type){
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
		default: break;
		}
	}
	
	public void setSpeed(int x, int y){
		speedX = x;
		speedY = y;
		isModified = true;
	}
	
	public void draw(Graphics2D g, Image car, ImageObserver IO){
		g.drawImage(car, rect.x, rect.y, rect.x + rect.width, rect.y+rect.height,
				0, 0, car.getWidth(null), car.getHeight(null), IO);
		
	}

}
