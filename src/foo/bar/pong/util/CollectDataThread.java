package foo.bar.pong.util;

import java.util.ArrayList;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class CollectDataThread extends Thread {
	
	public static int DURATION_MS = 250;
	
	public static int[] MIN_TEST = {100,200,300,400,42,100,700,851,912,192,931, 
            100,200,300,400,42,100,700,851,912,192};
	
	public static int[] MAX_TEST = {500,100,400,200,420,10,70,51,912,192,931, 
     		10,20,30,40,42,10,70,851,92,912};
	
	private ArrayList<Integer> min;
	private ArrayList<Integer> max;
	private SimpleXYSeries minSeries;
	private SimpleXYSeries maxSeries;
	private XYPlot plot;
	
	public CollectDataThread(XYPlot plot, SimpleXYSeries minSeries,
			SimpleXYSeries maxSeries) {
		this.minSeries = minSeries;
		this.maxSeries = maxSeries;
		this.plot = plot;
	}
	
	@Override
	public void run() {
		for(int i=0;i<MAX_TEST.length; i++) {
			this.minSeries.addLast(null, MIN_TEST[i]);
			this.maxSeries.addLast(null, MAX_TEST[i]);
			this.plot.redraw();
			try {
                synchronized (this) {
                	Thread.sleep(DURATION_MS);
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
