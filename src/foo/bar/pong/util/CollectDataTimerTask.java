package foo.bar.pong.util;

import java.util.ArrayList;
import java.util.TimerTask;

import singleton.Connector;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class CollectDataTimerTask extends TimerTask {

	public static int[] MIN_TEST = {100,200,300,400,42,100,700,851,912,192,931, 
            100,200,300,400,42,100,700,851,912,192};
	
	public static int[] MAX_TEST = {500,100,400,200,420,10,70,51,912,192,931, 
     		10,20,30,40,42,10,70,851,92,912};
	
	private ArrayList<Integer> min;
	private ArrayList<Integer> max;
	private SimpleXYSeries minSeries;
	private SimpleXYSeries maxSeries;
	private XYPlot plot;
	private Context context;
	
	private int counter = 0;
	
	public CollectDataTimerTask(XYPlot plot, Context context) {
		this.context = context;
		this.plot = plot;
		this.minSeries = new SimpleXYSeries("minimum");
		this.minSeries.useImplicitXVals();
		this.maxSeries = new SimpleXYSeries("maximum");
		this.maxSeries.useImplicitXVals();
		plot.addSeries(maxSeries,
        		new LineAndPointFormatter(Color.rgb(0, 255, 0),
        				Color.BLACK, null));
		plot.addSeries(minSeries,
        		new LineAndPointFormatter(Color.rgb(255, 0, 0),
        				Color.WHITE, null));
		
	}
	
	private AlertDialog buildAlertDialog(boolean min) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle("Calibration (Minimum)");
 
		// set dialog message
		alertDialogBuilder
			.setMessage("Calibration for minimum is about to start!")
			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		return alertDialog;
	}
	
	@Override
	public void run() {
		int value = Connector.getInstance().getData();
		if(counter == 0) {
//			this.buildAlertDialog(true);
		}
		if(counter<20) {
			this.minSeries.addLast(null, value);
			this.min.add(value);
//			this.minSeries.addLast(null,MIN_TEST[counter]);
//			this.min.add(MIN_TEST[counter]);
		}
		else {
			this.maxSeries.addLast(null, value);
			this.max.add(value);
//			this.maxSeries.addLast(null,MAX_TEST[counter-20]);
//			this.max.add(MAX_TEST[counter]);
		}
		this.plot.redraw();
		counter++;
		if(counter==40) {
			this.saveConfig();
			this.cancel();
		}
	}
	
	private void saveConfig() {
		Connector.getInstance().setMinimum(this.getMean(this.min));
		Connector.getInstance().setMaximum(this.getMean(this.max));
	}
	
	private int getMean(ArrayList<Integer> values) {
		int sum=0;
		for(int i=0; i<values.size(); i++) {
			sum+=values.get(i);
		}
		return sum/values.size();
	}

}
