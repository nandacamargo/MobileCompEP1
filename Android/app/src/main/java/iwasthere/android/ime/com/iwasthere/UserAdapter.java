package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;

/**
 * Created by nanda on 13/05/17.
 */

public class UserAdapter extends ArrayAdapter<CheckboxModel> {
    CheckboxModel[] modelItems = null;
    Context context;

    public UserAdapter(Context context, CheckboxModel[] resource) {

        super(context,R.layout.row,resource);

        this.context = context;
        this.modelItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        name.setText(modelItems[position].getName());
        /*if(modelItems[position].getValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);*/
        return convertView;
    }
}



