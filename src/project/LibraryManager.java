package project;

import java.util.List;
import java.util.Stack;

/**
 * Biblioteca com histórico de operações e anulação da última (undo).
 */
public class LibraryManager {
    private Library library;

    private record OperationData (String operation, int readerId, String isbn, int day, Reader waitingReader) {}
    private Stack<OperationData> history;

    public LibraryManager() {
        library = new Library();
        history = new Stack<>();
    }

    // ----- Métodos que delegam na Library sem alterar o histórico -----

    public void addBook(Book book) {
        library.addBook(book);
    }

    public boolean hasBook(String isbn) {
        return library.hasBook(isbn);
    }

    public boolean hasReader(int id) {
        return library.hasReader(id);
    }

    public Book getBook(String isbn) {
        return library.getBook(isbn);
    }

    public Reader getReader(int id) {
        return library.getReader(id);
    }

    public List<Loan> loansOf(int readerId) {
        return library.loansOf(readerId);
    }

    public List<Book> catalog() {
        return library.catalog();
    }

    public List<Reader> readers() {
        return library.readers();
    }

    public WaitingList waitingListFor(String isbn) {
        return library.waitingListFor(isbn);
    }

    // ----- Métodos que, para além de delegar, registam no histórico -----

    public Reader registerReader(String name, Reader.Priority p) {
        Reader r = library.registerReader(name, p);
        history.push(new OperationData("register", r.getId(), "", 0, null));
        return r;
    }

    public Loan borrow(int readerId, String isbn, int day) {
        WaitingList w = waitingListFor(isbn);
        history.push(new OperationData("borrow", readerId, isbn, day,
                        library.getReader(readerId)));
        return library.borrow(readerId, isbn, day);
    }

    public Loan returnBook(int readerId, String isbn, int day) {
        WaitingList w = waitingListFor(isbn);
        history.push(new OperationData("return", readerId, isbn, day,
                        w.isEmpty() ? null : w.peek()));
        return library.returnBook(readerId, isbn, day);
    }

    // ----- Undo -----

    /**
     * Anula a última operação registada. Devolve true se havia operação
     * para anular, false se o histórico estava vazio.
     */
    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }
        OperationData o = history.pop();

        switch (o.operation()) {
            case "return" -> {
                if (o.waitingReader() != null) {
                    library.returnBook(o.waitingReader().getId(), o.isbn(), o.day);
                }
                library.borrow(o.readerId(), o.isbn(), o.day());
                if (o.waitingReader() != null) {
                    library.borrow(o.waitingReader().getId(), o.isbn(), o.day());
                }
            }
            case "borrow" -> {
                WaitingList w = library.waitingListFor(o.isbn());

                if (w.contains(o.waitingReader())) {
                    w.remove(o.waitingReader());
                }
                library.returnBook(o.readerId(), o.isbn(), o.day());
            } 
            case "register" -> library.removeReader(o.readerId());
        }
        return true;
    }

    public int historySize() {
        return history.size();
    }

    public void clearHistory() {
        history.clear();
    }

    /**
     * Representação multi-linha (a mesma da Library interna).
     */
    @Override
    public String toString() {
        return library.toString();
    }
}
