package it.itsprodigi.fsd2.model;

/**
 * Mago: danni magici elevati, punti vita bassi.
 * Stat bonus: +5 Intelligenza.
 * Abilita' speciale: Palla di Fuoco (2x intelligenza + bonus, usabile una volta).
 */
public class Mago extends Personaggio {

    private boolean abilitaUsata = false;

    public Mago(String nome) {
        super(nome, "Elfo", 80);
        this.classe = "Mago";
        this.intelligenza += 5;  // Bonus classe
    }

    @Override
    public void attacco(Personaggio bersaglio) {
        // Danno magico: intelligenza + dado da 8
        int danno = intelligenza + rand.nextInt(9);
        int dannoPreso = bersaglio.subisciDanno(danno);
        System.out.printf("  [Mago]      %s lancia un Dardo Arcano su %s: %d danni%n",
                nome, bersaglio.getNome(), dannoPreso);
    }

    @Override
    public String getAbilitaSpecialeNome() { return "Palla di Fuoco"; }

    @Override
    public void abilitaSpeciale(Personaggio bersaglio) {
        // Doppia intelligenza + grande dado — massimo potere magico
        int danno = (intelligenza * 2) + rand.nextInt(21) + 10;
        int dannoPreso = bersaglio.subisciDanno(danno);
        abilitaUsata = true;
        System.out.printf("  [Mago]      %s lancia PALLA DI FUOCO su %s: %d danni!%n",
                nome, bersaglio.getNome(), dannoPreso);
    }

    @Override
    public boolean haUsatoAbilita() { return abilitaUsata; }
}
