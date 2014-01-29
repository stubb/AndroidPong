package foo.bar.pong.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.google.gson.Gson;

public class FetchHighscoreDataThread extends Thread {
	
         private URL url;
         private InputStream is = null;
         private BufferedReader br;
         public String data;
         private String chunk;
         public Boolean ready;
         private String[][] plainData;

     	public FetchHighscoreDataThread(String url, String data, Boolean isReady) {
    		try {
    			ready = isReady;
				this.url = new URL(url);
				//System.out.println(url);
				this.data = data;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
     	
    	public void run() {
         try {
             is = url.openStream();  // throws an IOException
             //System.out.println(url.toString());
             br = new BufferedReader(new InputStreamReader(is));
             while ((chunk = br.readLine()) != null) {
                 data += chunk;
                 //System.out.println(data);
             }
         } catch (MalformedURLException mue) {
              mue.printStackTrace();
         } catch (IOException ioe) {
              ioe.printStackTrace();
         } finally {
             try {
                 if (is != null) is.close();
             } catch (IOException ioe) {
                 // nothing to see here
             }
             //System.out.println("DATA: " + data);
             ready = true;
         }
    	}
         
         public String getData() {
        	 return data;
         }
}
