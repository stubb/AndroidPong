package foo.bar.pong.util;

import foo.bar.pong.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listitem, parent, false);
	    TextView posView = (TextView) rowView.findViewById(R.id.tvPos);
	    TextView nameView = (TextView) rowView.findViewById(R.id.tvName);
	    TextView pointView = (TextView) rowView.findViewById(R.id.tvPoints);
    	posView.setText(" "+String.valueOf(position+1)+":");
	    nameView.setText(this.values[position][0]);
	    if(this.expert) {
	    	pointView.setText(this.values[position][1]+" turns");
	    }
	    else {
	    	pointView.setText(this.values[position][1]+" minutes");
	    }
	    return rowView;
    }
	
}
