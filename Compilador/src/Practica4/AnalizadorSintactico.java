package Practica4;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import Nebrija.traductor.*;
/*declaraciones => declaracion declaraciones | epsilon
 * declaracion => tipo declaraciones ; 
 * tipo => int | float
 * identificadores => id mas-identificadores
 * mas-identificadores => , id mas-identificadores | epsilon
 */


public class AnalizadorSintactico {

	private ComponenteLexico componenteLexico;
	private Lexico lexico;
	private Hashtable<String, String> simbolos;
	
	//constructor
	public AnalizadorSintactico(Lexico lexico) {
		this.simbolos = new Hashtable<String, String>();
		this.lexico = lexico;
		this.componenteLexico = this.lexico.getComponenteLexico();
	}
	
	public String tablaSimbolos() {
		String simbolos = "";
		Set<Map.Entry<String, String>> s = this.simbolos.entrySet();
		
		for(Map.Entry<String, String> m: s)
			simbolos = simbolos + "<'" + m.getKey() + "'," + m.getValue().toString() + "> \n";
		return simbolos;
	}

	public void declaracionTipos() {
		declaraciones();
	}
	
	private void declaraciones() {
		String etiqueta = this.componenteLexico.getEtiqueta();
		System.out.println("\tetiqueta"+this.componenteLexico.toString());
		if(etiqueta.equals("int") || etiqueta.equals("float")) {
			declaracion();
			declaraciones();
		}
	}
	
	private void declaracion() {
		String tipo = tipo();
		
		identificadores(tipo);
		compara("semicolon");
	}

	private void identificadores(String tipo) {
		
		if(this.componenteLexico.getEtiqueta().equals("id")) {
			
			Identificador id = (Identificador) this.componenteLexico;
			
			this.simbolos.put(id.getLexema(), tipo);
			
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
				
				simbolos.put(id.getLexema(), tipo);
				
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