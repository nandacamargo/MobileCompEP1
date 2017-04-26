package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dududcbier on 26/04/17.
 */

public class SeminarsAdapter extends ArrayAdapter<Seminar> {
    public SeminarsAdapter(Context context, ArrayList<Seminar> seminars) {
        super(context, 0, seminars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Seminar seminar = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seminar_list, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.seminar_name);
        name.setText(seminar.getName());
        return convertView;
    }
}
