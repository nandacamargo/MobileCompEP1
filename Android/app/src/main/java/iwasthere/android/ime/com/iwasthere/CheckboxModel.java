package iwasthere.android.ime.com.iwasthere;

import java.util.ArrayList;

/**
 * Created by nanda on 13/05/17.
 */

public class CheckboxModel {

    User user;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    CheckboxModel(User user, int value){
        this.user = user;
        this.value = value;
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

    public int getValue(){
        return this.value;
    }

    public int setValue(int value){
        return this.value = value;
    }

}
