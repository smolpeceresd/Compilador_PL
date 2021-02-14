package Practica_2;

import java.nio.charset.StandardCharsets;

import Nebrija.traductor.*;

public class Test2 {

	public static void main(String[] args) {
		Nebrija.traductor.ComponenteLexico etiquetaLexica;
		//String programa= " int x; float w;";
		//String programa= "int k; for(int i=0;i<10;i=i+1) k=k*2;";
		String fichero="Programa1.txt";
		
		Lexico lexico= new Lexico(fichero,StandardCharsets.UTF_8);
		//Lexico lexico= new Lexico(programa);
		
		int c=0;
		System.out.println("Test lexico Basico:\n->\n"+lexico.getPrograma() +"\n<-\n");
		
		do {
			etiquetaLexica=lexico.getComponenteLexico();
			System.out.println("<"+etiquetaLexica.toString()+">");
			c++;	
			}while (!etiquetaLexica.getEtiqueta().equals("end_program"));
		
		System.out.println("\nComponentes lexicos: "+c+",Lineas: "+lexico.getLineas());
	}

}