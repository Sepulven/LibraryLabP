package project;


import java.lang.StringBuilder;
import java.util.Objects;

/**
 * Representa um livro do catálogo da biblioteca.
 *
 * Dois livros são iguais se tiverem o mesmo ISBN.
 */
public class Book {

    private String isbn;
    private String title;
    private String author;
    private int total;
    private int avaibleCopies;
    /**
     * Constrói um livro com totalCopies exemplares (todos inicialmente
     * disponíveis).
     */
    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.total = totalCopies;
        this.avaibleCopies = totalCopies;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int totalCopies() {
        return total;
    }

    public int availableCopies() {
        return avaibleCopies;
    }

    public void borrowCopy() {
        avaibleCopies--;
    }

    public void returnCopy() {
        avaibleCopies++;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Book obj = (Book)other;
        return (obj.isbn == isbn);
    }

    /**
     * Formato: 'isbn "title" by author (available X/Y)'
     * Exemplo: '978-972-0-04789-2 "Os Maias" by Eca de Queiros (available 3/3)'
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder ();

        sb.append(isbn)
          .append(" \"")
          .append(title)
          .append("\" by ")
          .append(author)
          .append(" (available ")
          .append(avaibleCopies)
          .append("/")
          .append(total)
          .append(")");
        return sb.toString();
    }

    /**
     * Representação curta (sem "available").
     * Formato: 'isbn "title" by author'
     * Exemplo: '978-972-0-04789-2 "Os Maias" by Eca de Queiros'
     */
    public String toShortString() {
        StringBuilder sb = new StringBuilder ();

        sb.append(isbn)
          .append(" \"")
          .append(title)
          .append("\" by ")
          .append(author);
        return sb.toString();
    }
}
