package singleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Vector;

import constants.Values;

public class Connector {

	private static int RANGE_MIN = 1;
	private static int RANGE_MAX = 50;
	
	private static Connector instance = null;

	private Integer data = 0;

	private int minimum;
	private int maximum;
	
	/**
	 * Thread for handling network stuff.
	 */
	private Thread networkThread;

	private Connector() {
		networkThread = new Thread() {
			public void run() {
				try {
					while (true) {
						byte[] message = new byte[2];
						DatagramPacket p = new DatagramPacket(message,
								message.length);
						DatagramSocket s = new DatagramSocket(
								Values.SERVER_PORT);
						if (isInterrupted()) {
							s.close();
							this.join();
						}
						if (!s.isClosed()) {
							s.receive(p);
						}

						int sum = (message[1] & 0xFF) << 8;
						sum |= (message[0] & 0xFF);
						//System.out.println("sum: " + sum);

						data = Integer.valueOf(sum);

						if (!s.isClosed()) {
							s.close();
						}
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	public static Connector getInstance() {
		if (instance == null) {
			instance = new Connector();
		}
		return instance;
	}

	public boolean hasConnection() {
		return networkThread.isAlive();
	}
	
	public void killConnection() {
		if(networkThread != null) {
			networkThread.interrupt();
			networkThread = null;
		}		
	}
	
	public void startConnection() {
		if(networkThread != null && !networkThread.isAlive()) {
			networkThread.start();
		}
	}
	
	public Integer getData() {
		return data;
	}
	
	public int getMappedData() {
		int mappedData = 0;
		if (this.maximum != 0 && this.minimum != 0) {
			mappedData = Math.round(((float)(this.data - this.minimum) / (float)(this.maximum - this.minimum)) * (RANGE_MAX - RANGE_MIN) + RANGE_MIN);
		}
		return mappedData; 
	}

}
