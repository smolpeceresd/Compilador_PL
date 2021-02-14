package Nebrija.traductor;

public class TipoArray extends TipoDato{
	private int tamaño;
	
	public TipoArray(String tipo,int tamaño) {
		super(tipo);
		this.tamaño=tamaño;
	}

	public int getTamaño() {
		return tamaño;
	}
	
	public String toString() {
		return "array ("+super.toString() + " , "+this.getTamaño()+")";
	}
	
}
