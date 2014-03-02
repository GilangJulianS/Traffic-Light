package Christ;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import TL.Car;
import TL.Tank;

public class World {

	
	
	public static enum State {
		KKMMM, HHMMM, MMMKM, MMMHH, MMKHH, MMKHM, MMHHM;
		public State next() {
            return values()[(ordinal() + 1) % values().length];
		}
		public void print(){
			switch(ordinal()){
			case 0: System.out.println("KKMMM"); break;
			case 1: System.out.println("HHMMM"); break;
			case 2: System.out.println("MMMKM"); break;
			case 3: System.out.println("MMMHH"); break;
			case 4: System.out.println("MMKHH"); break;
			case 5: System.out.println("MMKHM"); break;
			case 6: System.out.println("MMHHM"); break;
			default: break;
			}
		}
	};
	
	private State state;
	private ThreeLamps US, Barat, Timur;
	private OneLamp TU;
	private long deltaTime, stateTime;
	public List<Car> carsLeft, carsRight, carsTop, carsBottom;
	private Car car;
	private Rectangle leftLine, rightLine, topLine, bottomLine;
	private boolean nabrak;

	public World() {
		US = new ThreeLamps();
		Barat = new ThreeLamps();
		Timur = new ThreeLamps();
		TU = new OneLamp();
		deltaTime = 0;
		state = State.MMHHM;
		stateTime = 5;
		carsLeft = new ArrayList<Car>();
		carsRight = new ArrayList<Car>();
		carsTop = new ArrayList<Car>();
		carsBottom = new ArrayList<Car>();
		leftLine = new Rectangle(220, 290, 20, 120);
		rightLine = new Rectangle(455, 290, 20, 120);
		topLine = new Rectangle(290, 220, 120, 20);
		bottomLine = new Rectangle(290, 460, 120, 20);
		nabrak = false;
	}

	public void update(long elapsedTime) {
		deltaTime += elapsedTime;
		if(deltaTime >= stateTime){
			//System.out.println(deltaTime + " " + stateTime);
			changeState();
		}
		int len = carsLeft.size();
		for(int i=len-1; i>=0; i--){
			car = carsLeft.get(i);
			car.update();
			nabrak = false;
			if(car.rect.x < -30 || car.rect.x>730 || car.rect.y < -30 || car.rect.y > 730){
				carsLeft.remove(i);
				i--; len--;
			}
			for(int j=i-1; j>=0 && !nabrak; j--){
				if(car.colliBound.intersects(carsLeft.get(j).colliBound)){
					car.setSpeed(0, 0);
					nabrak = true;
				}
			}
			if(car.isModified && !nabrak){
				car.resetSpeed();
				//System.out.println("reset");
			}
			if(Barat.isRed() || Barat.isYellow()){
				if(leftLine.intersects(car.colliBound)){
					car.setSpeed(0, 0);
				}
			}
			
		}
		len = carsRight.size();
		for(int i=len-1; i>=0; i--){
			car = carsRight.get(i);
			car.update();
			nabrak = false;
			if(car.rect.x < -30 || car.rect.x>730 || car.rect.y < -30 || car.rect.y > 730){
				carsRight.remove(i);
				i--; len--;
			}
			for(int j=i-1; j>=0 && !nabrak; j--){
				if(car.colliBound.intersects(carsRight.get(j).colliBound)){
					car.setSpeed(0, 0);
					nabrak = true;
				}
			}
			if(car.isModified && !nabrak)
				car.resetSpeed();
			if(Timur.isRed() || Timur.isYellow()){
				if(rightLine.intersects(car.colliBound))
					car.setSpeed(0, 0);
			}
			
		}
		len = carsTop.size();
		for(int i=len-1; i>=0; i--){
			car = carsTop.get(i);
			car.update();
			nabrak = false;
			if(car.rect.x < -30 || car.rect.x>730 || car.rect.y < -30 || car.rect.y > 730){
				carsTop.remove(i);
			}
			for(int j=i-1; j>=0 && !nabrak; j--){
				if(car.colliBound.intersects(carsTop.get(j).colliBound)){
					car.setSpeed(0, 0);
					nabrak = true;
				}
			}
			if(car.isModified && !nabrak)
				car.resetSpeed();
			if(US.isRed() || US.isYellow()){
				if(topLine.intersects(car.colliBound))
					car.setSpeed(0, 0);
			}
			
		}
		len = carsBottom.size();
		for(int i=len-1; i>=0; i--){
			car = carsBottom.get(i);
			car.update();
			nabrak = false;
			if(car.rect.x < -30 || car.rect.x>730 || car.rect.y < -30 || car.rect.y > 730){
				carsBottom.remove(i);
				i--; len--;
			}
			for(int j=i-1; j>=0 && !nabrak; j--){
				if(car.colliBound.intersects(carsBottom.get(j).colliBound)){
					car.setSpeed(0, 0);
					nabrak = true;
				}
			}
			if(car.isModified && !nabrak)
				car.resetSpeed();
			if(US.isRed() || US.isYellow()){
				if(bottomLine.intersects(car.colliBound))
					car.setSpeed(0, 0);
			}
			
		}
	}
	
	public void addCar(int x, int y, int type, int code){
		switch(type){
		case Car.GO_UP:
			if(code==9)
				carsBottom.add(new Tank(x, y, type, code));
			else
				carsBottom.add(new Car(x, y, type, code));
			break;
		case Car.GO_DOWN:
			if(code==9)
				carsTop.add(new Tank(x, y, type, code));
			else
				carsTop.add(new Car(x, y, type, code));
			break;
		case Car.GO_RIGHT:
			if(code==9)
				carsLeft.add(new Tank(x, y, type, code));
			else
				carsLeft.add(new Car(x, y, type, code));
			break;
		case Car.GO_LEFT:
			if(code==9)
				carsRight.add(new Tank(x, y, type, code));
			else
				carsRight.add(new Car(x, y, type, code));
			break;
			default: break;
		}
	}
	
	public void suddenChange(State newState){
		deltaTime = 0;
		state = newState;
		switch (state) {
		case KKMMM:
			US.setLamps(true, true, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, false, false);
			TU.setLamp(true);
			stateTime = Main.time[0];
			break;
		case HHMMM:
			US.setLamps(false, false, true);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, false, false);
			TU.setLamp(true);
			stateTime = Main.time[1];
			break;
		case MMMKM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, true, false);
			TU.setLamp(true);
			stateTime = Main.time[2];
			break;
		case MMMHH:
			US.setLamps(true, false, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(false);
			stateTime = Main.time[3];
			break;
		case MMKHH:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(false);
			stateTime = Main.time[4];
			break;
		case MMKHM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(true);
			stateTime = Main.time[5];
			break;
		case MMHHM:
			US.setLamps(true, false, false);
			Barat.setLamps(false, false, true);
			Timur.setLamps(false, false, true);
			TU.setLamp(true);
			stateTime = Main.time[6];
			break;
		}
	}
	
	public void changeState(){
		deltaTime = 0;
		state = state.next();
		//state.print();
		switch (state) {
		case KKMMM:
			US.setLamps(true, true, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, false, false);
			TU.setLamp(true);
			stateTime = Main.time[0];
			break;
		case HHMMM:
			US.setLamps(false, false, true);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, false, false);
			TU.setLamp(true);
			stateTime = Main.time[1];
			break;
		case MMMKM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(true, true, false);
			TU.setLamp(true);
			stateTime = Main.time[2];
			break;
		case MMMHH:
			US.setLamps(true, false, false);
			Barat.setLamps(true, false, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(false);
			stateTime = Main.time[3];
			break;
		case MMKHH:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(false);
			stateTime = Main.time[4];
			break;
		case MMKHM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(false, false, true);
			TU.setLamp(true);
			stateTime = Main.time[5];
			break;
		case MMHHM:
			US.setLamps(true, false, false);
			Barat.setLamps(false, false, true);
			Timur.setLamps(false, false, true);
			TU.setLamp(true);
			stateTime = Main.time[6];
			break;
		}
	}
	
	public ThreeLamps getUSLamp(){
		return US;
	}
	
	public ThreeLamps getBaratLamp(){
		return Barat;
	}
	
	public ThreeLamps getTimurLamp(){
		return Timur;
	}
	
	public OneLamp getTULamp(){
		return TU;
	}

}
