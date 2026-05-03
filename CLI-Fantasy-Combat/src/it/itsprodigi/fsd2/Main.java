package Personaggio.src.it.itsprodigi.fsd2;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creazione dei personaggi
        Guerriero guerriero = new Guerriero("Rinaldo", "Sgargabonzi", 38, "M", "Umano", 100, 15);
        Mago mago = new Mago("Goffredo", "Missiloni", 52, "M", "Elfo", 100, 17);
        Chierico chierico = new Chierico("Polonio", "Tronato", 45, "M", "Nano", 100, 14);

        // Creazione del Boss con input dell'utente
        System.out.println("=== CREAZIONE DEL BOSS ===");
        System.out.print("Nome del Boss: ");
        String nomeBoss = scanner.nextLine();
        

        Boss boss = new Boss(nomeBoss, "Er Magnifico", 45, "M", "umano", 100);

        // Avvio del combattimento
        Combattimento.combattimento(guerriero, mago, chierico, boss);

        scanner.close();
    }
}