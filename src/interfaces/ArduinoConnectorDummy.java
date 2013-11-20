package interfaces;

public class ArduinoConnectorDummy implements ArduinoConnectable {

	@Override
	public boolean hasConnection() {
		return true;
	}

	@Override
	public int getMinValue() {
		return 60;
	}

	@Override
	public int getMaxValue() {
		return 420;
	}

}
