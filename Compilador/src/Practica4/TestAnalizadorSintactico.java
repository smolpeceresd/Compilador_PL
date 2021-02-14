package Practica4;


import Nebrija.traductor.*;

public class TestAnalizadorSintactico {

	public static void main(String[] args) {
		ComponenteLexico etiquetaLexica;
		//String programa= " int x; float w;";
		 String programa= "int a,b,c,d;float z,y,z;";
		// String fichero="programa1";
		
		//Lexico lexico= new Lexico(fichero,StandardCharsets.UTF_8);
		Lexico lexico= new Lexico(programa);
		
		int c=0;
		System.out.println("Test lexico Basico:\n"+lexico.getPrograma() +"\n");
		
		do {
			etiquetaLexica=lexico.getComponenteLexico();
			System.out.println("<"+etiquetaLexica.toString()+">");
			c++;	
			}while (!etiquetaLexica.getEtiqueta().equals("end_program"));
		
		System.out.println("\nComponentes lexicos: "+c+",Lineas: "+lexico.getLineas());
		
		AnalizadorSintactico compilador= new AnalizadorSintactico(new Lexico(programa));
		 System.out.println("compilacion de sentancia de declaracion de Variables\n ");
		 System.out.println(programa+"\n");
		 compilador.declaracionTipos();
		 System.out.println("Tabla de simbolos \n\n"+compilador.tablaSimbolos());
	}
}
