package domains;

import com.sun.tools.javac.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotaTest {
    Nota nota;

    @BeforeEach
    void setUp() {
        nota = new Nota(LocalDate.of(2019, 11, 11), "Vasile", 10, feedback);
        nota.setId(Pair.of("1", "1"));
    }

    @Test
    void getIdStudent() {
        assertEquals("1", nota.getId().fst);
    }

    @Test
    void setIdStudent() {
        nota.setId(Pair.of("2", "1"));
        assertEquals("2", nota.getId().fst);
    }

    @Test
    void getIdTema() {
        assertEquals("1", nota.getId().snd);
    }

    @Test
    void setIdTema() {
        nota.setId(Pair.of("2", "2"));
        assertEquals("2", nota.getId().snd);
    }

    @Test
    void getData() {
        assertEquals(LocalDate.of(2019, 11, 11), nota.getData());
    }

    @Test
    void setData() {
        nota.setData(LocalDate.of(2019, 11, 15));
        assertEquals(LocalDate.of(2019, 11, 15), nota.getData());

    }

    @Test
    void getProfesor() {
        assertEquals("Vasile", nota.getProfesor());
    }

    @Test
    void setProfesor() {
        nota.setProfesor("Gigi");
        assertEquals("Gigi", nota.getProfesor());
    }

    @Test
    void getValoare() {
        assertEquals(10, nota.getValoare());
    }

    @Test
    void setValoare() {
        nota.setValoare(9);
        assertEquals(9, nota.getValoare());
    }
}