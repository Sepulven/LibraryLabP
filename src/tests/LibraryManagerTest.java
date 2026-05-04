package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import project.*;

public class LibraryManagerTest {

    @Test
    public void emptyManager() {
        LibraryManager lm = new LibraryManager();
        assertEquals(0, lm.historySize());
        assertFalse(lm.undo());
    }

    @Test
    public void addBookDoesNotEnterHistory() {
        LibraryManager lm = new LibraryManager();
        lm.addBook(new Book("978-1", "T", "A", 1));
        assertEquals(0, lm.historySize());
    }

    @Test
    public void registerGoesToHistory() {
        LibraryManager lm = new LibraryManager();
        lm.registerReader("Ana", Reader.Priority.ESTUDANTE);
        assertEquals(1, lm.historySize());
    }

    @Test
    public void undoRegister() {
        LibraryManager lm = new LibraryManager();
        Reader r = lm.registerReader("Ana", Reader.Priority.ESTUDANTE);
        assertTrue(lm.hasReader(r.getId()));
        assertTrue(lm.undo());
        assertFalse(lm.hasReader(r.getId()));
        assertEquals(0, lm.historySize());
    }

    @Test
    public void undoBorrowOk() {
        LibraryManager lm = new LibraryManager();
        Book b = new Book("978-1", "T", "A", 1);
        lm.addBook(b);
        Reader r = lm.registerReader("Ana", Reader.Priority.ESTUDANTE);
        Loan l = lm.borrow(r.getId(), "978-1", 1);
        assertNotNull(l);
        assertEquals(0, b.availableCopies());

        assertTrue(lm.undo());
        assertEquals(1, b.availableCopies());
        assertTrue(lm.loansOf(r.getId()).isEmpty());
    }

    @Test
    public void undoBorrowWaitlisted() {
        LibraryManager lm = new LibraryManager();
        lm.addBook(new Book("978-1", "T", "A", 1));
        Reader a = lm.registerReader("A", Reader.Priority.ESTUDANTE);
        Reader b = lm.registerReader("B", Reader.Priority.DOCENTE);
        lm.borrow(a.getId(), "978-1", 1);
        Loan waitlisted = lm.borrow(b.getId(), "978-1", 2);
        assertNull(waitlisted);
        assertEquals(1, lm.waitingListFor("978-1").size());

        assertTrue(lm.undo());
        assertEquals(0, lm.waitingListFor("978-1").size());
        assertFalse(lm.waitingListFor("978-1").contains(b));
    }

    @Test
    public void undoReturnOk() {
        LibraryManager lm = new LibraryManager();
        Book b = new Book("978-1", "T", "A", 1);
        lm.addBook(b);
        Reader r = lm.registerReader("Ana", Reader.Priority.ESTUDANTE);
        lm.borrow(r.getId(), "978-1", 1);
        Loan auto = lm.returnBook(r.getId(), "978-1", 2);
        assertNull(auto);
        assertEquals(1, b.availableCopies());
        assertTrue(lm.loansOf(r.getId()).isEmpty());

        assertTrue(lm.undo());
        assertEquals(0, b.availableCopies());
        assertEquals(1, lm.loansOf(r.getId()).size());
    }

    @Test
    public void undoReturnAutoBorrow() {
        LibraryManager lm = new LibraryManager();
        Book book = new Book("978-1", "T", "A", 1);
        lm.addBook(book);
        Reader a = lm.registerReader("A", Reader.Priority.ESTUDANTE);
        Reader b = lm.registerReader("B", Reader.Priority.DOCENTE);
        lm.borrow(a.getId(), "978-1", 1);
        lm.borrow(b.getId(), "978-1", 2);   // waitlisted
        Loan auto = lm.returnBook(a.getId(), "978-1", 3);  // auto-atende b
        assertNotNull(auto);
        assertEquals(b, auto.getReader());
        assertEquals(1, lm.loansOf(b.getId()).size());
        assertTrue(lm.loansOf(a.getId()).isEmpty());
        assertTrue(lm.waitingListFor("978-1").isEmpty());

        assertTrue(lm.undo());
        // b volta à fila, a volta a ter o empréstimo, cópia fica com a
        assertEquals(1, lm.waitingListFor("978-1").size());
        assertTrue(lm.waitingListFor("978-1").contains(b));
        assertTrue(lm.loansOf(b.getId()).isEmpty());
        assertEquals(1, lm.loansOf(a.getId()).size());
        assertEquals(0, book.availableCopies());
    }

    @Test
    public void undosInLifoOrder() {
        LibraryManager lm = new LibraryManager();
        lm.addBook(new Book("978-1", "T", "A", 1));
        Reader a = lm.registerReader("A", Reader.Priority.ESTUDANTE);
        lm.borrow(a.getId(), "978-1", 1);
        // histórico: REGISTER, BORROW_OK (topo)

        assertTrue(lm.undo());   // desfaz BORROW_OK
        assertTrue(lm.loansOf(a.getId()).isEmpty());
        assertTrue(lm.hasReader(a.getId()));

        assertTrue(lm.undo());   // desfaz REGISTER
        assertFalse(lm.hasReader(a.getId()));

        assertFalse(lm.undo());  // histórico vazio
    }

    @Test
    public void historySizeTracks() {
        LibraryManager lm = new LibraryManager();
        lm.addBook(new Book("978-1", "T", "A", 1));
        Reader r = lm.registerReader("A", Reader.Priority.ESTUDANTE);
        assertEquals(1, lm.historySize());
        lm.borrow(r.getId(), "978-1", 1);
        assertEquals(2, lm.historySize());
        lm.returnBook(r.getId(), "978-1", 2);
        assertEquals(3, lm.historySize());
        lm.undo();
        assertEquals(2, lm.historySize());
    }

    @Test
    public void clearHistory() {
        LibraryManager lm = new LibraryManager();
        lm.addBook(new Book("978-1", "T", "A", 1));
        Reader r = lm.registerReader("A", Reader.Priority.ESTUDANTE);
        lm.borrow(r.getId(), "978-1", 1);
        assertEquals(2, lm.historySize());
        lm.clearHistory();
        assertEquals(0, lm.historySize());
        // estado da biblioteca mantém-se
        assertTrue(lm.hasReader(r.getId()));
        assertFalse(lm.loansOf(r.getId()).isEmpty());
        // e agora o undo não faz nada
        assertFalse(lm.undo());
    }
}
