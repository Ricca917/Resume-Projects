package it.itsprodigi.fsd2.model;

/**
 * Guerriero: specializzato in danni fisici.
 * Stat bonus: +4 Forza.
 * Abilita' speciale: Colpo Devastante (3x forza, usabile una volta).
 */
public class Guerriero extends Personaggio {

    private boolean abilitaUsata = false;

    public Guerriero(String nome) {
        super(nome, "Umano", 120);
        this.classe = "Guerriero";
        this.forza += 4;  // Bonus classe
    }

    @Override
    public void attacco(Personaggio bersaglio) {
        // Danno fisico: forza + dado da 10
        int danno = forza + rand.nextInt(11);
        int dannoPreso = bersaglio.subisciDanno(danno);
        System.out.printf("  [Guerriero] %s colpisce %s con la spada: %d danni%n",
                nome, bersaglio.getNome(), dannoPreso);
    }

    @Override
    public String getAbilitaSpecialeNome() { return "Colpo Devastante"; }

    @Override
    public void abilitaSpeciale(Personaggio bersaglio) {
        // Tripla forza + dado da 10 — devastante ma usabile una sola volta
        int danno = (forza * 3) + rand.nextInt(11);
        int dannoPreso = bersaglio.subisciDanno(danno);
        abilitaUsata = true;
        System.out.printf("  [Guerriero] %s usa COLPO DEVASTANTE su %s: %d danni!%n",
                nome, bersaglio.getNome(), dannoPreso);
    }

    @Override
    public boolean haUsatoAbilita() { return abilitaUsata; }
}
