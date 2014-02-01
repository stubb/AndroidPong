package foo.bar.pong.util;

import java.util.ArrayList;
import java.util.TimerTask;

import singleton.Connector;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import constants.Values;

/**
 * This class is responsible for getting the data from the connector singleton
 * and keeping it for the calculation of the mean of all measured values. It also
 * prints on the plot to visualize the collected data.
 */
public class CollectDataTimerTask extends TimerTask {

	public static int[] MIN_TEST = {10,20,30,40,4,10,70,85,91,19,93, 
            10,20,30,00,42,10,70,85,92,19};
	
	public static int[] MAX_TEST = {500,100,400,200,420,10,70,51,912,192,931, 
     		10,20,30,40,42,10,70,851,92,912};
	
	private Activity activity;
	private SharedPreferences settings;	
	private ArrayList<Integer> min;
	private ArrayList<Integer> max;
	private SimpleXYSeries minSeries;
	private SimpleXYSeries maxSeries;
	private XYPlot plot;
	
	private boolean minFlag;
	private int counter = 0;
	
	public CollectDataTimerTask(XYPlot plot, boolean minFlag, Activity activity) {
		this.activity = activity;
		this.settings = this.activity.getSharedPreferences(Values.CONFIG,
				Activity.MODE_PRIVATE);
		this.plot = plot;
		this.minFlag = minFlag;
		this.minSeries = new SimpleXYSeries("minimum");
		this.minSeries.useImplicitXVals();
		this.maxSeries = new SimpleXYSeries("maximum");
		this.maxSeries.useImplicitXVals();
		this.min = new ArrayList<Integer>();
		this.max = new ArrayList<Integer>();
		plot.addSeries(maxSeries,
        		new LineAndPointFormatter(Color.rgb(0, 255, 0),
        				Color.BLACK, null));
		plot.addSeries(minSeries,
        		new LineAndPointFormatter(Color.rgb(255, 0, 0),
        				Color.WHITE, null));				
	}
	
	@Override
	public void run() {
		int value = Connector.getInstance().getData();
		if(minFlag) {
			
			this.minSeries.addLast(null, value);
			this.min.add(value);
//			this.minSeries.addLast(null,MIN_TEST[counter]);
//			this.min.add(MIN_TEST[counter]);
		}
		else {
			this.maxSeries.addLast(null, value);
			this.max.add(value);
//			this.maxSeries.addLast(null,MAX_TEST[counter]);
//			this.max.add(MAX_TEST[counter]);
		}
		this.plot.redraw();
		counter++;
		if(counter==20) {
			this.saveConfig();
			this.cancel();
		}
	}
	
	/**
	 * write the mean of the collected minimum/maximum data to the application
	 * storage
	 */
	private synchronized void saveConfig() {
		Editor editor = this.settings.edit();
		if(minFlag) {
			editor.putInt(Values.CALIBRATED_MIN_VAL, this.getMean(this.min));
			Connector.getInstance().setMinimum(this.getMean(this.min));
			editor.commit();
		}
		else {
			editor.putInt(Values.CALIBRATED_MAX_VAL, this.getMean(this.max));
			Connector.getInstance().setMaximum(this.getMean(this.max));
			editor.commit();
			this.activity.setResult(Activity.RESULT_OK);
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.activity.finish();
		}
	}
	
	/**
	 * calculates the mean of an arraylist of given integer values and returns
	 * it
	 * @param values The values of which the mean is to be calculated
	 * @return the mean of the given values
	 */
	private int getMean(ArrayList<Integer> values) {
		int sum=0;
		for(int i=0; i<values.size(); i++) {
			sum+=values.get(i);
		}
		return sum/values.size();
	}

}
