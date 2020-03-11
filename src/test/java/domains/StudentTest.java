package domains;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class StudentTest {
    Student s1;

    @Before
    public void setUp() throws Exception {
        s1 = new Student("Popescu", "Nelutu", "popescunelutu@yahoo.com", "Horia", 221);
        s1.setId("1");
    }

    @Test
    public void getNume() {
        assertEquals("Popescu", s1.getNume());
    }

    @Test
    public void setNume() {
        s1.setNume("Goldis");
        assertEquals("Goldis", s1.getNume());
    }

    @Test
    public void getPrenume() {
        assertEquals("Nelutu", s1.getPrenume());
    }

    @Test
    public void setPrenume() {
        s1.setPrenume("Vasilica");
        assertEquals("Vasilica", s1.getPrenume());
    }

    @Test
    public void getEmail() {
        assertEquals("popescunelutu@yahoo.com", s1.getEmail());
    }

    @Test
    public void setEmail() {
        s1.setEmail("popescu_nelutu@yahoo.com");
        assertEquals("popescu_nelutu@yahoo.com", s1.getEmail());
    }

    @Test
    public void getCadruDidacticLab() {
        assertEquals("Horia", s1.getCadruDidacticLab());
    }

    @Test
    public void setCadruDidacticLab() {
        s1.setCadruDidacticLab("Naomi");
        assertEquals("Naomi", s1.getCadruDidacticLab());
    }

    @Test
    public void getGrupa() {
        assertEquals(221, s1.getGrupa());
    }

    @Test
    public void setGrupa() {
        s1.setGrupa(222);
        assertEquals(222, s1.getGrupa());
    }
}