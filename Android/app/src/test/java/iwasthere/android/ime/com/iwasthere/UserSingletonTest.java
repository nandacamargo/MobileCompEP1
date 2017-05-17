package iwasthere.android.ime.com.iwasthere;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nanda on 17/05/17.
 */
public class UserSingletonTest {

    private User user, user2;
    private User user_singleton;


    @Before
    public void setUp() throws Exception {
        user = new User("1234", "First", false);
        user2 = new User("5678", "Second", true);

        user_singleton = UserSingleton.getInstance("First","1234", false);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
        user2 = null;
        assertNull(user);
        assertNull(user2);
    }

    @Test
    public void getInstance_3params() throws Exception {

        assertEquals(user.getName(), user_singleton.getName());
        assertEquals(user.getNusp(), user_singleton.getNusp());
        assertEquals(user.isTeacher(), user_singleton.isTeacher());
    }

}