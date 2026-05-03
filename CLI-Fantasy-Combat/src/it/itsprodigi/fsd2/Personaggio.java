package Personaggio.src.it.itsprodigi.fsd2;

import java.util.Random;

abstract class Personaggio implements Interazione {
    private String nome;
    private String cognome;
    private int livello;
    private int eta;
    private String sesso;
    private String razza;
    private int vita;
    private int forza;
    private int destrezza;
    private int costituzione;
    private int intelligenza;
    private int saggezza;
    private int carisma;
    

    // Costruttore di oggetto "vuoto"
    public Personaggio() {
        this.nome = "";
        this.cognome = "";
        this.livello = 0;
        this.eta = 0;
        this.sesso = "";
        this.razza = "";
        this.vita = 100;
        setRandomCaratteristiche();
    }
    
    // Costruttore fornendo i dati di un personaggio
    public Personaggio(String nome, String cognome, int eta, String sesso, String razza, int vita) {
        this.nome = nome;
        this.cognome = cognome;
        this.livello = 1;  // Livello viene settato a 0 in modo da permettere la creazione di personaggi "vuoti"
        this.eta = eta;
        this.sesso = sesso;
        this.razza = razza;
        this.vita = vita;
        setRandomCaratteristiche();
    }
    
    // Getter per ottenere i dati del personaggio
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public int getLivello() {
        return livello;
    }
    public int getEta() {
        return eta;
    }
    public String getSesso() {
        return sesso;
    }
    public String getRazza() {
        return razza;
    }
    
    public int getVita() {
    	return vita;
    }
    public int getForza() {
        return forza;
    }
    public int getDestrezza() {
        return destrezza;
    }
    public int getCostituzione() {
        return costituzione;
    }
    public int getIntelligenza() {
        return intelligenza;
    }
    public int getSaggezza() {
        return saggezza;
    }
    public int getCarisma() {
        return carisma;
    }
    
    // Setter per la classe Personaggio
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public void setLivello(int livello) {
        this.livello = livello;
    }
    public void setEta(int eta) {
        this.eta = eta;
    }
    public void setSesso(String sesso) {
        this.sesso = sesso;
    }
    public void setRazza(String razza) {
        this.razza = razza;
    }
    public void setVita(int vita) {
    	this.vita = vita;
    }
    public void setForza(int forza) {
        this.forza = forza;
    }
        public void setDestrezza(int destrezza) {
        this.destrezza = destrezza;
    }
    public void setCostituzione(int costituzione) {
        this.costituzione = costituzione;
    }
    public void setIntelligenza(int intelligenza) {
        this.intelligenza = intelligenza;
    }
    public void setSaggezza(int saggezza) {
        this.saggezza = saggezza;
    }
    public void setCarisma(int carisma) {
        this.carisma = carisma;
    }


    // Funzioni Alternative, forse per implementazioni future



    // Funzione alternativa per calcolare la vita del personaggio (Costituzione * 10 + livello)
    public int calcoloVita() {
        return (costituzione * 10) + livello;
    }
    
    // Funzione alternativa eventuale per impostare le caratteristiche (forza, destrezza, costituzione, intelligenza, saggezza, carisma) randomicamente da 5 a 18
    public void setRandomCaratteristiche() {
    	Random rand = new Random();
        this.forza = (int) (Math.random() * 14) + 5; 
        this.destrezza = (int) (Math.random() * 14) + 5;
        this.costituzione = (int) (Math.random() * 14) + 5;
        this.intelligenza = (int) (Math.random() * 14) + 5;
        this.saggezza = (int) (Math.random() * 14) + 5;
        this.carisma = (int) (Math.random() * 14) + 5;
       }


}
