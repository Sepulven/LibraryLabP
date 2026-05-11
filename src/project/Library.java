package project;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Iterator;

import java.lang.StringBuilder;

/**
 * Estado global da biblioteca.
 */
public class Library {

    private TreeMap<String,Book> booksCatalog;
    private TreeMap<Integer, Reader> readers;
    private HashMap<Integer, List<Loan>> loans;
    private HashMap<String, WaitingList> booksWaitingList;
    private int readersCounter;

    public Library() {
       booksCatalog = new TreeMap<>();
       readers = new TreeMap<>();
       loans = new LinkedHashMap<>();
       booksWaitingList = new LinkedHashMap<>();
       readersCounter = 1; 
    }

    // ----- Operações principais -----

    /**
     * Acrescenta um livro ao catálogo.
     */
    public void addBook(Book book) {
        booksCatalog.put(book.getIsbn(), book);
    }

    /**
     * Regista um novo leitor com o próximo id disponível (começando em 1)
     * e devolve-o.
     */
    public Reader registerReader(String name, Reader.Priority p) {
        Reader r = new Reader(readersCounter++, name, p);

        readers.put(r.getId(), r);
        return r;
    }

    public boolean hasBook(String isbn) {
        return booksCatalog.containsKey(isbn);
    }

    public boolean hasReader(int id) {
        return readers.containsKey(id);
    }

    public Book getBook(String isbn) {
        return booksCatalog.get(isbn);
    }

    public Reader getReader(int id) {
        return readers.get(id);
    }

    /**
     * Tenta emprestar um exemplar do livro ao leitor na data dada.
     */
    public Loan borrow(int readerId, String isbn, int day) {
        Reader r = getReader(readerId);
        Book b = getBook(isbn);
        List<Loan> loanList = loans.get(readerId);

        if (b == null || r == null) {
            return null;
        }

        if (b.availableCopies() == 0) {
           waitingListFor(isbn).add(r);
           return null;
        }
        if (loanList == null) {
            loanList = new LinkedList<>();
            loans.put(readerId, loanList);
        }

        Loan loan = new Loan(r, b, day);

        loanList.add(loan);
        b.borrowCopy();
        return loan;
    }

    /**
     * Regista a devolução de um exemplar.
     */
    public Loan returnBook(int readerId, String isbn, int day) {
        List<Loan> loanList = loans.get(readerId);
        WaitingList w = waitingListFor(isbn);
        Book b = booksCatalog.get(isbn);

        if (b == null || loanList == null){
            return null;
        }

        Iterator<Loan> it = loanList.iterator();

        while (it.hasNext()) {
            Loan l = it.next();
            if (l.getBook().equals(b)) {
                it.remove();
            }
        }

        b.returnCopy();

        if (w.isEmpty()) {
            return null;
        }
        return borrow(w.next().getId(), isbn, day);
    }

    /** Empréstimos ativos do leitor. Lista vazia se o leitor não existir. */
    public List<Loan> loansOf(int readerId) {
        List<Loan> l = loans.get(readerId);

        if (l == null) {
            return new LinkedList<>();
        }
        return l;
    }

    /** Livros por ordem alfabética de ISBN. */
    public List<Book> catalog() {
        return new LinkedList<>(booksCatalog.values());
    }

    /** Leitores por ordem crescente de id. */
    public List<Reader> readers() {
        return new LinkedList<>(readers.values());
    }

    /**
     * Devolve a fila de espera associada ao ISBN dado (possivelmente
     * vazia).
     */
    public WaitingList waitingListFor(String isbn) {
        WaitingList w = booksWaitingList.get(isbn);

        if (w == null) {
            w = new WaitingList();
            booksWaitingList.put(isbn, w);
        }
        return w;
    }

    // ----- Operações usadas pelo LibraryManager no undo -----

    /**
     * Remove um leitor do sistema.
     */
    public void removeReader(int id) {
        readers.remove(id);
        readersCounter--;
    }

    /**
     * Desfaz um empréstimo normal: devolve a cópia do livro e remove o
     * Loan da lista de empréstimos do leitor. NÃO mexe na fila de espera.
     */
    public void cancelLoan(int readerId, String isbn, Loan loan) {
        loans.get(readerId).remove(loan);
        loan.getBook().returnCopy();
    }

    /**
     * Repõe um empréstimo previamente devolvido: marca a cópia do livro
     * como emprestada e acrescenta o Loan à lista do leitor.
     */
    public void reinstateLoan(int readerId, String isbn, Loan loan) {
        List<Loan> loanList = loans.get(readerId);

        if (loanList != null) {
            booksCatalog.get(isbn).borrowCopy();
            loanList.add(loan);
        }
    }

    /**
     * Representação multi-linha com as secções:
     *   Books:, Readers:, Loans:, Waiting lists:
     * Cada item é indentado com dois espaços ("  "). Secções vazias
     * mantêm só o cabeçalho. Em "Waiting lists:" só aparecem livros
     * com fila não vazia, no formato "  isbn -> [leitor1, leitor2, ...]".
     * Sem newline final.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Books:");
        for (Book b : booksCatalog.values()) {
            sb.append("  ")
              .append(b)
              .append(System.lineSeparator());
        }

        sb.append("Readers:");
        for (Reader r : readers.values()) {
            sb.append("  ")
              .append(r)
              .append(System.lineSeparator());
        }

        sb.append("Loans:");
        for (List<Loan> l : loans.values()) {
            sb.append("  ")
              .append(l)
              .append(System.lineSeparator());
        }

        sb.append("Waiting Lists:");
        for (String isbn : booksWaitingList.keySet()) {
            sb.append("  ")
              .append(isbn)
              .append("-> ")
              .append(booksWaitingList.get(isbn))
              .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
