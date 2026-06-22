package util;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleUtils {
    private final Scanner scanner = new Scanner(System.in);

    public String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Entrada não pode ser vazia.");
        }
    }

    public int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    public BigDecimal lerDecimal(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                BigDecimal valor = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor decimal válido.");
            }
        }
    }

    /** Aguarda o usuário pressionar ENTER usando o scanner compartilhado. */
    public void aguardar() {
        try {
            scanner.nextLine();
        } catch (java.util.NoSuchElementException ignored) {
            // Fim de entrada em testes automatizados — ignora silenciosamente
        }
    }
}
