package foo.bar.pong;

import constants.Values;
import singleton.Connector;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetric;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.*;

public class CalibrationActivity extends Activity {
	
	private static final int HISTORY_SIZE = 20;            // number of points to plot in history
	
	private TextView minTV;
	private TextView maxTV;
	private SharedPreferences settings;
	private int min;
	private int max;
	
	private XYPlot musclePlot = null;
	private SimpleXYSeries minMuscleSeries = null;
	private SimpleXYSeries maxMuscleSeries = null;
	
	@SuppressWarnings({ "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration_layout);
		
        this.createPlot();
        fillPlot();
	}
	
	public void createPlot() {
		musclePlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
        minMuscleSeries = new SimpleXYSeries("minimum");
        minMuscleSeries.useImplicitXVals();
        maxMuscleSeries = new SimpleXYSeries("maximum");
        maxMuscleSeries.useImplicitXVals();
        musclePlot.setRangeBoundaries(0, 1000, BoundaryMode.FIXED);
        musclePlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        musclePlot.getGraphWidget().setPadding(
        		PixelUtils.dpToPix(15),
        		PixelUtils.dpToPix(20),
        		0,
        		PixelUtils.dpToPix(30));
        musclePlot.addSeries(maxMuscleSeries,
        		new LineAndPointFormatter(Color.rgb(0, 255, 0),
        				Color.BLACK, null));
        musclePlot.addSeries(minMuscleSeries,
        		new LineAndPointFormatter(Color.rgb(255, 0, 0),
        				Color.WHITE, null));
        musclePlot.setDomainStepValue(10);
        musclePlot.setTicksPerRangeLabel(1);
        musclePlot.setDomainLabel("time");
        musclePlot.getDomainLabelWidget().getLabelPaint().
        	setTextSize(PixelUtils.dpToPix(16));
        musclePlot.getDomainLabelWidget().pack();
        musclePlot.setRangeLabel("muscle tenseness");
        musclePlot.getRangeLabelWidget().getLabelPaint().
        	setTextSize(PixelUtils.dpToPix(16));
        musclePlot.getRangeLabelWidget().pack();
        musclePlot.getLegendWidget().getTextPaint().setTextSize(
        		PixelUtils.dpToPix(16));
	}
	
	public synchronized void fillPlot()	{
		 int[] test0 = {100,200,300,400,42,100,700,851,912,192,931, 
		               100,200,300,400,42,100,700,851,912,192};
		 int[] test1 = {500,100,400,200,420,10,70,51,912,192,931, 
	               10,20,30,40,42,10,70,851,92,912};
		 for(int i=0; i<test0.length; i++) {
			 minMuscleSeries.addLast(null, test0[i]);
			 maxMuscleSeries.addLast(null, test1[i]);
			 musclePlot.redraw();
		 } 
	}
	
	public synchronized void runCalibration(View view) {
//		((LinearLayout) findViewById(R.id.calibrationInPorgressLayout)).
//			setVisibility(View.VISIBLE);
//		this.startCalibration(false);
//		this.startCalibration(true);
//		this.writeCalibratedValues();
//		((LinearLayout) findViewById(R.id.calibrationInPorgressLayout)).
//			setVisibility(View.GONE);
	}
	
	private synchronized void startCalibration(boolean max) {
		Integer[] results = new Integer[10];
		for(int i=0; i<10; i++) {
			results[i] = calibrate(max);
			System.out.println("Debug: "+results[i]);
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(max) {
			this.max = this.getAverage(results);
			this.maxTV.setText(String.valueOf(this.max));
		}
		else {
			this.min = this.getAverage(results);
			this.minTV.setText(String.valueOf(this.min));
		}
	}
	
	private int calibrate(boolean max) {
		if(max) {
			return Connector.getInstance().getMax();
		}
		else {
			return Connector.getInstance().getMin();
		}
	}
	
	private int getAverage(Integer[] values) {
		int sum = 0;
		for(int i=0; i<values.length; i++) {
			sum += values[i];
		}
		return (Math.round(sum/values.length));
	}
	
	private void writeCalibratedValues() {
		Editor editor = this.settings.edit();
		editor.putInt(Values.CALIBRATED_MIN_VAL, this.min);
		editor.putInt(Values.CALIBRATED_MAX_VAL, this.max);
		editor.commit();
	}

}
