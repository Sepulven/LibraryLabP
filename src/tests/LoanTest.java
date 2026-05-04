package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import project.*;

public class LoanTest {

    @Test
    public void gettersWork() {
        Reader r = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        Book b = new Book("978-1", "Os Maias", "Eca", 1);
        Loan l = new Loan(r, b, 3);
        assertSame(r, l.getReader());
        assertSame(b, l.getBook());
        assertEquals(3, l.getDay());
    }

    @Test
    public void toStringUsesReaderAndShortBook() {
        Reader r = new Reader(1, "Ana", Reader.Priority.ESTUDANTE);
        Book b = new Book("978-1", "Os Maias", "Eca", 1);
        Loan l = new Loan(r, b, 3);
        assertEquals(
            "[1] Ana (ESTUDANTE) - 978-1 \"Os Maias\" by Eca (day 3)",
            l.toString());
    }

    @Test
    public void loanToStringUnaffectedByBookAvailability() {
        // O formato do Loan usa toShortString do livro, portanto não depende
        // de borrowCopy/returnCopy terem sido chamados entretanto.
        Reader r = new Reader(2, "Rui", Reader.Priority.DOCENTE);
        Book b = new Book("978-1", "T", "A", 2);
        Loan l = new Loan(r, b, 5);
        String s1 = l.toString();
        b.borrowCopy();
        String s2 = l.toString();
        assertEquals(s1, s2);
    }
}
