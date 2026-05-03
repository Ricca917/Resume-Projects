package it.itsprodigi.fsd2.ui;

/**
 * Gestisce l'output formattato nel terminale.
 * Usa codici ANSI per colori e stili (funziona nel terminale integrato di VS Code).
 */
public class TerminalUI {

    // ── Colori ANSI ──────────────────────────────────────────────────────────
    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";

    // ── Barra HP ─────────────────────────────────────────────────────────────
    /**
     * Restituisce una barra HP colorata tipo: [████████░░░░] 80/120
     * Verde > 50%, Giallo > 25%, Rosso altrimenti.
     */
    public static String barraHP(int vitaAttuale, int vitaMax) {
        int barLen = 20;
        int filled = vitaMax > 0
                ? Math.max(0, Math.min(barLen, (int) Math.round((double) vitaAttuale / vitaMax * barLen)))
                : 0;

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLen; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("]");

        double ratio = vitaMax > 0 ? (double) vitaAttuale / vitaMax : 0;
        String colore = ratio > 0.50 ? GREEN : ratio > 0.25 ? YELLOW : RED;

        return colore + bar + " " + vitaAttuale + "/" + vitaMax + RESET;
    }

    // ── Titolo ───────────────────────────────────────────────────────────────
    public static void titolo() {
        System.out.println(BOLD + YELLOW);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   ⚔   IL DUNGEON DEI CONDANNATI   ⚔    ║");
        System.out.println("  ║        Gioco Testuale a Turni            ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    // ── Separatori ───────────────────────────────────────────────────────────
    public static void separatore() {
        System.out.println(CYAN + "─".repeat(54) + RESET);
    }

    public static void separatoreGrosso() {
        System.out.println(BOLD + CYAN + "═".repeat(54) + RESET);
    }

    // ── Log combattimento ─────────────────────────────────────────────────────
    public static void logAttacco(String attaccante, String bersaglio, int danno) {
        System.out.printf(RED + "  ⚔  %-12s colpisce %-12s per %d danni%n" + RESET,
                attaccante, bersaglio, danno);
    }

    public static void logCura(String nome, int quantita) {
        System.out.printf(GREEN + "  ✚  %-12s recupera %d HP%n" + RESET, nome, quantita);
    }

    public static void logAbilita(String nome, String abilita) {
        System.out.printf(PURPLE + "  ✨ %-12s usa %s!%n" + RESET, nome, abilita);
    }

    public static void logDifesa(String nome) {
        System.out.printf(BLUE + "  🛡  %-12s adotta posizione difensiva (danno dimezzato)%n" + RESET, nome);
    }

    public static void logMorte(String nome) {
        System.out.printf(RED + BOLD + "  ☠  %s e' caduto!%n" + RESET, nome);
    }

    public static void logBossAbilita(String nomeBoss, String nomeAbilita) {
        System.out.printf(RED + BOLD + "  💀 %s usa %s!%n" + RESET, nomeBoss, nomeAbilita);
    }

    // ── Schermate finali ──────────────────────────────────────────────────────
    public static void vittoria() {
        System.out.println();
        System.out.println(BOLD + GREEN + "  ╔══════════════════════════════════════╗");
        System.out.println(         "  ║   🏆  VITTORIA! Il Boss e' sconfitto  ║");
        System.out.println(         "  ╚══════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public static void sconfitta() {
        System.out.println();
        System.out.println(BOLD + RED + "  ╔══════════════════════════════════════╗");
        System.out.println(          "  ║   💀  GAME OVER - Il party e' morto  ║");
        System.out.println(          "  ╚══════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public static void fuga() {
        System.out.println();
        System.out.println(YELLOW + "  🏃 Il party fugge nell'oscurita'... Game Over." + RESET);
        System.out.println();
    }
}
