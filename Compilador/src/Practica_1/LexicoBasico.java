package Practica_1;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;


public class LexicoBasico {

	private Hashtable<String, String> palabrasReservadas;
	//private PalabrasReservadas palabras;
	private int posiciones;
	private int lineas;
	private char caracter;
	private String programa;

	public LexicoBasico (String programa) {

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

	}
	public LexicoBasico (String ficheroEntrada, Charset codificacion) {
		this (contenidoFichero(ficheroEntrada, codificacion));
	} 
	private char extraerCaracter() {
		return this.programa.charAt(this.posiciones++);
	}
	//extraerCaracter(char c) se usa para reconocer operadores con lexemas de 2 caracteres: &&, ||, <=, >=, ==, !=
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
	public componenteLexicoBasico getComponenteLexico() {
		//el analizador lexico descarta los espacios (codigo 32),
		//tabuladores (cidigo 9) y saltos de líneas (codigos 10 y 13)
		while (true) {
			this.caracter = extraerCaracter();
			if(this.caracter == 0)
				return new componenteLexicoBasico ("end_program");
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
				return new componenteLexicoBasico ("int", Integer.parseInt(numero) + "");
			}
			do {
				numero = numero + this.caracter;
				this.caracter = extraerCaracter();
			}while (Character.isDigit(this.caracter));
			devuelveCaracter();
			return new componenteLexicoBasico ("float", Float.parseFloat(numero) + "");
		}
		//identificadores y palabras reservadas
		if(Character.isLetter(this.caracter)) {
			String lexema = "";

			do {

				lexema = lexema + this.caracter;

				this.caracter = extraerCaracter();

			}while (Character.isLetterOrDigit(this.caracter));

			devuelveCaracter();

			if(this.palabrasReservadas.containsKey(lexema))

				return new componenteLexicoBasico ((String) this.palabrasReservadas.get(lexema));
			else

				return new componenteLexicoBasico("id", lexema);
		}
		// operadores sritmrticos, relacionales, logicos y caracteres delimitadores
		switch (this.caracter) {
		case '&':
			if(this.extraerCaracter('&')) {
				return new componenteLexicoBasico("and"); 
			}else {
				return new componenteLexicoBasico("bitwise_and");
			}

		case '|':

			if(this.extraerCaracter('|')) {
				return new componenteLexicoBasico("or");
			}else {
				return new componenteLexicoBasico("bitwise_or");
			}
		case '=':
			if(this.extraerCaracter('=')) {
				return new componenteLexicoBasico("equals");
			}else {
				return new componenteLexicoBasico("assignment");
			}
		case '!':
			if(this.extraerCaracter('=')) {
				return new componenteLexicoBasico("not_equals");
			}else {
				return new componenteLexicoBasico("not");
			}
		case '<':
			if(this.extraerCaracter('=')) {

				return new componenteLexicoBasico("less_equals");

			}else {
				return new componenteLexicoBasico("less_than");
			}
		case '>':
			if(this.extraerCaracter('=')) {

				return new componenteLexicoBasico("greater_equals");

			}else {
				return new componenteLexicoBasico("greater_than");
			}
		case '+': 
			return new componenteLexicoBasico("add");
		case '-':
			return new componenteLexicoBasico("subtract");
		case '*':
			return new componenteLexicoBasico("multiply");
		case '/':
			return new componenteLexicoBasico("divide");
		case '%':
			return new componenteLexicoBasico("remainder");
		case ';':
			return new componenteLexicoBasico("semicolon");
		case '(':
			return new componenteLexicoBasico("open_parenthesis");
		case ')':
			return new componenteLexicoBasico("closed_parenthesis");
		case '[':
			return new componenteLexicoBasico("open_square_braket");
		case ']':
			return new componenteLexicoBasico("closed_square_braket");

		case '{':
			return new componenteLexicoBasico("open_braquet");

		case '}':
			return new componenteLexicoBasico("closed_braquet");

			// resto de operadores y delimitadores
		default:
			 return new componenteLexicoBasico("invalid_char");
		}

	}
	private static boolean existeFichero(String fichero) {
		File ficheroEntrada = new File (fichero);
		return ficheroEntrada.exists();
	}
	private static String contenidoFichero (String fichero, Charset codificacion) {
		String s = null;
		if(existeFichero(fichero)) {
			try {
				byte [] contenido = Files.readAllBytes(Paths.get(fichero));
				s = new String(contenido, codificacion);
			}catch (IOException e) { }
		}
		return s;
	}
}
