package it.itsprodigi.fsd2.model;

import java.util.List;

/**
 * Chierico: danni sacri medi + supporto alla guarigione.
 * Stat bonus: +4 Saggezza.
 * Abilita' speciale: Guarigione di Massa (cura tutto il party, usabile una volta).
 *
 * Nota: abilitaSpeciale() riceve il boss come parametro ma lo ignora —
 * l'effetto e' sul party. Il riferimento al party viene passato via setParty().
 */
public class Chierico extends Personaggio {

    private boolean abilitaUsata = false;
    private List<Personaggio> party;

    public Chierico(String nome) {
        super(nome, "Nano", 100);
        this.classe = "Chierico";
        this.saggezza += 4;  // Bonus classe
    }

    /** Deve essere chiamato dopo la composizione del party. */
    public void setParty(List<Personaggio> party) {
        this.party = party;
    }

    @Override
    public void attacco(Personaggio bersaglio) {
        // Danno sacro: saggezza + dado da 8
        int danno = saggezza + rand.nextInt(9);
        int dannoPreso = bersaglio.subisciDanno(danno);
        System.out.printf("  [Chierico]  %s colpisce %s con la mazza sacra: %d danni%n",
                nome, bersaglio.getNome(), dannoPreso);
    }

    @Override
    public String getAbilitaSpecialeNome() { return "Guarigione di Massa"; }

    @Override
    public void abilitaSpeciale(Personaggio bersaglio) {
        // Cura ogni membro vivo del party (bersaglio ignorato per questa abilita')
        int curaPerMembro = saggezza * 2 + rand.nextInt(16);
        abilitaUsata = true;
        if (party != null) {
            for (Personaggio p : party) {
                if (p.isVivo()) {
                    p.cura(curaPerMembro);
                    System.out.printf("  [Chierico]  %s cura %s: +%d HP%n",
                            nome, p.getNome(), curaPerMembro);
                }
            }
        } else {
            // Fallback: auto-cura se il riferimento al party non e' disponibile
            cura(curaPerMembro);
            System.out.printf("  [Chierico]  %s si auto-cura: +%d HP%n", nome, curaPerMembro);
        }
    }

    @Override
    public boolean haUsatoAbilita() { return abilitaUsata; }
}
