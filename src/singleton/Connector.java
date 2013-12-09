package singleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Vector;

import constants.Values;

public class Connector {

	private static Connector instance = null;

	public Vector<Integer> data = new Vector<Integer>();

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
						System.out.println("sum: " + sum);

						data.add(Integer.valueOf(sum));

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
		if(!networkThread.isAlive()) {
			networkThread.start();
		}
	}
	
	public Integer getLastData() {
		return data.lastElement();
	}
	
	public Integer getMax() {
		Integer max = Integer.MIN_VALUE;
		for (Integer curr : data) {
	        if (curr > max) {
	            max = curr;
	        }
	    }
		return max;
	}
	
	public Integer getMin() {
		Integer min = Integer.MAX_VALUE;
		for (Integer curr : data) {
	        if (curr < min) {
	            min = curr;
	        }
	    }
		return min;
	}

}
