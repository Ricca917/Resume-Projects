package it.itsprodigi.fsd2.model;

import java.util.List;

/**
 * Boss: nemico principale con statistiche potenziate.
 * Attacca un eroe casuale vivo ogni turno.
 * Ogni 3 turni usa Attacco Brutale (forza + intelligenza combinati).
 */
public class Boss extends Personaggio {

    private final String titolo;

    public Boss(String nome, String titolo) {
        super(nome, "Demone", 200);
        this.classe = "Boss";
        this.titolo = titolo;
        // Il boss e' piu' potente degli eroi
        this.forza        += 6;
        this.intelligenza += 4;
    }

    public String getTitolo() { return titolo; }

    // ── Attacco base ─────────────────────────────────────────────────────────
    @Override
    public void attacco(Personaggio bersaglio) {
        int danno = forza + rand.nextInt(16);  // piu' letale degli eroi
        int dannoPreso = bersaglio.subisciDanno(danno);
        System.out.printf("  [Boss]      %s attacca %s: %d danni!%n",
                nome, bersaglio.getNome(), dannoPreso);
        if (!bersaglio.isVivo()) {
            System.out.printf("  [Boss]      %s e' caduto!%n", bersaglio.getNome());
        }
    }

    // ── Logica turno boss (chiamata da Combattimento) ─────────────────────────
    /**
     * Il boss sceglie l'azione in base al numero di turno:
     * - turno multiplo di 3 → Attacco Brutale su un eroe casuale
     * - altrimenti → attacco normale su un eroe casuale
     */
    public void eseguiTurno(List<Personaggio> party, int turno) {
        List<Personaggio> vivi = party.stream().filter(Personaggio::isVivo).toList();
        if (vivi.isEmpty()) return;

        Personaggio bersaglio = vivi.get(rand.nextInt(vivi.size()));

        if (turno % 3 == 0) {
            abilitaSpeciale(bersaglio);
        } else {
            attacco(bersaglio);
        }
    }

    // ── Abilita' speciale ─────────────────────────────────────────────────────
    @Override
    public String getAbilitaSpecialeNome() { return "Attacco Brutale"; }

    @Override
    public void abilitaSpeciale(Personaggio bersaglio) {
        // Combina forza e intelligenza per un attacco devastante
        int danno = (forza + intelligenza) + rand.nextInt(21) + 5;
        int dannoPreso = bersaglio.subisciDanno(danno);
        System.out.printf("  [Boss]      %s usa ATTACCO BRUTALE su %s: %d danni!%n",
                nome, bersaglio.getNome(), dannoPreso);
        if (!bersaglio.isVivo()) {
            System.out.printf("  [Boss]      %s e' caduto!%n", bersaglio.getNome());
        }
    }

    // Il boss non ha limite di utilizzo abilita' (ogni 3 turni)
    @Override
    public boolean haUsatoAbilita() { return false; }
}
