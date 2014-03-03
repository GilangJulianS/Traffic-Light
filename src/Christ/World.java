package Christ;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import TL.Car;
import TL.Pedestrian;
import TL.Tank;

public class World {

	public static enum State {
		KKMMM, HHMMM, MMMKM, MMMHH, MMKHH, MMKHM, MMHHM, MMKKM;
		public State next() {
			return values()[Main.nextState[ordinal()]];
            //return values()[(ordinal() + 1) % values().length];
		}
		public void print() {
			switch(ordinal()){
			case 0: System.out.println("KKMMM"); break;
			case 1: System.out.println("HHMMM"); break;
			case 2: System.out.println("MMMKM"); break;
			case 3: System.out.println("MMMHH"); break;
			case 4: System.out.println("MMKHH"); break;
			case 5: System.out.println("MMKHM"); break;
			case 6: System.out.println("MMHHM"); break;
			case 7: System.out.println("MMKKM"); break;
			default: break;
			}
		}
		@Override
		public String toString(){
			switch(ordinal()){
			case 0: return "KKMMM";
			case 1: return "HHMMM";
			case 2: return "MMMKM";
			case 3: return "MMMHH";
			case 4: return "MMKHH";
			case 5: return "MMKHM";
			case 6: return "MMHHM";
			case 7: return "MMKKM";
			default: return "";
			}
		}
	};
	
	public State state;
	private ThreeLamps US, Barat, Timur;
	private OneLamp TU;
	private long  stateTime, pedDelayTime, spawnTime;
	public long deltaTime;
	public int runTime;
	public List<Car> carsLeft, carsRight, carsTop, carsBottom;
	private Car car;
	private Rectangle leftLine, rightLine, topLine, bottomLine;
	private boolean nabrak, macet;
	public Pedestrian p;
	public List<Pedestrian> pedestrians;
	private Random rand;

	public World() {
		US = new ThreeLamps();
		Barat = new ThreeLamps();
		Timur = new ThreeLamps();
		TU = new OneLamp();
		deltaTime = 0;
		pedDelayTime = 0;
		runTime = 0;
		state = State.MMHHM;
		stateTime = 5;
		carsLeft = new ArrayList<Car>();
		carsRight = new ArrayList<Car>();
		carsTop = new ArrayList<Car>();
		carsBottom = new ArrayList<Car>();
		pedestrians = new ArrayList<Pedestrian>();
		rand = new Random();
		leftLine = new Rectangle(220, 290, 20, 120);
		rightLine = new Rectangle(455, 290, 20, 120);
		topLine = new Rectangle(290, 220, 120, 20);
		bottomLine = new Rectangle(290, 460, 120, 20);
		nabrak = false;
		macet= false;
		spawnTime = 500+rand.nextInt(2500);
	}

	public void update(long elapsedTime) {
		if(elapsedTime < 1000000)
			runTime += (int)elapsedTime;
		deltaTime += elapsedTime;
		pedDelayTime += elapsedTime;
		
		
		if(pedDelayTime >= spawnTime){
			pedDelayTime = 0;
			spawnTime = 500+rand.nextInt(2500);
			addPedestrian();
			generateCar();
		}
		if(deltaTime >= stateTime){
			changeState();
			if(macet){
				macet = false;
			}
		}
		if ((carsTop.size() >= 2 * (carsLeft.size() + carsRight.size())
				|| carsBottom.size() >= 2 * (carsLeft.size() + carsRight.size()))
				&& (carsTop.size() > 10 || carsBottom.size() > 10) && !macet && state!=State.KKMMM && state!=State.HHMMM) {
			suddenChange(State.KKMMM);
			macet = true;
		}
		int len = carsLeft.size();
		for(int i=len-1; i>=0; i--){
			car = carsLeft.get(i);
			car.update();
			nabrak = false;
			if(car.rect.x < -60 || car.rect.x>730 || car.rect.y < -60 || car.rect.y > 730){
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
			if(car.rect.x < -60 || car.rect.x>730 || car.rect.y < -60 || car.rect.y > 730){
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
			if(car.rect.x < -60 || car.rect.x>730 || car.rect.y < -60 || car.rect.y > 730){
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
			if(car.rect.x < -60 || car.rect.x>730 || car.rect.y < -60 || car.rect.y > 730){
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
		boolean nyebrang;
		if(US.isRed() && !US.isYellow())
			nyebrang = true;
		else
			nyebrang = false;
		for(int i=0; i<pedestrians.size(); i++){
			p = pedestrians.get(i);
			p.update(elapsedTime);
			if(nyebrang && p.waitToCross){
				p.waitToCross = false;
				p.walk();
			}
				
			if(p.rect.x < 0 || p.rect.x > 700 || p.rect.y < 0 || p.rect.y > 700){
				pedestrians.remove(i);
				i--; len--;
			}
		}
		
	}
	
	public void addPedestrian(){
		int n = rand.nextInt(4);
		int m = rand.nextInt(2);
		switch(n){
		case Pedestrian.GO_DOWN:
			if(m==0)
				pedestrians.add(new Pedestrian(260, 0, Pedestrian.GO_DOWN));
			else
				pedestrians.add(new Pedestrian(412, 0, Pedestrian.GO_DOWN));
			break;
		case Pedestrian.GO_UP:
			if(m==0)
				pedestrians.add(new Pedestrian(260, 700, Pedestrian.GO_UP));
			else
				pedestrians.add(new Pedestrian(412, 700, Pedestrian.GO_UP));
			break;
		case Pedestrian.GO_RIGHT:
			if(m==0)
				pedestrians.add(new Pedestrian(0, 265, Pedestrian.GO_RIGHT));
			else
				pedestrians.add(new Pedestrian(0, 410, Pedestrian.GO_RIGHT));
			break;
		case Pedestrian.GO_LEFT:
			if(m==0)
				pedestrians.add(new Pedestrian(700, 265, Pedestrian.GO_LEFT));
			else
				pedestrians.add(new Pedestrian(700, 410, Pedestrian.GO_LEFT));
			break;
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
		case MMKKM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(true, true, false);
			TU.setLamp(true);
			stateTime = Main.time[7];
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
		case MMKKM:
			US.setLamps(true, false, false);
			Barat.setLamps(true, true, false);
			Timur.setLamps(true, true, false);
			TU.setLamp(true);
			stateTime = Main.time[7];
			break;
		}
	}
	
	public void generateCar(){
		int i = rand.nextInt(2);
		int code = rand.nextInt(10);
		int type = rand.nextInt(4);
		if(type==0){
			if(i==0 || code==9)
				addCar(0-(Car.DEFAULT_WIDTH_L)-20, 305-(Car.DEFAULT_HEIGHT_L/2), Car.GO_RIGHT, code);
			else
				addCar(0-(Car.DEFAULT_WIDTH_L)-20, 332-(Car.DEFAULT_HEIGHT_L/2), Car.GO_RIGHT, code);
		}
		else if(type==1){
			if(i==0 || code ==9)
				addCar(700+(Car.DEFAULT_WIDTH_L/2), 367-(Car.DEFAULT_HEIGHT_L/2), Car.GO_LEFT, code);
			else
				addCar(700+(Car.DEFAULT_WIDTH_L/2), 393-(Car.DEFAULT_HEIGHT_L/2), Car.GO_LEFT, code);
		}
		else if(type==2){
			if(i==0 || code==9)
				addCar(305-(Car.DEFAULT_WIDTH_P/2), 700+(Car.DEFAULT_HEIGHT_P/2), Car.GO_UP, code);
			else
				addCar(332-(Car.DEFAULT_WIDTH_P/2), 700+(Car.DEFAULT_HEIGHT_P/2), Car.GO_UP, code);
		}
		else if(type==3){
			if(i==0 || code==9)
				addCar(367-(Car.DEFAULT_WIDTH_P/2), -(Car.DEFAULT_HEIGHT_P/2)-20, Car.GO_DOWN, code);
			else
				addCar(394-(Car.DEFAULT_WIDTH_P/2), -(Car.DEFAULT_HEIGHT_P/2)-20, Car.GO_DOWN, code);
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
