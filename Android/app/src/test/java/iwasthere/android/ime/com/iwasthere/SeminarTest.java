package iwasthere.android.ime.com.iwasthere;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by nanda on 17/05/17.
 */
public class SeminarTest {

    private Seminar seminar, seminar2;

    @Before
    public void setUp() throws Exception {
        seminar = new Seminar("MySeminar", 1);
        seminar2 = new Seminar("MySeminar2", 2);

    }

    @After
    public void tearDown() throws Exception {
        seminar = null;
        assertNull(seminar);

        seminar2 = null;
        assertNull(seminar2);
    }

    @Test
    public void getName() throws Exception {

        assertEquals(seminar.getName(), "MySeminar");

        assertEquals(seminar2.getName(), "MySeminar2");
    }

    @Test
    public void getId() throws Exception {

        Integer id = 1;
        assertEquals(seminar.getId(), id);

        id = 2;
        assertEquals(seminar2.getId(), id);
    }


    @Test
    public void setName() throws Exception {

        seminar.setName("Another");
        assertEquals("Another", seminar.getName());

        seminar.setName("Powerful");
        assertEquals("Powerful", seminar.getName());
    }

    @Test
    public void setId() throws Exception {

        Integer id = 1;
        seminar.setId(id);
        assertEquals(id, seminar.getId());

        id = 2;
        seminar.setId(id);
        assertEquals(id, seminar.getId());

    }

    @Test
    public void testToString() throws Exception {

        assertEquals("Name: MySeminar2, ID: 2", seminar2.toString());

    }

}