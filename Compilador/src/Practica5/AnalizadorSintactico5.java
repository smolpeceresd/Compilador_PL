package Practica5;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import Nebrija.traductor.*;
/*
 * programa → void main { declaraciones }
	declaraciones → declaración-variable declaraciones | ep
	declaración-variable → tipo-vector id ; |
	tipo-primitivo lista-identificadores ;
	tipo-primitivo → int | float
	tipo-vector → int [ num ] | float [ num ]
	lista-identificadores → id más-identificadores
	más-identificadores → , id más-identificadores | ep
 */


public class AnalizadorSintactico5 {

	private ComponenteLexico componenteLexico;
	private Lexico lexico;
	private Hashtable<String, TipoDato> simbolos;

	//constructor
	public AnalizadorSintactico5(Lexico lexico) {
		this.simbolos = new Hashtable<String, TipoDato>();
		this.lexico = lexico;
		this.componenteLexico = this.lexico.getComponenteLexico();
	}

	public String tablaSimbolos() {
		String simbolos = "";
		Set<Map.Entry<String, TipoDato>> s = this.simbolos.entrySet();

		for(Map.Entry<String, TipoDato> m: s)
			simbolos = simbolos + "<'" + m.getKey() + "'," + m.getValue().toString() + "> \n";
		return simbolos;
	}

	public void programa() {
		compara("void");
		compara("main");
		compara("open_bracket");

		declaraciones();

		compara("closed_bracket");
		
	}

	private void declaraciones() {
		String etiqueta = this.componenteLexico.getEtiqueta();
		if(etiqueta.equals("int") || etiqueta.equals("float")) {
			declaracion();
			declaraciones();
		}
	}

	private void declaracion() {
		String tipo = tipo();
		int tamaño=1;


		if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")){
			// Vector de tipo primtivo int float

			compara("open_square_bracket");

			if(this.componenteLexico.getEtiqueta().equals("int")) {
				NumeroEntero numero= (NumeroEntero) this.componenteLexico;
				tamaño=numero.getValor();
			}

			compara("int");
			compara("closed_square_bracket");

			if(this.componenteLexico.getEtiqueta().equals("id")) {
				Identificador id= (Identificador) this.componenteLexico;
				simbolos.put(id.getLexema(), new TipoArray(tipo,tamaño));
			}
			compara("id");compara("semicolon");
		}else {
			// tio primitivo
			identificadores(tipo);
			compara("semicolon");
		}

		/*
		 * si despues del tipo viene un [ es una declaracion array
		 * en caso contrario es una lista de identificadores de tipos primitivo
		 * 
		 */
		/*identificadores(tipo);
		compara("semicolon");*/
	}

	private void identificadores(String tipo) {

		if(this.componenteLexico.getEtiqueta().equals("id")) {

			Identificador id = (Identificador) this.componenteLexico;

			this.simbolos.put(id.getLexema(), new TipoPrimitivo(tipo));

			compara("id");
			masIdentificadores(tipo);
		}
	}

	/*private void masIdentificadores(String tipo) {

		if(this.componenteLexico.getEtiqueta().equals("comma")) {
			compara("comma");

			Identificador id = (Identificador) this.componenteLexico;

			simbolos.put(id.getLexema(), tipo);

			compara("id");
			masIdentificadores(tipo);
		}else {
			//epsilon
		}
	}*/
	private void masIdentificadores(String tipo) {

		if(this.componenteLexico.getEtiqueta().equals("comma")) {
			compara("comma");

			if(this.componenteLexico.getEtiqueta().equals("id")) {
				Identificador id = (Identificador) this.componenteLexico;

				simbolos.put(id.getLexema(), new TipoPrimitivo(tipo));

				compara("id");

				masIdentificadores(tipo);

			}
		}
	}

	private String tipo() {
		String tipo = this.componenteLexico.getEtiqueta();

		if(tipo.equals("int")) {
			compara("int");
		}
		else if(tipo.equals("float")) {
			compara("float");
		}else {
			System.out.println("Error, se esperaba un int o float. ");
		}
		return tipo;
	}

	private void compara(String etiqueta) {

		if(this.componenteLexico.getEtiqueta().equals(etiqueta)) {
			this.componenteLexico = this.lexico.getComponenteLexico(); //avanza
		}else
			System.out.println("Error, se esperaba " + etiqueta);
	}

}