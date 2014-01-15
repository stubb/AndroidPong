package foo.bar.pong;

import java.util.Timer;
import java.util.TimerTask;

import constants.Values;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.*;

import foo.bar.pong.util.CollectDataThread;
import foo.bar.pong.util.CollectDataTimerTask;

public class CalibrationActivity extends Activity {
	
	public static int DURATION_MS = 250;
	
	private static final int HISTORY_SIZE = 20;            // number of points to plot in history
	
	private TextView minTV;
	private TextView maxTV;
	private SharedPreferences settings;
	private int min;
	private int max;
	
	private XYPlot musclePlot = null;
	
	@SuppressWarnings({ "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration_layout);
		
        this.createPlot();
	}
	
	public void createPlot() {
		musclePlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
        
        musclePlot.setRangeBoundaries(0, 500, BoundaryMode.GROW);
        musclePlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        musclePlot.getGraphWidget().setPadding(
        		PixelUtils.dpToPix(15),
        		PixelUtils.dpToPix(20),
        		0,
        		PixelUtils.dpToPix(30));
        
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
	
	public void runCalibration(View view) {
//		CollectDataThread collector = new CollectDataThread(musclePlot, minMuscleSeries,
//				maxMuscleSeries);
//		collector.run();
		
		Timer t = new Timer();
		this.musclePlot.clear();
		t.schedule(new CollectDataTimerTask(this.musclePlot, this),
				0,
				250);
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
