package domains;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TemaTest {
    Tema t1;
    StructuraAnUniversitar structuraAnUniversitar;

    @Before
    public void setUp() throws Exception {
        t1 = new Tema("MAP", 4, 6);
        t1.setId("1");
    }

    @Test
    public void getDescriere() {
        assertEquals("MAP", t1.getDescriere());
    }

    @Test
    public void setDescriere() {
        t1.setDescriere("PLF");
        assertEquals("PLF", t1.getDescriere());
    }

    @Test
    public void getStartWeek() {
        assertEquals("Metoda getStartWeek a crapat!", 4, t1.getStartWeek());
    }

    @Test
    public void setStartWeek() {
        t1.setStartWeek(5);
        assertEquals("Metoda setStartWeek a crapat!", 5, t1.getStartWeek());
    }

    @Test
    public void getDeadlineWeek() {
        assertEquals("Metoda getDeadlineWeek a crapat!", 6, t1.getDeadlineWeek());
    }

    @Test
    public void setDeadlineWeek() {
        t1.setDeadlineWeek(14);
        assertEquals("Metoda setDeadlineWeek a crapat!", 14, t1.getDeadlineWeek());
    }
}