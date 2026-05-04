package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import project.Book;

public class BookTest {

    @Test
    public void gettersWork() {
        Book b = new Book("978-972-0-04789-2", "Os Maias", "Eca de Queiros", 3);
        assertEquals("978-972-0-04789-2", b.getIsbn());
        assertEquals("Os Maias", b.getTitle());
        assertEquals("Eca de Queiros", b.getAuthor());
        assertEquals(3, b.totalCopies());
        assertEquals(3, b.availableCopies());
    }

    @Test
    public void borrowAndReturnCopies() {
        Book b = new Book("978-1", "T", "A", 2);
        b.borrowCopy();
        assertEquals(1, b.availableCopies());
        assertEquals(2, b.totalCopies());
        b.borrowCopy();
        assertEquals(0, b.availableCopies());
        b.returnCopy();
        assertEquals(1, b.availableCopies());
    }

    @Test
    public void equalityIsByIsbn() {
        Book b1 = new Book("978-1", "T1", "A1", 1);
        Book b2 = new Book("978-1", "T2", "A2", 5);
        Book b3 = new Book("978-2", "T1", "A1", 1);
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1, b3);
    }

    @Test
    public void toStringFormat() {
        Book b = new Book("978-1", "Os Maias", "Eca", 3);
        assertEquals("978-1 \"Os Maias\" by Eca (available 3/3)", b.toString());
        b.borrowCopy();
        assertEquals("978-1 \"Os Maias\" by Eca (available 2/3)", b.toString());
    }

    @Test
    public void toShortStringOmitsAvailable() {
        Book b = new Book("978-1", "Os Maias", "Eca", 3);
        assertEquals("978-1 \"Os Maias\" by Eca", b.toShortString());
        b.borrowCopy();
        // toShortString não depende do estado de disponibilidade
        assertEquals("978-1 \"Os Maias\" by Eca", b.toShortString());
    }
}
