package project;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

/**
 * Fila de espera associada a um livro. Os leitores são atendidos por ordem
 * de prioridade (INVESTIGADOR > DOCENTE > ESTUDANTE); entre leitores do
 * mesmo escalão, o que entrou mais cedo na fila é atendido primeiro.
 *
 * Deve ser implementada usando um PriorityQueue da API Java.
 */
public class WaitingList {

    private record Entry(Reader reader, long order) implements Comparable<Entry>{
    	@Override
    	public int compareTo(Entry o) {
    		int p = Integer.compare(o.reader().getPriority().ordinal(),
    								this.reader().getPriority().ordinal());
    		if (p != 0) return p;
    		return Long.compare(this.order(), o.order());
    	}
    }
    private Queue<Entry> q;
    private int counter;


    public WaitingList() {
        q = new PriorityQueue<>();
        counter = 0;
    }

    /** Acrescenta reader ao fim da fila (respeitando a sua prioridade). */
    public void add(Reader reader) {
        q.add(new Entry(reader, counter++));
    }

    /** Devolve e remove o próximo leitor a ser atendido. */
    public Reader next() {
        Entry e = q.peek();

        q.remove(e);
        return e.reader();
    }

    /** Devolve (sem remover) o próximo leitor a ser atendido. */
    public Reader peek() {
        return q.peek().reader();
    }

    public boolean contains(Reader reader) {
        Iterator<Entry> it = q.iterator();

        while (it.hasNext()) {
            if (it.next().reader().equals(reader)) {
                return true;
            }
        }
        return false;
    }

    /** Remove reader da fila. */
    public void remove(Reader reader) {
        Iterator<Entry> it = q.iterator();
        Entry e;

        while (it.hasNext()) {
            e = it.next();
            if (e.reader().equals(reader)) {
                q.remove(e);
                return ;
            }
        }
    }

    public boolean isEmpty() {
        return q.isEmpty();
    }

    public int size() {
        return q.size();
    }

    /**
     * Formato: "[leitor1, leitor2, ...]" por ordem de atendimento.
     * Fila vazia: "[]".
     * Exemplo: "[[3] Bia Santos (INVESTIGADOR), [4] Leo Monteiro (ESTUDANTE)]"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry> it = q.iterator();

        sb.append("[");
        while (it.hasNext()) {
            sb.append(it.next().reader());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
