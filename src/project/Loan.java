package project;

import java.lang.StringBuilder;
/**
 * Representa um empréstimo ativo de um livro a um leitor numa dada data.
 */
public class Loan {
    private Reader reader;
    private Book book;
    private int day;

    public Loan(Reader reader, Book book, int day) {
        this.reader = reader;
        this.book = book;
        this.day = day;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public int getDay() {
        return day;
    }

    /**
     * Formato: "reader - book (day D)"
     * Exemplo: '[1] Ana Ferreira (ESTUDANTE) - 978-972-0-04789-2 "Os Maias" by Eca de Queiros (day 1)'
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(reader)
          .append(" - ")
          .append(book.toShortString())
          .append(" (day ").append(day).append(")");
        return sb.toString();
    }
}
