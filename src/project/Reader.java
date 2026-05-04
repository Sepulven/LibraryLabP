package project;

/**
 * Representa um leitor da biblioteca.
 *
 * Dois leitores são iguais se tiverem o mesmo id.
 */
public class Reader {

    public enum Priority { ESTUDANTE, DOCENTE, INVESTIGADOR }

    /**
     * Constrói um leitor com o identificador, nome e prioridade dados.
     */
    public Reader(int id, String name, Priority priority) {
        // TODO
    }

    public int getId() {
        // TODO
        return 0;
    }

    public String getName() {
        // TODO
        return null;
    }

    public Priority getPriority() {
        // TODO
        return null;
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
     * Formato: "[id] name (PRIORITY)"
     * Exemplo: "[1] Ana (ESTUDANTE)"
     */
    @Override
    public String toString() {
        // TODO
        return null;
    }
}
