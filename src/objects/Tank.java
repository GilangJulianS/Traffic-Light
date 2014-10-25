package objects;

import java.awt.Rectangle;

public class Tank extends Car {

	public Tank(int x, int y, int type, int imageCode) {
		super(x, y, type, imageCode);
		switch(type){
		case GO_LEFT:
			rect = new Rectangle(x, y, (DEFAULT_WIDTH_L*2), (DEFAULT_HEIGHT_L*2)+10);
			colliBound = new Rectangle(x-10, y, (DEFAULT_WIDTH_L*2)+20, (DEFAULT_HEIGHT_L*2)+10);
			break;
		case GO_RIGHT:
			rect = new Rectangle(x, y, (DEFAULT_WIDTH_L*2), (DEFAULT_HEIGHT_L*2)+10);
			colliBound = new Rectangle(x-10, y, (DEFAULT_WIDTH_L*2)+20, (DEFAULT_HEIGHT_L*2)+10);
			break;
		case GO_UP:
			rect = new Rectangle(x, y, (DEFAULT_WIDTH_P*2)+10, (DEFAULT_HEIGHT_P*2));
			colliBound = new Rectangle(x, y-10, (DEFAULT_WIDTH_P*2)+10, (DEFAULT_HEIGHT_P*2)+20);
			break;
		case GO_DOWN:
			rect = new Rectangle(x, y, (DEFAULT_WIDTH_P*2)+10, (DEFAULT_HEIGHT_P*2));
			colliBound = new Rectangle(x, y-10, (DEFAULT_WIDTH_P*2)+10, (DEFAULT_HEIGHT_P*2)+20);
			break;
		default: break;
		}
	}

}
