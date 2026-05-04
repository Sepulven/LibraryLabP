package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import project.*;

public class WaitingListTest {

    @Test
    public void emptyListIsEmpty() {
        WaitingList w = new WaitingList();
        assertTrue(w.isEmpty());
        assertEquals(0, w.size());
        assertEquals("[]", w.toString());
    }

    @Test
    public void singleReader() {
        WaitingList w = new WaitingList();
        Reader r = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        w.add(r);
        assertFalse(w.isEmpty());
        assertEquals(1, w.size());
        assertTrue(w.contains(r));
        assertEquals(r, w.peek());
        assertEquals(1, w.size()); // peek não remove
        assertEquals(r, w.next());
        assertTrue(w.isEmpty());
    }

    @Test
    public void higherPrioritiesComeFirst() {
        WaitingList w = new WaitingList();
        Reader est = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        Reader doc = new Reader(2, "Rui", Reader.Priority.DOCENTE);
        Reader inv = new Reader(3, "Bia", Reader.Priority.INVESTIGADOR);
        // Adicionados por ordem crescente de prioridade
        w.add(est);
        w.add(doc);
        w.add(inv);
        assertEquals(inv, w.next());
        assertEquals(doc, w.next());
        assertEquals(est, w.next());
    }

    @Test
    public void fifoWithinSamePriority() {
        WaitingList w = new WaitingList();
        Reader a = new Reader(1, "A", Reader.Priority.ESTUDANTE);
        Reader b = new Reader(2, "B", Reader.Priority.ESTUDANTE);
        Reader c = new Reader(3, "C", Reader.Priority.ESTUDANTE);
        w.add(a); w.add(b); w.add(c);
        assertEquals(a, w.next());
        assertEquals(b, w.next());
        assertEquals(c, w.next());
    }

    @Test
    public void mixedPrioritiesAndFifo() {
        WaitingList w = new WaitingList();
        Reader e1 = new Reader(1, "E1", Reader.Priority.ESTUDANTE);
        Reader d1 = new Reader(2, "D1", Reader.Priority.DOCENTE);
        Reader e2 = new Reader(3, "E2", Reader.Priority.ESTUDANTE);
        Reader d2 = new Reader(4, "D2", Reader.Priority.DOCENTE);
        Reader i1 = new Reader(5, "I1", Reader.Priority.INVESTIGADOR);
        w.add(e1); w.add(d1); w.add(e2); w.add(d2); w.add(i1);
        // Esperado: i1 (INV), d1 (DOC, mais antigo), d2 (DOC), e1 (EST, mais antigo), e2 (EST)
        assertEquals(i1, w.next());
        assertEquals(d1, w.next());
        assertEquals(d2, w.next());
        assertEquals(e1, w.next());
        assertEquals(e2, w.next());
    }

    @Test
    public void removeReader() {
        WaitingList w = new WaitingList();
        Reader a = new Reader(1, "A", Reader.Priority.ESTUDANTE);
        Reader b = new Reader(2, "B", Reader.Priority.ESTUDANTE);
        Reader c = new Reader(3, "C", Reader.Priority.ESTUDANTE);
        w.add(a); w.add(b); w.add(c);
        w.remove(b);
        assertEquals(2, w.size());
        assertFalse(w.contains(b));
        assertEquals(a, w.next());
        assertEquals(c, w.next());
    }

    @Test
    public void toStringOrderedByService() {
        WaitingList w = new WaitingList();
        Reader e = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        Reader d = new Reader(2, "Rui", Reader.Priority.DOCENTE);
        w.add(e);
        w.add(d);
        assertEquals("[[2] Rui (DOCENTE), [1] Ana (ESTUDANTE)]", w.toString());
    }
}
