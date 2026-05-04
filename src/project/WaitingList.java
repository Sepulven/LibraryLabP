package project;

import java.util.PriorityQueue;

/**
 * Fila de espera associada a um livro. Os leitores são atendidos por ordem
 * de prioridade (INVESTIGADOR > DOCENTE > ESTUDANTE); entre leitores do
 * mesmo escalão, o que entrou mais cedo na fila é atendido primeiro.
 *
 * Deve ser implementada usando um PriorityQueue da API Java.
 */
public class WaitingList {

    public WaitingList() {
        // TODO
    }

    /** Acrescenta reader ao fim da fila (respeitando a sua prioridade). */
    public void add(Reader reader) {
        // TODO
    }

    /** Devolve e remove o próximo leitor a ser atendido. */
    public Reader next() {
        // TODO
        return null;
    }

    /** Devolve (sem remover) o próximo leitor a ser atendido. */
    public Reader peek() {
        // TODO
        return null;
    }

    public boolean contains(Reader reader) {
        // TODO
        return false;
    }

    /** Remove reader da fila. */
    public void remove(Reader reader) {
        // TODO
    }

    public boolean isEmpty() {
        // TODO
        return true;
    }

    public int size() {
        // TODO
        return 0;
    }

    /**
     * Formato: "[leitor1, leitor2, ...]" por ordem de atendimento.
     * Fila vazia: "[]".
     * Exemplo: "[[3] Bia Santos (INVESTIGADOR), [4] Leo Monteiro (ESTUDANTE)]"
     */
    @Override
    public String toString() {
        // TODO
        return null;
    }
}
