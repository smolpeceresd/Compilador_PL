package Practica_1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class LecturaFicheroTexto {

	public static void main(String[] args) throws IOException {

		String idFichero = "E:\\Eclipse_clase\\Aplicacion_ProAv\\es.txt";
		File ficheroEntrada = new File (idFichero);

		if (ficheroEntrada.exists()) {

			Scanner datosFichero = new Scanner(ficheroEntrada);
			while (datosFichero.hasNext()) {

				String [] numerosFichero =
						datosFichero.nextLine().split(",");
				for (int i=0; i < numerosFichero.length; i++)
					System.out.print(numerosFichero[i] + "\t");
				System.out.println("");
			}
			datosFichero.close();
		}
		else
			System.out.println("¡El fichero no existe!");
	}
}
