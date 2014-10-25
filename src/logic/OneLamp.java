package logic;

public class OneLamp {

	private boolean red;
	
	public OneLamp() {
		red = true;
	}
	
	public OneLamp(boolean red) {
		this.red = red;
	}

	//Getter
	public boolean isRed() {
		return red;
	}

	//Setter
	public void setLamp(boolean red) {
		this.red = red;
	}

}
