package interfaces;

import java.util.Random;

public class ArduinoConnectorDummy implements ArduinoConnectable {

	private Random rnd;
	
	public ArduinoConnectorDummy() {
		rnd = new Random();
	}
	
	@Override
	public boolean hasConnection() {
		return true;
	}

	@Override
	public int getMinValue() {
		return rnd.nextInt(20)+50;
	}

	@Override
	public int getMaxValue() {
		return rnd.nextInt(60)+380;
	}

}
