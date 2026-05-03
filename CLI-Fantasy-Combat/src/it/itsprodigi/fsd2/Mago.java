package Personaggio.src.it.itsprodigi.fsd2;

public class Mago extends Personaggio implements Interazione {
	private int magia;
	
	public Mago(String nome, String cognome, int eta, String sesso, String razza, int vita, int magia) {
		super(nome, cognome, eta, sesso, razza, vita);
		this.magia = magia; // Attributo specifico classe Mago (per attaccare)
	}

	// Metodo per far attaccare il mago
	@Override
    public void attacco(Personaggio nemico) {
        int danno = this.magia + (int)(Math.random() * 10); // Danno casuale
        nemico.setVita(nemico.getVita() - danno);
        System.out.println(this.getNome() + " attacca infliggendo " + danno + " danni.");
	}
	
	// Getter ---------------------------------
	public int getMagia() {
		return magia;
	}
		
	// Setter ---------------------------------
	public void setMagia(int magia) {
		this.magia = magia;
	}

}