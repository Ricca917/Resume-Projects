package Personaggio.src.it.itsprodigi.fsd2;

public class Chierico extends Personaggio implements Interazione {
	private int fede;
	
	public Chierico(String nome, String cognome, int eta, String sesso, String razza,int vita, int fede) {
		super(nome, cognome, eta, sesso, razza, vita);
		this.fede = fede; // Attributo specifico della classe Chierico (si usa per l'attacco)
	}

	// Metodo per far attaccare il Chierico
	@Override
    public void attacco(Personaggio nemico) {
        int danno = this.fede + (int)(Math.random() * 10); // Danno casuale
        nemico.setVita(nemico.getVita() - danno);
        System.out.println(this.getNome() + " attacca infliggendo " + danno + " danni.");
	}
	
	// Getter------------------------------------------
	public int getFede() {
		return fede;
	}

	// Setter------------------------------------------
	
	public void setFede(int fede) {
		this.fede = fede;
	}

}