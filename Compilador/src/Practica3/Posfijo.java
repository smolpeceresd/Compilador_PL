package Practica3;

import java.util.Vector;


public class Posfijo {

	private String infijo=null;
	@SuppressWarnings("unused")
	private String posfijo=null;
	private Vector<Character> Pila=new Vector<Character>();
	private char [] simbolos={'+','-','*','/','(',')'};


	Posfijo(String infijo){
		this.infijo=infijo;
		this.posfijo=creaPosfijo();
	}

	public String creaPosfijo() {
		String deVuelta="";

		final String  infijo_copy=this.getInfijo();// creo una copia constante del infijo para poder operar con él

		for(int i=0;i<infijo_copy.length();i++) {
			//System.out.println("\nELEMENTO: "+infijo_copy.charAt(i));

			if(simbolos(infijo_copy.charAt(i))){// si pertenece a la tabla de simbolos
				deVuelta+=desapila_Apila(infijo_copy.charAt(i));// Apila o Desapila dependiendo de las normas
			}else {
				deVuelta+=infijo_copy.charAt(i);// si no es un simbolo , lo añado directamente
			}

			//verPila();// Solo muestra la pila , para ver como funciona
		}
		deVuelta+=desapila_Apila('0');// le paso 0 para indicar que es el final
		return deVuelta;
	}

	public String desapila_Apila(char elemento) {
		String deVuelta="";
		if(elemento==')') {// si es un ) cerrado ->
			do {

				deVuelta+=this.getPila().get(this.getPila().size()-1);// añade los operadores a deVuelta
				this.getPila().remove(this.getPila().size()-1);//Borra el operador introducido

			}while(this.getPila().get(this.getPila().size()-1)!='(');//lo hace hasta que encuentre el ( Abierto

			this.getPila().remove(this.getPila().size()-1);//elimina el  abierto (

		}else if((elemento != ')' && elemento !='(')// si no son corchetes 
				&& (this.getPila().size()>0)// no es el unico elemento 
				&&((this.getPila().get(this.getPila().size()-1)!='('))){// y el valor anterior no es un corchete

			deVuelta+=this.getPila().get(this.getPila().size()-1);// añade el anterior 
			// de momento no implica la prioridad de simbolos
			this.getPila().remove(this.getPila().size()-1);// elimina dicho simbolo
			this.getPila().add(elemento);// apila el nuevo elemento

		}else if(elemento=='0') {// si ya hemos terminado de leer el lexico
			while(this.getPila().size()>0) {

				deVuelta+=this.getPila().get(this.getPila().size()-1);// añade los operadores a deVuelta(si el size=0, lo devuelve vacio
				this.getPila().remove(this.getPila().size()-1);//Borra el operador introducido

			}// añade todos los elementos restantes hasta que este vacio
		}else {
			this.getPila().add(elemento);// si no cumple ninguna condicion para desapilar , lo apila
		}
		return deVuelta;
	}

	public Boolean simbolos(char c) {
		for(char elem:this.getSimbolos()) { if(c==elem)return true; }return false;
	}

	public char[] getSimbolos() {
		return simbolos;
	}

	public String getInfijo() {
		return infijo;
	}

	public Vector<Character> getPila() {
		return Pila;
	}

	public void setPila(Vector<Character> pila) {
		Pila = pila;
	}

	public String getPosfijo() {
		return posfijo;
	}

	public void verPila() {

		System.out.println("\nPILA");
		for(int j=this.getPila().size()-1;j>=0;j--) {

			System.out.println(this.getPila().get(j));
		}
	}
}
