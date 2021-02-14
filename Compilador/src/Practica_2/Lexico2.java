package Practica_2;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.util.Hashtable;


public class Lexico2 {

	//private Hashtable<String, String> palabrasReservadas;
	//private PalabrasReservadas palabras;
	private ComponentesLexicos componentesLexicos;
	private int posiciones;
	private int lineas;
	private char caracter;
	private String programa;

	/*public Lexico (String programa) {

		this.posiciones = 0;
		this.lineas = 1;
		//this.palabra = new PalabrasReservadas("programa.txt");
		//this.programa = programa + this.palabras.getLexema("end_program");
		//la tabla de palabras reservadas almacena el lexema(clave) y el token (valor)
		this.palabrasReservadas = new Hashtable<String, String> ();
		this.palabrasReservadas.put("break", "break");
		this.palabrasReservadas.put("do", "do");
		this.palabrasReservadas.put("else", "else");
		this.palabrasReservadas.put("float", "float");
		this.palabrasReservadas.put("for", "for");
		this.palabrasReservadas.put("if", "if");
		this.palabrasReservadas.put("int", "int");
		this.palabrasReservadas.put("while", "while");

		this.programa = programa + (char) (0);

	}*/

	public Lexico2(String programa) {
		this.posiciones=0;
		this.lineas=1;
		this.componentesLexicos=new ComponentesLexicos("Lexico.txt");
		this.programa = programa + (char) (0);
	}
	public Lexico2 (String ficheroEntrada, Charset codificacion) {
		this (contenidoFichero(ficheroEntrada, codificacion));
	} 
	private char extraerCaracter() {
		return this.programa.charAt(this.posiciones++);
	}
	//extraerCaracter(char c) se usa para reconocer operadores con lexemas de 2 caracteres: &&, ||, <=, >=, ==, !=
	@SuppressWarnings("unused")
	private boolean extraerCaracter(char c) {
		if(this.posiciones < this.programa.length() -1) {
			this.caracter = extraerCaracter();

			if(c == this.caracter)
				return true;
			else {
				devuelveCaracter();

				return false;
			}
		}else

			return false;
	}

	private void devuelveCaracter() {
		this.posiciones--; // retrocede el puntero
	}
	public int getLineas() {
		return this.lineas;
	}
	public ComponenteLexico getComponenteLexico() {
		//el analizador lexico descarta los espacios (codigo 32),
		//tabuladores (cidigo 9) y saltos de líneas (codigos 10 y 13)
		while (true) {
			this.caracter = extraerCaracter();
			if(this.caracter == 0)
				return new ComponenteLexico ("end_program");
			else if (this.caracter == ' ' || (int) this.caracter == 9 || (int) this.caracter == 13)
				continue;
			else if ((int) this.caracter == 10)
				this.lineas++;
			else
				break;
		}
		//secuencias de digitos de numeros enteros y reales
		if(Character.isDigit(this.caracter)) {
			String numero = "";
			do {
				numero = numero + this.caracter;
				this.caracter = extraerCaracter();
			}while (Character.isDigit(this.caracter));
			if(this.caracter != '.') {
				devuelveCaracter();
				return new NumeroEntero(Integer.parseInt(numero));
				
			}
			do {
				numero = numero + this.caracter;
				this.caracter = extraerCaracter();
			}while (Character.isDigit(this.caracter));
			devuelveCaracter();
			return new NumeroReal(Float.parseFloat(numero));
			//return new ComponenteLexico ("float", Float.parseFloat(numero) + "");
		}
		//identificadores y palabras reservadas
		if(Character.isLetter(this.caracter)) {
			String lexema = "";

			do {

				lexema = lexema + this.caracter;

				this.caracter = extraerCaracter();

			}while (Character.isLetterOrDigit(this.caracter));

			devuelveCaracter();

			String etiquetaLexica=componentesLexicos.getEtiqueta(lexema);
			if(etiquetaLexica==null) {
				return new Identificador(lexema);
			}else {
				return new ComponenteLexico(etiquetaLexica);
			}
		}
		// operadores aritmrticos, relacionales, logicos y caracteres delimitadores
		String lexema = "", lexemaAlternativo, etiquetaAlternativa,etiquetaLexica;
		do {
			lexema = lexema + this.caracter;
			etiquetaLexica = componentesLexicos.getEtiqueta(lexema);
			if (etiquetaLexica == null)
				return new ComponenteLexico("invalid_char");
			else {
				lexemaAlternativo = lexema;
				this.caracter = extraerCaracter();
				lexemaAlternativo = lexemaAlternativo + this.caracter;
				etiquetaAlternativa =componentesLexicos.getEtiqueta(lexemaAlternativo);
				if (etiquetaAlternativa != null)
					etiquetaLexica = etiquetaAlternativa;
			}
		} while (etiquetaAlternativa != null);
		devuelveCaracter();
		return new ComponenteLexico(etiquetaLexica);

	}
	private static boolean existeFichero(String fichero) {
		File ficheroEntrada = new File (fichero);
		return ficheroEntrada.exists();
	}
	private static String contenidoFichero (String fichero, Charset codificacion) {
		String s = null;
		if(existeFichero(fichero)==true) {
			try {
				byte [] contenido = Files.readAllBytes(Paths.get(fichero));
				s = new String(contenido, codificacion);
			}catch (IOException e) { }
		}
		return s;
	}
	

	//////////////////////////////////Mi aportacion
	public String getPrograma() {
		return this.programa;
	}
}
