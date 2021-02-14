package CASOPRACTICO;


import java.nio.charset.StandardCharsets;

import Nebrija.traductor.*;

public class TestAnalizadorSintactico {

	public static void main(String[] args) {
		//ComponenteLexico etiquetaLexica;
		//String programa= " int x; float w;";
		//String programa= "int a,b,c,d;float z,y,z;";
		//String programa= "void main { int a, b, c, d; float x; int[100] v; float x, y, z; int[5] k; a = b; a = y; }";
		String fichero="Test12Pila.txt";

		Lexico lexico= new Lexico(fichero,StandardCharsets.UTF_8);
		//Lexico lexico= new Lexico(programa);

		/*int c=0;
		System.out.println("Test lexico Basico:\n"+lexico.getPrograma() +"\n");

		do {
			etiquetaLexica=lexico.getComponenteLexico();
			System.out.println("<"+etiquetaLexica.toString()+">");
			c++;	
			}while (!etiquetaLexica.getEtiqueta().equals("end_program"));

		System.out.println("\nComponentes lexicos: "+c+",Lineas: "+lexico.getLineas());*/

		Analizador compilador= new Analizador(new Lexico(fichero,StandardCharsets.UTF_8));
		System.out.println("compilacion de sentancia de declaracion de Variables\n ");
		System.out.println(lexico.getPrograma()+"\n");
		compilador.programa();
		System.out.println(compilador.errores());
		System.out.println("Tabla de simbolos \n\n"+compilador.tablaSimbolos());
		if(fichero.contains("Pila")) {
			System.out.println("PILA \n\n"+compilador.Pila());
		}
	}
}
