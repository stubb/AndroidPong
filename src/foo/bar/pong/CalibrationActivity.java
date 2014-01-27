package foo.bar.pong;

import java.util.Timer;
import java.util.TimerTask;

import singleton.Connector;
import constants.Values;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.*;

import foo.bar.pong.util.CollectDataThread;
import foo.bar.pong.util.CollectDataTimerTask;

public class CalibrationActivity extends Activity {
	int[] minValues = {100,200,300,400,42,100,700,851,912,192,931, 
            100,200,300,400,42,100,700,851,912,192};
	
	int[] maxValues = {500,100,400,200,420,10,70,51,912,192,931, 
     		10,20,30,40,42,10,70,851,92,912};
	
	
	public static int DURATION_MS = 250;
	private static final int HISTORY_SIZE = 20;            // number of points to plot in history
	
	private Context c;
	private XYPlot musclePlot = null;
	
	@SuppressWarnings({ "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration_layout);

		this.c = this.getApplicationContext();
        this.createPlot();
	};	
	
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
		Timer t = new Timer();
		this.musclePlot.clear();
		CollectDataTimerTask cdtt1 = new CollectDataTimerTask(this.musclePlot,
				true, this);
		// each measure has a duration of 5 seconds
		// 0000 ms toast
		// 1000 ms start (min)
		// 6000 ms end (min)
		// 6500 ms toast
		// 8000 ms start (max)
		//13000 ms end (max)
		Toast.makeText(c, "Relax muscles...", Toast.LENGTH_LONG).show();
		t.schedule(cdtt1,
				1000,
				DURATION_MS);
		Handler handler = new Handler();
		handler.postDelayed(
				new Runnable() {
					@Override
					public void run() {
						Toast.makeText(c, "Flex muscles...", Toast.LENGTH_LONG).show();
					}
				}, 6500);
		
		CollectDataTimerTask cdtt2 = new CollectDataTimerTask(this.musclePlot,
				false, this);
		t.schedule(cdtt2,
				8000,
				DURATION_MS);
		handler.postDelayed(
				new Runnable() {
					@Override
					public void run() {
						Toast.makeText(c, "Done!", Toast.LENGTH_LONG).show();
					}
				}, 13500);
	}
	
}
