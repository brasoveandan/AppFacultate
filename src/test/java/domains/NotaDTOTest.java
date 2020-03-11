package domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotaDTOTest {
    NotaDTO notaDTO;

    @BeforeEach
    void setUp() {
        notaDTO = new NotaDTO("Dan", "1", 10, 5, 7, "Excelent");
    }

    @Test
    void getNumeStudent() {
        assertEquals("Dan", notaDTO.getNumeStudent());
    }

    @Test
    void setNumeStudent() {
        notaDTO.setNumeStudent("Vasile");
        assertEquals("Vasile", notaDTO.getNumeStudent());
    }

    @Test
    void getIdTema() {
        assertEquals("1", notaDTO.getIdTema());
    }

    @Test
    void setIdTema() {
        notaDTO.setIdTema("2");
        assertEquals("2", notaDTO.getIdTema());
    }

    @Test
    void getValoare() {
        assertEquals(10, notaDTO.getValoare());
    }

    @Test
    void setValoare() {
        notaDTO.setValoare(9);
        assertEquals(9, notaDTO.getValoare());
    }

    @Test
    void getNrSaptamana() {
        assertEquals(5, notaDTO.getNrSaptamana());
    }

    @Test
    void setNrSaptamana() {
        notaDTO.setNrSaptamana(6);
        assertEquals(6, notaDTO.getNrSaptamana());
    }

    @Test
    void getDeadlineWeek() {
        assertEquals(7, notaDTO.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        notaDTO.setDeadlineWeek(8);
        assertEquals(8, notaDTO.getDeadlineWeek());
    }

    @Test
    void getFeedback() {
        assertEquals("Excelent", notaDTO.getFeedback());
    }

    @Test
    void setFeedback() {
        notaDTO.setFeedback("Aproape bine");
        assertEquals("Aproape bine", notaDTO.getFeedback());
    }
}