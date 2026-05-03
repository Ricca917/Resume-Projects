package it.itsprodigi.fsd2.model;

import java.util.Random;

/**
 * Classe astratta base per tutti i personaggi (eroi e nemici).
 * Mantiene le statistiche D&D-like e i metodi comuni di combattimento.
 */
public abstract class Personaggio {

    protected String nome;
    protected String classe;
    protected String razza;

    // Statistiche vitali
    protected int vitaMax;
    protected int vitaAttuale;

    // Statistiche D&D (5-18, generate casualmente alla creazione)
    protected int forza;
    protected int destrezza;
    protected int costituzione;
    protected int intelligenza;
    protected int saggezza;
    protected int livello;

    // Stato difensivo: dimezza il prossimo danno ricevuto
    private boolean difendendo = false;

    protected static final Random rand = new Random();

    // ── Costruttore ───────────────────────────────────────────────────────────
    public Personaggio(String nome, String razza, int vitaBase) {
        this.nome        = nome;
        this.razza       = razza;
        this.livello     = 1;
        // Vita = vitaBase + bonus costituzione (assegnata dopo generaStats)
        generaStats();
        this.vitaMax     = vitaBase + (costituzione / 2);
        this.vitaAttuale = this.vitaMax;
    }

    /** Genera le statistiche base in modo casuale (range 5–18, come D&D). */
    protected void generaStats() {
        forza        = rand.nextInt(14) + 5;
        destrezza    = rand.nextInt(14) + 5;
        costituzione = rand.nextInt(14) + 5;
        intelligenza = rand.nextInt(14) + 5;
        saggezza     = rand.nextInt(14) + 5;
    }

    // ── Metodi astratti (ogni sottoclasse li implementa diversamente) ──────────
    public abstract void attacco(Personaggio bersaglio);
    public abstract String getAbilitaSpecialeNome();
    public abstract void abilitaSpeciale(Personaggio bersaglio);
    public abstract boolean haUsatoAbilita();

    // ── Meccaniche comuni ─────────────────────────────────────────────────────

    /**
     * Applica danno al personaggio.
     * Se sta difendendo, il danno viene dimezzato e la difesa si resetta.
     * @return danno effettivamente subito (utile per il log)
     */
    public int subisciDanno(int danno) {
        if (difendendo) {
            danno = Math.max(1, danno / 2);
            difendendo = false;
        }
        danno = Math.max(0, danno);
        vitaAttuale = Math.max(0, vitaAttuale - danno);
        return danno;
    }

    /** Cura il personaggio senza superare la vita massima. */
    public void cura(int quantita) {
        vitaAttuale = Math.min(vitaMax, vitaAttuale + quantita);
    }

    /** Imposta la posizione difensiva per il prossimo attacco subito. */
    public void setDifendendo(boolean difendendo) {
        this.difendendo = difendendo;
    }

    public boolean isDifendendo() { return difendendo; }

    public boolean isVivo() { return vitaAttuale > 0; }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getNome()       { return nome; }
    public String getClasse()     { return classe; }
    public String getRazza()      { return razza; }
    public int getVitaAttuale()   { return vitaAttuale; }
    public int getVitaMax()       { return vitaMax; }
    public int getForza()         { return forza; }
    public int getDestrezza()     { return destrezza; }
    public int getCostituzione()  { return costituzione; }
    public int getIntelligenza()  { return intelligenza; }
    public int getSaggezza()      { return saggezza; }
    public int getLivello()       { return livello; }

    @Override
    public String toString() {
        return String.format("%s il %s (%s) - HP: %d/%d | FOR:%d INT:%d SAG:%d",
                nome, classe, razza, vitaAttuale, vitaMax, forza, intelligenza, saggezza);
    }
}
