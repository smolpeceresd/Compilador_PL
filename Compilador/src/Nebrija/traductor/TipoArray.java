package Nebrija.traductor;

public class TipoArray extends TipoDato{
	private int tama�o;
	
	public TipoArray(String tipo,int tama�o) {
		super(tipo);
		this.tama�o=tama�o;
	}

	public int getTama�o() {
		return tama�o;
	}
	
	public String toString() {
		return "array ("+super.toString() + " , "+this.getTama�o()+")";
	}
	
}
