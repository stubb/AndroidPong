package foo.bar.pong.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import singleton.Connector;

/**
 * This thread fetches highscoredata from the server. It simply downloads the
 * data and stores it into a String, this means you have to take care about
 * format and type by yourself.
 */
public class FetchHighscoreDataThread extends Thread {

	private URL url;
	private InputStream is = null;
	private BufferedReader br;
	public String data = "";
	private String chunk;

	/**
	 * Konstruktor
	 * 
	 * @param url
	 *            The url which provides data
	 */
	public FetchHighscoreDataThread(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the thread.
	 */
	public void run() {
		try {
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			while ((chunk = br.readLine()) != null) {
				data += chunk;
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				Connector.getInstance().setHighscoreData(data);
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}
}
