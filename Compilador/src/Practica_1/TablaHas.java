package Practica_1;
import java.util.Hashtable;
public class TablaHas {

	public static void main(String[] args) {
		Hashtable<Integer, String> alumnos = new Hashtable<Integer, String>();
				alumnos.put(10, "Carlos");
				alumnos.put(20, "Luis");
				alumnos.put(30, "María");
				alumnos.put(40, "Pedro");
				
				System.out.println("La tabla alumnos " + alumnos + "\n");
				System.out.println("<30> " + alumnos.get(30));
	}

}
