package Nebrija.traductor;

public abstract class TipoDato {
	private String tipo;
	
	public TipoDato(String tipo) {
		this.tipo=tipo;
	}

	public String getTipo() {
		return tipo;
	}
	
	public String toString() {
		
	return 	this.getTipo();
	
	}
		
}