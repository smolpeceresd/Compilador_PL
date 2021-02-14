package Practica_1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
public class LecturaFicheroCadena {
	private static boolean existeFichero(String fichero) {
		File ficheroEntrada = new File (fichero);
		return ficheroEntrada.exists();
	}
	private static String contenidoFichero(String fichero,Charset codificacion) {
		String s = null;
		if (existeFichero(fichero)) {
			try {
				byte [] contenido =
						Files.readAllBytes(Paths.get(fichero));
				s = new String(contenido, codificacion);
			} catch (IOException e) { }
		}
		return s;
	}
	public static void main(String[] args) {
		String ficheroEntrada = "E:\\Eclipse_clase\\Compilador\\Lexico.txt";
		String programa = contenidoFichero(ficheroEntrada,StandardCharsets.UTF_8);
		System.out.println(programa);
	}
}
