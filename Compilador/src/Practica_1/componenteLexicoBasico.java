package Practica_1;

public class componenteLexicoBasico {
	private String etiqueta;
	private String valor;
	
	public componenteLexicoBasico(String etiqueta) {
		this.etiqueta=etiqueta;
		this.valor="";
	}
	
	public componenteLexicoBasico(String etiqueta,String valor) {
		this.etiqueta=etiqueta;
		this.valor=valor;
	}
	
	public String getEtiqueta() {
		return this.etiqueta;
	}
	
	public String toString() {
		if( this.valor.length()==0) {
			return "<" + this.etiqueta + ">";
		}else {
			return "<" +this.etiqueta + ","+this.valor+">";
		}
	}

}
