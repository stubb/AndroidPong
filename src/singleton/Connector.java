package singleton;

import interfaces.ArduinoConnectable;
import interfaces.ArduinoConnectorDummy;

public class Connector {

	private static Connector instance = null;
	private static ArduinoConnectable connectable;
	
	private Connector() {
		connectable = new ArduinoConnectorDummy();
	}
	
	public static Connector getInstance() {
	      if(instance == null) {
	         instance = new Connector();
	      }
	      return instance;
	   }
	
	public boolean hasConnection() {
		return true;
	}
	
	public int getMax() {
		return Connector.connectable.getMaxValue();
	}
	
	public int getMin() {
		return Connector.connectable.getMinValue();
	}
}
