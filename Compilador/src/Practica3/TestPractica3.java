package Practica3;

import Nebrija.traductor.*;


public class TestPractica3 {

	public static void main(String[] args) {
		ComponenteLexico etiquetaLexica;
		//String programa= " int x; float w;";
		/*		(25 * (2 + 2)) / 2 * 3
		 * 		(1*(2-3))+(4+5)
		 * 		(23+34)*45/6
		 */
		String programa="(25 * (2 + 2)) / 2 * 3";

		Lexico lexico= new Lexico(programa);

		int c=0;
		System.out.println("Test lexico Basico:\n"+lexico.getPrograma() +"\n");

		do {
			etiquetaLexica=lexico.getComponenteLexico();
			System.out.println("<"+etiquetaLexica.toString()+">");
			c++;	
		}while (!etiquetaLexica.getEtiqueta().equals("end_program"));

		System.out.println("\nComponentes lexicos: "+c+",Lineas: "+lexico.getLineas());

		Posfijo texto= new Posfijo(programa);

		System.out.println("Texto infijo: "+texto.getInfijo());
		System.out.println("Texto posfijo: "+texto.getPosfijo());

	}

}
