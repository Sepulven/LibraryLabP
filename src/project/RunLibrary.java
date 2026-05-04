package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Aplicação cliente. Recebe como argumentos os nomes de ficheiros de
 * operações e produz output no stdout. Cada ficheiro é processado com
 * uma instância nova de LibraryManager.
 *
 * Formato das operações (campos separados por ';'):
 *   addBook;<isbn>;<titulo>;<autor>;<exemplares>
 *   register;<nome>;<prioridade>
 *   borrow;<id_leitor>;<isbn>;<dia>
 *   return;<id_leitor>;<isbn>;<dia>
 *   loansOf;<id_leitor>
 *   catalog
 *   readers
 *   waiting;<isbn>
 *   undo
 *
 */
public class RunLibrary {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                System.out.println();
            }
            // mostrar apenas o nome do ficheiro (sem o path)
            String name = args[i];
            int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
            if (slash >= 0) {
                name = name.substring(slash + 1);
            }
            System.out.println("=== " + name + " ===");
            runFile(args[i]);
        }
    }

    private static void runFile(String path) throws IOException {
        LibraryManager lib = new LibraryManager();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                execute(lib, line);
            }
        }
    }

    private static void execute(LibraryManager lib, String line) {
        String[] p = line.split(";");
        String op = p[0];
        switch (op) {
        case "addBook": {
            // addBook;<isbn>;<titulo>;<autor>;<exemplares>
            String isbn = p[1];
            String title = p[2];
            String author = p[3];
            int copies = Integer.parseInt(p[4]);
            lib.addBook(new Book(isbn, title, author, copies));
            System.out.println("addBook: " + isbn);
            break;
        }
        case "register": {
            // register;<nome>;<prioridade>
            String name = p[1];
            Reader.Priority prio = Reader.Priority.valueOf(p[2]);
            Reader r = lib.registerReader(name, prio);
            System.out.println("register: " + r);
            break;
        }
        case "borrow": {
            // borrow;<id>;<isbn>;<dia>
            int id = Integer.parseInt(p[1]);
            String isbn = p[2];
            int day = Integer.parseInt(p[3]);
            if (!lib.hasReader(id) || !lib.hasBook(isbn)) {
                System.out.println("borrow: error (unknown reader/book)");
                break;
            }
            Loan loan = lib.borrow(id, isbn, day);
            if (loan != null) {
                System.out.println("borrow: " + loan);
            }
            else {
                System.out.println("borrow: waitlisted " + id + " for " + isbn);
            }
            break;
        }
        case "return": {
            // return;<id>;<isbn>;<dia>
            int id = Integer.parseInt(p[1]);
            String isbn = p[2];
            int day = Integer.parseInt(p[3]);
            if (!lib.hasReader(id) || !lib.hasBook(isbn)) {
                System.out.println("return: error (no such loan)");
                break;
            }
            // validar que o leitor tem esse livro
            boolean has = false;
            for (Loan l : lib.loansOf(id)) {
                if (l.getBook().getIsbn().equals(isbn)) { has = true; break; }
            }
            if (!has) {
                System.out.println("return: error (no such loan)");
                break;
            }
            Loan auto = lib.returnBook(id, isbn, day);
            if (auto == null) {
                System.out.println("return: ok");
            }
            else {
                System.out.println("return: auto-borrow " + auto);
            }
            break;
        }
        case "loansOf": {
            int id = Integer.parseInt(p[1]);
            System.out.println("loansOf " + id + ":");
            List<Loan> ls = lib.loansOf(id);
            if (ls.isEmpty()) {
                System.out.println("  (no loans)");
            }
            else {
                for (Loan l : ls) {
                    System.out.println("  - " + l);
                }
            }
            break;
        }
        case "catalog": {
            System.out.println("catalog:");
            List<Book> bs = lib.catalog();
            if (bs.isEmpty()) {
                System.out.println("  (empty)");
            }
            else {
                for (Book b : bs) {
                    System.out.println("  - " + b);
                }
            }
            break;
        }
        case "readers": {
            System.out.println("readers:");
            List<Reader> rs = lib.readers();
            if (rs.isEmpty()) {
                System.out.println("  (empty)");
            }
            else {
                for (Reader r : rs) {
                    System.out.println("  - " + r);
                }
            }
            break;
        }
        case "waiting": {
            // waiting;<isbn>
            String isbn = p[1];
            System.out.println("waiting " + isbn + ":");
            printWaiting(lib, isbn);
            break;
        }
        case "undo": {
            boolean ok = lib.undo();
            System.out.println(ok ? "undo: ok" : "undo: empty");
            break;
        }
        default:
            // operação desconhecida: ignora
            break;
        }
    }

    private static void printWaiting(LibraryManager lib, String isbn) {
        if (!lib.hasBook(isbn)) {
            System.out.println("  (unknown book)");
            return;
            }
        WaitingList wl = lib.waitingListFor(isbn);
        if (wl.isEmpty()) {
            System.out.println("  (empty)");
            }
        else {
            // imprimir cada leitor numa linha, por ordem de atendimento
            // (extraindo do toString da fila, que já vem ordenado)
            String s = wl.toString(); // "[r1, r2, ...]"
            String inner = s.substring(1, s.length() - 1);
            for (String part : inner.split(", ")) {
                System.out.println("  - " + part);
            }
        }
    }
}
