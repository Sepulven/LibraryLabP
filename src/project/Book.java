package project;

/**
 * Representa um livro do catálogo da biblioteca.
 *
 * Dois livros são iguais se tiverem o mesmo ISBN.
 */
public class Book {

    /**
     * Constrói um livro com totalCopies exemplares (todos inicialmente
     * disponíveis).
     */
    public Book(String isbn, String title, String author, int totalCopies) {
        // TODO
    }

    public String getIsbn() {
        // TODO
        return null;
    }

    public String getTitle() {
        // TODO
        return null;
    }

    public String getAuthor() {
        // TODO
        return null;
    }

    public int totalCopies() {
        // TODO
        return 0;
    }

    public int availableCopies() {
        // TODO
        return 0;
    }

    public void borrowCopy() {
        // TODO
    }

    public void returnCopy() {
        // TODO
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        // TODO
        return false;
    }

    /**
     * Formato: 'isbn "title" by author (available X/Y)'
     * Exemplo: '978-972-0-04789-2 "Os Maias" by Eca de Queiros (available 3/3)'
     */
    @Override
    public String toString() {
        // TODO
        return null;
    }

    /**
     * Representação curta (sem "available").
     * Formato: 'isbn "title" by author'
     * Exemplo: '978-972-0-04789-2 "Os Maias" by Eca de Queiros'
     */
    public String toShortString() {
        // TODO
        return null;
    }
}
