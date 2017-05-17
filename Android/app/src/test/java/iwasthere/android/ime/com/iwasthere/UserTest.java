package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

/**
 * Created by nanda on 17/05/17.
 */


public class UserTest {

    private User user, user2;
    private User user3, user4;

    @Before
    public void setUp() throws Exception {
        user = new User("1234", "First", false, false);
        user2 = new User("5678", "Second", true, true);

        user3 = new User("1234", "SmartGuy", false, false);
        user4 = new User("1234", "SmartGuy", false, false);

    }

    @After
    public void tearDown() throws Exception {

        user = null;
        assertNull(user);
    }

    @Test
    public void getName() throws Exception {

        assertEquals("First", user.getName());

    }

    @Test
    public void setName() throws Exception {

        user.setName("Another");
        final Field field = user.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("Fields match", field.get(user), "Another");

        user.setName("MyUser");
        assertEquals("MyUser", user.getName());
    }

    @Test
    public void getNusp() throws Exception {

        assertEquals("1234", user.getNusp());
    }

    @Test
    public void isTeacher() throws Exception {

        assertEquals(false, user.isTeacher());

        assertEquals(true, user2.isTeacher());

    }

    @Test
    public void isSelected() throws Exception {

        assertEquals(false, user.isSelected());

        assertEquals(true, user2.isSelected());
    }

    @Test
    public void setSelected() throws Exception {

        boolean selected = true;
        user.setSelected(selected);
        assertEquals(true, user.isSelected());

        boolean selected2 = false;
        user.setSelected(selected2);
        assertEquals(false, user.isSelected());

    }


    @Test
    public void equals() throws Exception {

        assertFalse(user.equals(user2));

        assertTrue(user3.equals(user4));
    }

}