package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} 
		catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine(); // Se escanea la linea.
			if (linea.contains("*")) continue;
			else {} //Se recogen los datos de cada persona.
		}
		entrada.close();
	
		Alumno alumno0 = new Alumno("44497152H", "Martin", "Puga Egea", new GregorianCalendar(1995, 01, 24), new GregorianCalendar(2000, 11, 20)); //Herramienta.
		Profesor profesor0 = new Profesor("34925064R", "Gisela", "Egea Rodriguez", new GregorianCalendar(1957, 8, 23), "Titular", "Electrónica", 24);
		Asignatura asignatura0 = new Asignatura(5, "Cálculo", "CALI", 1);
		GregorianCalendar horaGrupo0 = new GregorianCalendar();
		horaGrupo0.set(Calendar.HOUR, 11);
		horaGrupo0.set(Calendar.MINUTE, 45);
		GregorianCalendar horaGrupo00 = new GregorianCalendar();
		horaGrupo00.set(Calendar.HOUR, 13);
		horaGrupo00.set(Calendar.MINUTE, 50);

		Grupo grupo0 = new Grupo ('A',15,'L', horaGrupo0, horaGrupo00);
	System.out.println(alumno0.toString()); //Herramienta.
	System.out.println(profesor0.toString()); //Herramienta.
	System.out.println(asignatura0.toString()); //Herramienta.
	System.out.println(grupo0.toString()); //Herramienta.
	}	
}

/* -----NOTAS-----
1.- alumno.txt:
	1- Añadir a alumno: "asignaturas superadas" y "docencia recibida".
	2-Terminar de modelar las clases.
	3-Añadir a profesor: "docencia impartida" y "asignaturas coordinadas".
	4-Rerapar letras de constructores.
	5-int vs Integer.
	6- Modelar clase nota final
 */