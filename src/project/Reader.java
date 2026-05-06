package project;

import java.lang.StringBuilder;
import java.util.Objects;

/**
 * Representa um leitor da biblioteca.
 *
 * Dois leitores são iguais se tiverem o mesmo id.
 */
public class Reader {

    public enum Priority { ESTUDANTE, DOCENTE, INVESTIGADOR };
    private Priority priority;
    private int id;
    private String name;

    /**
     * Constrói um leitor com o identificador, nome e prioridade dados.
     */
    public Reader(int id, String name, Priority priority) {
        this.priority = priority;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Reader obj = (Reader) other;
        return id == obj.id;
    }

    /**
     * Formato: "[id] name (PRIORITY)"
     * Exemplo: "[1] Ana (ESTUDANTE)"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[")
          .append(id)
          .append("] ")
          .append(name)
          .append(" (")
          .append(priority)
          .append(")");
        return sb.toString();
    }
}
