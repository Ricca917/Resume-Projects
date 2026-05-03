package Personaggio.src.it.itsprodigi.fsd2;

public class Combattimento  {
	
	public static void combattimento(Guerriero guerriero, Mago mago, Chierico chierico, Boss boss) {
	
	while (boss.getVita() > 0) {
		System.out.println("\n=== Combattimento ===");
		System.out.println("Vita Boss: " + boss.getVita());
	

	// Turno del Guerriero, controllo della vita del boss dopo attacco
	
	guerriero.attacco(boss);
	System.out.println("Vita del boss dopo l'attacco del Guerriero:" + boss.getVita());
	
	if (boss.getVita() <= 0) {
		System.out.println("Il Boss è Morto! Hai Vinto!");
		return;
	}
		
	// Turno del Mago, controllo della vita del boss dopo attacco
	
	mago.attacco(boss);
    System.out.println("Vita Boss dopo l' attacco del Mago: " + boss.getVita());
    
	
    if (boss.getVita() <= 0) {
        System.out.println("Il Boss è Morto! Hai vinto!");
        return;
    }

    // Turno del Chierico, controllo della vita del boss dopo attacco
    
    chierico.attacco(boss);
    System.out.println("Vita Boss dopo l'attacco del Chierico: " + boss.getVita());

    if (boss.getVita() <= 0) {
        System.out.println("Il Boss è Morto! Hai vinto!");
        return;
    }

	}
}

}