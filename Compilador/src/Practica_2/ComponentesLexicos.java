package Practica_2;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
public class ComponentesLexicos {
	/*
	 * ComponentesLexicos es una tabla Hash (String, String) que
	 * almacena los componentes l�xicos del lenguaje, definidos
	 * por parejas <lexema, etiquetaLexica> donde el lexema es
	 * la clave de la tabla y la etiqueta l�xica el valor
	 */
	private Hashtable<String, String> componentesLexicos;
	
	public ComponentesLexicos(String ficheroComponentesLexicos) {
		this.componentesLexicos = new Hashtable<String, String>();
		leeComponentesLexicos(this.componentesLexicos,ficheroComponentesLexicos);
	}
	
	
	public String getEtiqueta(String lexema) {//recibe un >
		return this.componentesLexicos.get(lexema);
	}
	public String getLexema(String etiquetaLexica) {
		String lexema = null;
		Set<Map.Entry<String, String>> s =this.componentesLexicos.entrySet();//Colocamelo para mapear 
		
		for(Map.Entry<String, String> m : s)// buscado en cada apartado de la tabla hash
			if (m.getValue().equals(etiquetaLexica)) {//si el valor (Recuerda que es clave,valor , ==valor
				lexema = m.getKey();
				break;
			}
		return lexema;
	}
	
	
	private static boolean existeFichero(String fichero) {
		File ficheroEntrada = new File (fichero);
		return ficheroEntrada.exists();
	}
	
	private static String etiqueta(String s) {
		return s.substring(0, s.indexOf("\t")).trim();
	}
	
	private static String lexema(String s) {
		return s.substring(s.lastIndexOf("\t") + 1).trim();
	}
	
	private static void leeComponentesLexicos(Hashtable<String, String>componentesLexicos, String ficheroComponentesLexicos) {
		if (existeFichero(ficheroComponentesLexicos)) {
			try {
				Scanner fichero = new Scanner(new File(ficheroComponentesLexicos));
				String componenteLexico, lexema, etiquetaLexica;
				while (fichero.hasNext()) {
					componenteLexico = fichero.nextLine();
					if (componenteLexico.length() > 0 &&componenteLexico.charAt(0) != '/') {
						lexema = lexema(componenteLexico);
						etiquetaLexica = etiqueta(componenteLexico);
						componentesLexicos.put(lexema, etiquetaLexica);
					}
				}
				fichero.close();
			} catch (IOException e) {}
		}
	}
}