package project;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Estado global da biblioteca.
 */
public class Library {

    public Library() { }

    // ----- Operações principais -----

    /**
     * Acrescenta um livro ao catálogo.
     */
    public void addBook(Book book) {
        // TODO
    }

    /**
     * Regista um novo leitor com o próximo id disponível (começando em 1)
     * e devolve-o.
     */
    public Reader registerReader(String name, Reader.Priority p) {
        // TODO
        return null;
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

    /**
     * Tenta emprestar um exemplar do livro ao leitor na data dada.
     */
    public Loan borrow(int readerId, String isbn, int day) {
        // TODO
        return null;
    }

    /**
     * Regista a devolução de um exemplar.
     */
    public Loan returnBook(int readerId, String isbn, int day) {
        // TODO
        return null;
    }

    /** Empréstimos ativos do leitor. Lista vazia se o leitor não existir. */
    public List<Loan> loansOf(int readerId) {
        // TODO
        return null;
    }

    /** Livros por ordem alfabética de ISBN. */
    public List<Book> catalog() {
        // TODO
        return null;
    }

    /** Leitores por ordem crescente de id. */
    public List<Reader> readers() {
        // TODO
        return null;
    }

    /**
     * Devolve a fila de espera associada ao ISBN dado (possivelmente
     * vazia).
     */
    public WaitingList waitingListFor(String isbn) {
        // TODO
        return null;
    }

    // ----- Operações usadas pelo LibraryManager no undo -----

    /**
     * Remove um leitor do sistema.
     */
    public void removeReader(int id) {
        // TODO
    }

    /**
     * Desfaz um empréstimo normal: devolve a cópia do livro e remove o
     * Loan da lista de empréstimos do leitor. NÃO mexe na fila de espera.
     */
    public void cancelLoan(int readerId, String isbn, Loan loan) {
        // TODO
    }

    /**
     * Repõe um empréstimo previamente devolvido: marca a cópia do livro
     * como emprestada e acrescenta o Loan à lista do leitor.
     */
    public void reinstateLoan(int readerId, String isbn, Loan loan) {
        // TODO
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
        // TODO
        return null;
    }
}
