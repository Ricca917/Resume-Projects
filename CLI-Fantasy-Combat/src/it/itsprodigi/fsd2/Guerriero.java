package Personaggio.src.it.itsprodigi.fsd2;

public class Guerriero extends Personaggio implements Interazione{
	private int melee;
	
	public Guerriero(String nome, String cognome, int eta, String sesso, String razza, int vita, int melee) {
		super(nome, cognome, eta, sesso, razza, vita);
		this.melee = melee ; // Attributo specifico classe Guerriero (per attaccare)
	}
	
	// Metodo per far attaccare il Guerriero
	@Override
    public void attacco(Personaggio nemico) {
        int danno = this.melee + (int)(Math.random() * 10); // Danno casuale
        nemico.setVita(nemico.getVita() - danno);
        System.out.println(this.getNome() + " attacca infliggendo " + danno + " danni.");
	}

	// Getter ---------------------------	
	public int getMelee() {
		return melee;
	}

	// Setter ---------------------------
	public void setMelee(int melee) {
		this.melee = melee;
	}

}
