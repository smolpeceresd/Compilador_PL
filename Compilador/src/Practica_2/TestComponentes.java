package Practica_2;

public class TestComponentes {
	public static void main(String[] args) {
		ComponentesLexicos componentesLexicos=new ComponentesLexicos("Lexico.txt");
		System.out.println(">  \t"+componentesLexicos.getEtiqueta(">"));
		System.out.println(">=  \t"+componentesLexicos.getEtiqueta(">="));
		System.out.println("<  \t"+componentesLexicos.getEtiqueta("<"));
		System.out.println("<=  \t"+componentesLexicos.getEtiqueta("<="));
		System.out.println("=  \t"+componentesLexicos.getEtiqueta("="));
		System.out.println("==  \t"+componentesLexicos.getEtiqueta("=="));
		System.out.println("!=  \t"+componentesLexicos.getEtiqueta("!="));
		System.out.println("for  \t"+componentesLexicos.getEtiqueta("for"));
		System.out.println("while  \t"+componentesLexicos.getEtiqueta("while"));
		System.out.println("main  \t"+componentesLexicos.getEtiqueta("main"));
		System.out.println("divide  \t"+componentesLexicos.getEtiqueta("/"));
		System.out.println("&&  \t"+componentesLexicos.getEtiqueta("&&"));
		System.out.println("||  \t"+componentesLexicos.getEtiqueta("||"));
		System.out.println("!  \t"+componentesLexicos.getEtiqueta("!"));
	}

}
