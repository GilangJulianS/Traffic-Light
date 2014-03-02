package Swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Christ.Main;
import Christ.ThreeLamps;
import Christ.World;
import TL.Car;
import TL.Pedestrian;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable,MouseListener, MouseMotionListener,KeyListener{

	
	private static long BUTTON_DELAY_TIME = 2000;
	private AffineTransform left, down, right, top, single;
	private Image bg, merah, merahkuning, hijau, singleOn, singleOff, button, pushButton, pedestrian;
	private Image[] carLeft, carRight, carUp, carDown;
	private Rectangle boundLeft, boundRight, boundTop, boundDown;
	private World world;
	private long lastUpdate, elapsedTime;
	private long tempTime, timeToChange;
	private boolean unset, hoverButton, buttonPushed, waitToRed;
	private Rectangle buttonArea;
	private List<Car> carList;
	private Car car;
	private Graphics g;
	private Random random;
	private List<Pedestrian> p;
	private static JFrame frame;
	
	public Panel() {
		init();
		Thread thread = new Thread(this);
		Main.main(null);
		while(Main.time == null){
			//do nothing
		}
		thread.start();
	}

	public void init(){
		setSize(800, 600);
		setBackground(Color.white);
		carLeft = new Image[10];
		carRight = new Image[10];
		carUp = new Image[10];
		carDown = new Image[10];
		String base = "/resource/";
		try {
			bg = ImageIO.read(getClass().getResource(base+"BackgroundTBO.png"));
			merah = ImageIO.read(getClass().getResource(base+ "m.png"));
			merahkuning = ImageIO.read(getClass().getResource(base+ "mk.png"));
			hijau = ImageIO.read(getClass().getResource(base+"h.png"));
			singleOn = ImageIO.read(getClass().getResource(base+ "sm.png"));
			singleOff = ImageIO.read(getClass().getResource(base+ "sw.png"));
			for(int i=1; i<11; i++){
				carRight[i-1] = ImageIO.read(getClass().getResource(base+"carl"+i+".png"));
				carLeft[i-1] = ImageIO.read(getClass().getResource(base+"carr"+i+".png"));
				carDown[i-1] = ImageIO.read(getClass().getResource(base+"caru"+i+".png"));
				carUp[i-1] = ImageIO.read(getClass().getResource(base+"card"+i+".png"));
			}
			button = ImageIO.read(getClass().getResource(base+ "Button.png"));
			pushButton = ImageIO.read(getClass().getResource(base+ "PushButton.png"));
			pedestrian = ImageIO.read(getClass().getResource(base+ "Pedestrian.png"));
		} catch (IOException e) {
			System.out.println("file bg not found");
		}
		
		left = new AffineTransform();
		down = new AffineTransform();
		right = new AffineTransform();
		top = new AffineTransform();
		single = new AffineTransform();
		boundLeft = new Rectangle(0, 290, 290, 120);
		boundRight = new Rectangle(410, 290, 290, 120);
		boundTop = new Rectangle(290, 0, 120, 290);
		boundDown = new Rectangle(290, 410, 120, 290);
		world = new World();
		carList = new ArrayList<Car>();
		buttonArea = new Rectangle(100, 480, 100, 100);
		random = new Random();
		unset = true;
		hoverButton = false;
		buttonPushed = false;
		waitToRed = false;
		tempTime = 0;
		timeToChange = BUTTON_DELAY_TIME;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		frame = new JFrame("Mini Tennis");
		Panel game = new Panel();
		frame.add(game);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2-20);
        frame.setVisible(true);
	}
	
	public void update(Graphics g){
		elapsedTime = System.currentTimeMillis() - lastUpdate;
		lastUpdate = System.currentTimeMillis();
		world.update(elapsedTime);
		if(waitToRed){
			tempTime += elapsedTime;
			if(tempTime >= timeToChange){
				tempTime = 0;
				waitToRed = false;
				world.suddenChange(World.State.MMKKM);
			}
		}
		if(unset && merah.getWidth(null)!=-1){
			double centerx = (merah.getWidth(null)/2) *0.7;
			double centery = (merah.getHeight(null)/2) *0.7;
			left.translate(200, 203);
			left.rotate(Math.toRadians(90), centerx, centery);
			left.scale(0.7, 0.7);
			right.translate(474, 443);
			right.rotate(Math.toRadians(270), centerx, centery);
			right.scale(0.7, 0.7);
			down.translate(225, 441);
			down.scale(0.7, 0.7);
			top.translate(445, 177);
			top.scale(0.7, 0.7);
			single.translate(447, 443);
			single.scale(0.7, 0.7);
			unset = false;
		}
		
	}
	
	
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.g = g;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		ThreeLamps lamp;
		g2d.drawImage(bg, 0, 0, 700, 700, 0, 0, bg.getWidth(null), bg.getHeight(null), this);
		
		//g2d.drawImage(carUp, 300, 300, this);
		
		lamp = world.getUSLamp();
		if(lamp.isGreen()){
			g2d.drawImage(hijau, top, this);
			g2d.drawImage(hijau, down, this);
		}
		else if(lamp.isYellow()){
			g2d.drawImage(merahkuning, top, this);
			g2d.drawImage(merahkuning, down, this);
		}
		else if(lamp.isRed()){
			g2d.drawImage(merah, top, this);
			g2d.drawImage(merah, down, this);
		}
		
		lamp = world.getBaratLamp();
		if(lamp.isGreen())
			g2d.drawImage(hijau, left, this);
		else if(lamp.isYellow())
			g2d.drawImage(merahkuning, left, this);
		else if(lamp.isRed())
			g2d.drawImage(merah, left, this);
		
		lamp = world.getTimurLamp();
		if(lamp.isGreen())
			g2d.drawImage(hijau, right, this);
		else if(lamp.isYellow())
			g2d.drawImage(merahkuning, right, this);
		else if(lamp.isRed())
			g2d.drawImage(merah, right, this);
		
		if(world.getTULamp().isRed())
			g2d.drawImage(singleOn, single, this);
		else
			g2d.drawImage(singleOff, single, this);
		
		if(hoverButton)
			g2d.drawImage(pushButton, buttonArea.x, buttonArea.y, buttonArea.x+buttonArea.width, buttonArea.y+buttonArea.height,
					0, 0, pushButton.getWidth(null), pushButton.getHeight(null), this);
		else
			g2d.drawImage(button, buttonArea.x, buttonArea.y, buttonArea.x+buttonArea.width, buttonArea.y+buttonArea.height,
					0, 0, button.getWidth(null), button.getHeight(null), this);
		
		carList = world.carsRight;
		for(int i=0; i<carList.size(); i++){
			if(carList.get(i)!=null){	
				car = carList.get(i);
				car.draw(g2d, carRight[car.imageCode], this);
			}
		}
		carList = world.carsLeft;
		for(int i=0; i<carList.size(); i++){
			if(carList.get(i) !=null){
				car = carList.get(i);
				car.draw(g2d, carLeft[car.imageCode], this);
			}
		}
		carList = world.carsTop;
		for(int i=0; i<carList.size(); i++){
			if(carList.get(i)!=null){
				car = carList.get(i);
				car.draw(g2d, carUp[car.imageCode], this);
			}
		}
		carList = world.carsBottom;
		for(int i=0; i<carList.size(); i++){
			if(carList.get(i)!=null){
				car = carList.get(i);
				car.draw(g2d, carDown[car.imageCode], this);
			}
		}
		p = world.pedestrians;
		for(int i=0; i<p.size(); i++){
			p.get(i).draw(g2d, pedestrian, this);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		System.out.println(event.getX() + " " + event.getY());
		int x, y;
		x = event.getX();
		y = event.getY();
		if(buttonArea.contains(x, y))
			hoverButton = true;
		else
			hoverButton = false;
		if(boundLeft.contains(x, y) || boundRight.contains(x, y) || boundTop.contains(x, y) || boundDown.contains(x, y) || hoverButton)
			frame.setCursor(Cursor.HAND_CURSOR);
		else
			frame.setCursor(Cursor.DEFAULT_CURSOR);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		int i = random.nextInt(2);
		int code = random.nextInt(10);
		int x, y;
		x = event.getX();
		y = event.getY();
		if(boundLeft.contains(x, y)){
			if(i==0 || code==9)
				world.addCar(0-(Car.DEFAULT_WIDTH_L)-20, 305-(Car.DEFAULT_HEIGHT_L/2), Car.GO_RIGHT, code);
			else
				world.addCar(0-(Car.DEFAULT_WIDTH_L)-20, 332-(Car.DEFAULT_HEIGHT_L/2), Car.GO_RIGHT, code);
		}
		else if(boundRight.contains(x, y)){
			if(i==0 || code ==9)
				world.addCar(700+(Car.DEFAULT_WIDTH_L/2), 367-(Car.DEFAULT_HEIGHT_L/2), Car.GO_LEFT, code);
			else
				world.addCar(700+(Car.DEFAULT_WIDTH_L/2), 393-(Car.DEFAULT_HEIGHT_L/2), Car.GO_LEFT, code);
		}
		else if(boundDown.contains(x, y)){
			if(i==0 || code==9)
				world.addCar(305-(Car.DEFAULT_WIDTH_P/2), 700+(Car.DEFAULT_HEIGHT_P/2), Car.GO_UP, code);
			else
				world.addCar(332-(Car.DEFAULT_WIDTH_P/2), 700+(Car.DEFAULT_HEIGHT_P/2), Car.GO_UP, code);
		}
		else if(boundTop.contains(x, y)){
			if(i==0 || code==9)
				world.addCar(367-(Car.DEFAULT_WIDTH_P/2), -(Car.DEFAULT_HEIGHT_P/2)-20, Car.GO_DOWN, code);
			else
				world.addCar(394-(Car.DEFAULT_WIDTH_P/2), -(Car.DEFAULT_HEIGHT_P/2)-20, Car.GO_DOWN, code);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if(hoverButton && !waitToRed) {
			if (world.state == World.State.HHMMM) {
				waitToRed = true;
			}
			tempTime = 0;
			buttonPushed = true;
			hoverButton = false;
			world.addPedestrian();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(buttonPushed){
			buttonPushed = false;
			hoverButton = true;
		}
	}

	@Override
	public void run() {
		while(true){
			repaint();
			update(g);
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent key) {
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.out.println("escape");
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}

}
