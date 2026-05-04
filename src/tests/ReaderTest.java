package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import project.*;
public class ReaderTest {

    @Test
    public void gettersWork() {
        Reader r = new Reader(1, "Ana Ferreira", Reader.Priority.ESTUDANTE);
        assertEquals(1, r.getId());
        assertEquals("Ana Ferreira", r.getName());
        assertEquals(Reader.Priority.ESTUDANTE, r.getPriority());
    }

    @Test
    public void equalityIsById() {
        Reader r1 = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        Reader r2 = new Reader(1, "Outra", Reader.Priority.DOCENTE);
        Reader r3 = new Reader(2, "Ana", Reader.Priority.ESTUDANTE);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
    }

    @Test
    public void toStringFormat() {
        Reader r = new Reader(1, "Ana Ferreira", Reader.Priority.ESTUDANTE);
        assertEquals("[1] Ana Ferreira (ESTUDANTE)", r.toString());
        Reader r2 = new Reader(42, "Rui Almeida", Reader.Priority.DOCENTE);
        assertEquals("[42] Rui Almeida (DOCENTE)", r2.toString());
    }

    @Test
    public void priorityOrdinalIncreases() {
        // ESTUDANTE < DOCENTE < INVESTIGADOR (confirma a declaração do enum)
        assertTrue(Reader.Priority.ESTUDANTE.ordinal()
                < Reader.Priority.DOCENTE.ordinal());
        assertTrue(Reader.Priority.DOCENTE.ordinal()
                < Reader.Priority.INVESTIGADOR.ordinal());
    }
}
