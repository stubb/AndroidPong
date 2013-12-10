package singleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
				
					while (true) {
						try {
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
						
						// store data
						data.add(Integer.valueOf(sum));
						
						// send data to gamethread
						Message msg = Message.obtain();
						msg.what = 999;
						msg.obj = sum;
						gameengine.GameThread.msgHandler.sendMessage(new Message());
						

						if (!s.isClosed()) {
							s.close();
						}
					
				} catch (SocketException e) {
					// TODO this sends trashvalues ...
					// send data to gamethread
					Message msg = Message.obtain();
					msg.what = 999;
					msg.obj = 1;
					gameengine.GameThread.msgHandler.sendMessage(new Message());
					e.printStackTrace();
				} catch (IOException e) {
					Message msg = Message.obtain();
					msg.what = 999;
					msg.obj = 2;
					gameengine.GameThread.msgHandler.sendMessage(new Message());
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					Message msg = Message.obtain();
					msg.what = 999;
					msg.obj = 3;
					gameengine.GameThread.msgHandler.sendMessage(new Message());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
