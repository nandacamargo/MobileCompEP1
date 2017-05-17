package iwasthere.android.ime.com.iwasthere;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by nanda on 17/05/17.
 */
public class SeminarSingletonTest {

    private Seminar seminar;
    private Seminar seminar2;
    Integer id = 1;
    Integer id2 = 2;

    private Seminar sem_singleton;


    @Before
    public void setUp() throws Exception {
        seminar = new Seminar("Cool", id);
        seminar2 = new Seminar("Nice seminar", id2);

        sem_singleton = SeminarSingleton.getInstance("Cool", id);
    }

    @After
    public void tearDown() throws Exception {
        seminar = null;
        assertNull(seminar);
    }

    @Test
    public void getInstance() throws Exception {

        assertEquals(seminar.getName(), sem_singleton.getName());
        assertEquals(seminar.getId(), sem_singleton.getId());
    }

    @Test
    public void updateInstance() throws Exception {

        sem_singleton = SeminarSingleton.updateInstance("Nice seminar", id2);

        assertEquals(seminar2.getName(), sem_singleton.getName());
        assertEquals(seminar2.getId(), sem_singleton.getId());
    }

}