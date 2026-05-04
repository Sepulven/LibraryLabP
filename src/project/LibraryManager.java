package project;

import java.util.List;

/**
 * Biblioteca com histórico de operações e anulação da última (undo).
 */
public class LibraryManager {

    public LibraryManager() {
        // TODO
    }

    // ----- Métodos que delegam na Library sem alterar o histórico -----

    public void addBook(Book book) {
        // TODO
    }

    public boolean hasBook(String isbn) {
        // TODO
        return false;
    }

    public boolean hasReader(int id) {
        // TODO
        return false;
    }

    public Book getBook(String isbn) {
        // TODO
        return null;
    }

    public Reader getReader(int id) {
        // TODO
        return null;
    }

    public List<Loan> loansOf(int readerId) {
        // TODO
        return null;
    }

    public List<Book> catalog() {
        // TODO
        return null;
    }

    public List<Reader> readers() {
        // TODO
        return null;
    }

    public WaitingList waitingListFor(String isbn) {
        // TODO
        return null;
    }

    // ----- Métodos que, para além de delegar, registam no histórico -----

    public Reader registerReader(String name, Reader.Priority p) {
        return null;
    }

    public Loan borrow(int readerId, String isbn, int day) {
        return null;
    }

    public Loan returnBook(int readerId, String isbn, int day) {
        return null;
    }

    // ----- Undo -----

    /**
     * Anula a última operação registada. Devolve true se havia operação
     * para anular, false se o histórico estava vazio.
     */
    public boolean undo() {
        // TODO
        return false;
    }

    public int historySize() {
        // TODO
        return 0;
    }

    public void clearHistory() {
        // TODO
    }

    /**
     * Representação multi-linha (a mesma da Library interna).
     */
    @Override
    public String toString() {
        // TODO
        return null;
    }
}
