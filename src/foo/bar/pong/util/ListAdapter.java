package foo.bar.pong.util;

import foo.bar.pong.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The self implemented list adapter to fill the high-score lists
 */
public class ListAdapter extends ArrayAdapter<String[]> {

	private final Context context;
	private final String[][] values;
	private boolean expert;
	
	public ListAdapter(Context context, String[][] values, boolean expert) {
		super(context, R.layout.listitem, values);
		this.context = context;
	    this.values = values;
	    this.expert = expert;
	}
	
	// example data
	// {"id":9,"username":"Horsti69","highscorenormalmode":27,"date":"Jan 11, 2014","time":"12:01:05 PM"}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listitem, parent, false);
	    TextView posView = (TextView) rowView.findViewById(R.id.tvPos);
	    TextView nameView = (TextView) rowView.findViewById(R.id.tvName);
	    TextView pointView = (TextView) rowView.findViewById(R.id.tvPoints);
    	posView.setText(" "+String.valueOf(position+1)+":");
	    nameView.setText(this.values[position][1]);
	    if(this.expert) {
	    	pointView.setText(this.values[position][2]+" sec " + "(" + this.values[position][3] + " " + this.values[position][4] + ")");
	    }
	    else {
	    	pointView.setText(this.values[position][2]+" turns " + "(" + this.values[position][3] + " " + this.values[position][4] + ")");
	    }
	    return rowView;
    }
	
}
