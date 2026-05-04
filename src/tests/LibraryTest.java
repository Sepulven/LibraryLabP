package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import project.*;

public class LibraryTest {

    @Test
    public void emptyLibrary() {
        Library lib = new Library();
        assertTrue(lib.catalog().isEmpty());
        assertTrue(lib.readers().isEmpty());
        assertFalse(lib.hasBook("978-1"));
        assertFalse(lib.hasReader(1));
    }

    @Test
    public void addBook() {
        Library lib = new Library();
        Book b = new Book("978-1", "T", "A", 2);
        lib.addBook(b);
        assertTrue(lib.hasBook("978-1"));
        assertSame(b, lib.getBook("978-1"));
    }

    @Test
    public void registerReaderAssignsSequentialIds() {
        Library lib = new Library();
        Reader a = lib.registerReader("A", Reader.Priority.ESTUDANTE);
        Reader b = lib.registerReader("B", Reader.Priority.DOCENTE);
        Reader c = lib.registerReader("C", Reader.Priority.INVESTIGADOR);
        assertEquals(1, a.getId());
        assertEquals(2, b.getId());
        assertEquals(3, c.getId());
        assertTrue(lib.hasReader(1));
        assertTrue(lib.hasReader(2));
        assertTrue(lib.hasReader(3));
        assertFalse(lib.hasReader(4));
    }

    @Test
    public void borrowWhenAvailable() {
        Library lib = new Library();
        Book b = new Book("978-1", "T", "A", 1);
        lib.addBook(b);
        Reader r = lib.registerReader("Ana", Reader.Priority.ESTUDANTE);
        Loan l = lib.borrow(r.getId(), "978-1", 5);
        assertNotNull(l);
        assertEquals(5, l.getDay());
        assertEquals(r, l.getReader());
        assertEquals(0, b.availableCopies());
        assertEquals(1, lib.loansOf(r.getId()).size());
    }

    @Test
    public void borrowWaitlistsWhenNoCopies() {
        Library lib = new Library();
        lib.addBook(new Book("978-1", "T", "A", 1));
        Reader a = lib.registerReader("A", Reader.Priority.ESTUDANTE);
        Reader b = lib.registerReader("B", Reader.Priority.DOCENTE);
        lib.borrow(a.getId(), "978-1", 1);
        Loan l = lib.borrow(b.getId(), "978-1", 2);
        assertNull(l);
        assertEquals(1, lib.waitingListFor("978-1").size());
        assertTrue(lib.waitingListFor("978-1").contains(b));
        assertEquals(0, lib.loansOf(b.getId()).size());
    }

    @Test
    public void returnWithoutQueue() {
        Library lib = new Library();
        Book book = new Book("978-1", "T", "A", 1);
        lib.addBook(book);
        Reader r = lib.registerReader("Ana", Reader.Priority.ESTUDANTE);
        lib.borrow(r.getId(), "978-1", 1);
        Loan auto = lib.returnBook(r.getId(), "978-1", 2);
        assertNull(auto);
        assertEquals(1, book.availableCopies());
        assertTrue(lib.loansOf(r.getId()).isEmpty());
    }

    @Test
    public void returnTriggersAutoBorrow() {
        Library lib = new Library();
        Book book = new Book("978-1", "T", "A", 1);
        lib.addBook(book);
        Reader a = lib.registerReader("A", Reader.Priority.ESTUDANTE);
        Reader b = lib.registerReader("B", Reader.Priority.DOCENTE);
        lib.borrow(a.getId(), "978-1", 1);
        lib.borrow(b.getId(), "978-1", 2);  // vai para fila
        Loan auto = lib.returnBook(a.getId(), "978-1", 3);
        assertNotNull(auto);
        assertEquals(b, auto.getReader());
        assertEquals(3, auto.getDay());
        assertEquals(0, book.availableCopies());
        assertTrue(lib.loansOf(a.getId()).isEmpty());
        assertEquals(1, lib.loansOf(b.getId()).size());
        assertTrue(lib.waitingListFor("978-1").isEmpty());
    }

    @Test
    public void returnAutoBorrowRespectsPriority() {
        Library lib = new Library();
        lib.addBook(new Book("978-1", "T", "A", 1));
        Reader owner = lib.registerReader("O", Reader.Priority.ESTUDANTE);
        Reader est = lib.registerReader("E", Reader.Priority.ESTUDANTE);
        Reader doc = lib.registerReader("D", Reader.Priority.DOCENTE);
        Reader inv = lib.registerReader("I", Reader.Priority.INVESTIGADOR);
        lib.borrow(owner.getId(), "978-1", 1);
        lib.borrow(est.getId(), "978-1", 2);
        lib.borrow(doc.getId(), "978-1", 3);
        lib.borrow(inv.getId(), "978-1", 4);
        Loan auto = lib.returnBook(owner.getId(), "978-1", 5);
        assertNotNull(auto);
        assertEquals(inv, auto.getReader()); // INVESTIGADOR vai primeiro
    }

    @Test
    public void catalogOrderedByIsbn() {
        Library lib = new Library();
        lib.addBook(new Book("978-3", "C", "X", 1));
        lib.addBook(new Book("978-1", "A", "X", 1));
        lib.addBook(new Book("978-2", "B", "X", 1));
        List<Book> cat = lib.catalog();
        assertEquals(3, cat.size());
        assertEquals("978-1", cat.get(0).getIsbn());
        assertEquals("978-2", cat.get(1).getIsbn());
        assertEquals("978-3", cat.get(2).getIsbn());
    }

    @Test
    public void readersOrderedById() {
        Library lib = new Library();
        lib.registerReader("A", Reader.Priority.ESTUDANTE);
        lib.registerReader("B", Reader.Priority.DOCENTE);
        lib.registerReader("C", Reader.Priority.INVESTIGADOR);
        List<Reader> rs = lib.readers();
        assertEquals(3, rs.size());
        assertEquals(1, rs.get(0).getId());
        assertEquals(2, rs.get(1).getId());
        assertEquals(3, rs.get(2).getId());
    }

    @Test
    public void waitingListExistsAfterAddBook() {
        Library lib = new Library();
        lib.addBook(new Book("978-1", "T", "A", 1));
        WaitingList wl = lib.waitingListFor("978-1");
        assertNotNull(wl);
        assertTrue(wl.isEmpty());
    }

    // ----- Métodos auxiliares usados pelo LibraryManager -----

    @Test
    public void removeReaderRemoves() {
        Library lib = new Library();
        Reader r = lib.registerReader("Ana", Reader.Priority.ESTUDANTE);
        assertTrue(lib.hasReader(r.getId()));
        lib.removeReader(r.getId());
        assertFalse(lib.hasReader(r.getId()));
    }

    @Test
    public void removeReaderRecyclesLastId() {
        Library lib = new Library();
        Reader a = lib.registerReader("A", Reader.Priority.ESTUDANTE);
        assertEquals(1, a.getId());
        lib.removeReader(a.getId());
        Reader b = lib.registerReader("B", Reader.Priority.ESTUDANTE);
        assertEquals(1, b.getId());
    }

    @Test
    public void cancelLoanReturnsCopyAndRemovesLoan() {
        Library lib = new Library();
        Book book = new Book("978-1", "T", "A", 2);
        lib.addBook(book);
        Reader r = lib.registerReader("Ana", Reader.Priority.ESTUDANTE);
        Loan l = lib.borrow(r.getId(), "978-1", 1);
        assertEquals(1, book.availableCopies());
        lib.cancelLoan(r.getId(), "978-1", l);
        assertEquals(2, book.availableCopies());
        assertTrue(lib.loansOf(r.getId()).isEmpty());
    }

    @Test
    public void reinstateLoanUndoesReturn() {
        Library lib = new Library();
        Book book = new Book("978-1", "T", "A", 1);
        lib.addBook(book);
        Reader r = lib.registerReader("Ana", Reader.Priority.ESTUDANTE);
        Loan l = lib.borrow(r.getId(), "978-1", 1);
        lib.returnBook(r.getId(), "978-1", 2);
        assertEquals(1, book.availableCopies());
        lib.reinstateLoan(r.getId(), "978-1", l);
        assertEquals(0, book.availableCopies());
        assertEquals(1, lib.loansOf(r.getId()).size());
    }
}
