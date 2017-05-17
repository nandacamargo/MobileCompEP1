package iwasthere.android.ime.com.iwasthere;

import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by nanda on 13/05/17.
 */

public class CheckboxModel {

    User user;
    CheckBox checkbox; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    CheckboxModel(User user, CheckBox checkbox){
        this.user = user;
        this.checkbox = checkbox;
    }

    /*public ArrayList<User> getUsers() { return  this.users; }

    public ArrayList<Integer> getValues() { return  this.values; }*/

    public User getUser() {
        return this.user;
    }

    public String getName() {
        return this.user.getName();
    }

    public String getNusp() {
        return this.user.getNusp();
    }

    public boolean getIsChecked(){
        return this.checkbox.isChecked();
    }



}
