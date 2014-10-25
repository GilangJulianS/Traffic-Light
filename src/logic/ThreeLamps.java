package logic;

public class ThreeLamps extends OneLamp {
	
	private boolean yellow;
	private boolean green;

	public ThreeLamps() {
		super();
		yellow = false;
		green = false;
	}

	public ThreeLamps(boolean red, boolean yellow, boolean green) {
		super(red);
		this.yellow = yellow;
		this.green = green;
	}
	
	//Getter
	public boolean isYellow() {
		return yellow;
	}

	public boolean isGreen() {
		return green;
	}
	
	//Setter
	public void setLamps(boolean red, boolean yellow, boolean green) {
		super.setLamp(red);
		this.yellow = yellow;
		this.green = green;
	}
	
}
