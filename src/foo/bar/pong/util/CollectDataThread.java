package foo.bar.pong.util;

import java.util.ArrayList;

import com.androidplot.xy.XYPlot;

import singleton.Connector;

public class CollectDataThread extends Thread {
	
	ArrayList<Integer> min;
	ArrayList<Integer> max;
	
	public CollectDataThread(XYPlot plot) {
		int[] minValues = {100,200,300,400,42,100,700,851,912,192,931, 
	               100,200,300,400,42,100,700,851,912,192};
		
		int[] maxValues = {500,100,400,200,420,10,70,51,912,192,931, 
            		10,20,30,40,42,10,70,851,92,912};
	}
	
	
	
}
