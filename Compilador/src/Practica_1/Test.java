package Practica_1;

public class Test {

	public static void main(String[] args) {
		componenteLexicoBasico etiquetaLexica;
		//String programa= " int x; float w;";
		 String programa= "int k; for(int i=0;i<10;i=i+1) k=k*2;";
		
		LexicoBasico lexico= new LexicoBasico(programa);
		
		int c=0;
		System.out.println("Test lexico Basico: { "+programa +" } \n");
		
		do {
			etiquetaLexica=lexico.getComponenteLexico();
			System.out.println(etiquetaLexica.toString());
			c++;	
			}while (!etiquetaLexica.getEtiqueta().equals("end_program"));
		
		System.out.println("\nComponentes lexicos: "+c+",Lineas: "+lexico.getLineas());
	}

}
