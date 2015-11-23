package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Gestion {
	public static void main (String args[]){

		Funcionalidades funcionalidad = new Funcionalidades(); //Composicion de Funcionalidades.
		ComprobacionErrores comprobacion = new ComprobacionErrores(); //Composicion de ComprobacionErrores.

		cargarFicheroPersonas(); //Se carga el fichero personas.txt.

		System.out.println(funcionalidad.start()); //Herramienta
		System.out.println(comprobacion.end()); //Herramienta.

	}

	public static void cargarFicheroPersonas () {
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt");// Se crea un flujo de datos al fichero.
		} 
		catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada);// Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine(); // Se escanea la linea.
			if (linea.contains("*")) continue;
			else {} //Se recogen los datos de cada persona.
		}
		entrada.close();
	}	
}
/* -----NOTAS-----

1.- alumno.txt:
	1- Añadir "asignaturas superadas" y "docencia recibida".

 */
