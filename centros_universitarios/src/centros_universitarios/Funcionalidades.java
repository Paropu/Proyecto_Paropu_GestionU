package centros_universitarios;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Funcionalidades { // Esta clase contendra las funcionalidades que aparecen explicados en las especificaciones del proyecto y el control de errores.

	/*
	 * FUNCIONALIDADES INCLUIDAS EN LAS ESPECIFICACIONES DEL PROYECTO:
	 * Insertar persona
	 * Asignar coordinador
	 * Asignar carga docente
	 * Matricular alumno
	 * Asignar grupo
	 * Evaluar asignatura
	 * Obtener expediente del alumno
	 * Obtener calendario del profesor 
	 */

	/* METODOS */
	// ===== Funcionalidades =====
	public void insertarPersona(String linea, TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos) { // falta a�adir treemaps
		String[] lineaDesplegadaEspacios = linea.split(" ");
		String[] lineaDesplegadaComillas = linea.split("\"");

		// DATOS COMUNES
		String dni = lineaDesplegadaEspacios[2];
		if (!validarDNI(dni))
			guardarError("IP", "Dni incorrecto");
		String nombre = lineaDesplegadaComillas[1];
		String apellidos = lineaDesplegadaComillas[3];
		GregorianCalendar fechaNacimiento = stringToCalendar(linea.substring(linea.indexOf("/") - 2, linea.indexOf("/") + 8));
		if (validarFecha(fechaNacimiento))
			guardarError("IP", "Fecha incorrecta");

		System.out.println(dni);
		System.out.println(nombre);
		System.out.println(apellidos);
		System.out.println(fechaNacimiento.getTime());

		// DATOS ALUMNO
		if (lineaDesplegadaEspacios[1].compareTo("alumno") == 0) {
			if ((existeAlumno(alumnos, dni))) {
				guardarError("IP", "Alumno ya existente");
				return;
			}

			GregorianCalendar fechaIngreso = stringToCalendar(linea.substring(linea.indexOf("/") + 9, linea.indexOf("/") + 19));
			if (validarFecha(fechaIngreso))
				guardarError("IP", "Fecha incorrecta");
			if (validarEdad(fechaNacimiento, fechaIngreso))
				guardarError("IP", "Fecha ingreso incorrecta");
			String aux = linea.substring(linea.lastIndexOf("/"));
			String[] aux2 = aux.split(" ");
			String notaAux = aux2[1];
			float notaMedia = Float.parseFloat(notaAux);

			// Otros datos
			TreeMap<Integer, Grupo> docenciaRecibidaA = new TreeMap<Integer, Grupo>();
			TreeMap<Integer, Grupo> docenciaRecibidaB = new TreeMap<Integer, Grupo>();
			TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
			String[] arrayAsignaturasSuperadas = null;
			TreeMap<Integer, Asignatura> asignaturasMatriculadas = new TreeMap<Integer, Asignatura>();
			String[] arrayDocenciaRecibida = null;

			// Falta meter datos en treemap
			/*			Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso, docenciaRecibidaA, docenciaRecibidaB,
								asignaturasSuperadas, arrayAsignaturasSuperadas, asignaturasMatriculadas, arrayDocenciaRecibida);
						alumnos.put(dni, alumno);*/

			System.out.println(fechaIngreso.getTime());
			System.out.println(notaMedia);
		}

		// DATOS PROFESOR
		if (lineaDesplegadaEspacios[1].compareTo("profesor") == 0) {
			if ((existeProfesor(profesores, dni))) {
				guardarError("IP", "Profesor ya existente");
				return;
			}

			String departamento = lineaDesplegadaComillas[5];
			Integer horasDocenciaAsignables = Integer.parseInt(lineaDesplegadaComillas[6].trim());
			if (horasDocenciaAsignables < 0)
				guardarError("IP", "Numero de horas incorrecto");
			String categoria = null;
			String aux = linea.substring(linea.lastIndexOf("/") + 6, linea.lastIndexOf("/") + 7);
			if (aux.equals("a")) {
				categoria = "asociado";
				if (horasDocenciaAsignables > 15)
					guardarError("IP", "Numero de horas incorrecto");
			}
			if (aux.equals("t")) {
				categoria = "titular";
				if (horasDocenciaAsignables > 20)
					guardarError("IP", "Numero de horas incorrecto");
			} else {
				System.out.println("ERROR, no has escrito ni titular ni asociado");
			}

			// OTROS DATOS
			TreeMap<Integer, Grupo> docenciaImpartidaA = new TreeMap<Integer, Grupo>(); // VACIO
			TreeMap<Integer, Grupo> docenciaImpartidaB = new TreeMap<Integer, Grupo>(); // VACIO
			TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura>(); // VACIO. En principio vacio, luego se completa al cargar las asignaturas.
			String[] arrayDocenciaImpartida = null;

			// Falta meter datos en treemap
			/*	Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables,
						docenciaImpartidaA, docenciaImpartidaB, asignaturasCoordinadas, arrayDocenciaImpartida);
				profesores.put(dni, profesor);
			*/
			System.out.println(categoria);
			System.out.println(horasDocenciaAsignables);
			System.out.println(departamento);
		}
	}

	public void asignarCoordinador(String linea, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) { // Formato: AsignaCoordinador persona asignatura
		String[] campos = linea.split(" ");
		String persona = campos[1];
		String asignatura = campos[2];

		if (!(existeProfesor(profesores, persona))) {
			guardarError("ACOORD", "Profesor inexistente");
			return;
		}
		if (!(profesorTitular(profesores, persona))) {
			guardarError("ACOORD", "Profesor no titular");
			return;
		}
		if (!(existeAsignatura(asignaturas, asignatura))) {
			guardarError("ACOORD", "Asignatura inexistente");
			return;
		}
		if (coordinadorDosMaterias(profesores, persona)) {
			guardarError("ACOORD", "Profesor ya es coordinador de 2 materias");
			return;
		}
		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		while (it.hasNext()) {
			Integer idAsignatura = it.next();
			if (asignaturas.get(idAsignatura).getSiglas().contentEquals(asignatura)) {
				asignaturas.get(idAsignatura).setCoordinador(profesores.get(persona));
				profesores.get(persona).getAsignaturasCoordinadas().put(asignaturas.get(idAsignatura).getIdAsignatura(),
						asignaturas.get(idAsignatura));
			}
		}
		return;
	}

	public void asignarCargaDocente() {

	}

	public void matricularAlumno() {

	}

	public void asignarGrupo() {

	}

	public void evaluarAsignatura() {

	}

	public void obtenerExpedienteAlumno() {

	}

	public void obtenerCalendarioProfesor() {

	}

	// ===== Control de errores =====
	public void guardarError(String abreviatura, String texto) { // Metodo que permite la escritura en el fichero "avisos.txt".
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("avisos.txt", true);
			pw = new PrintWriter(fichero);
			pw.println(abreviatura + " -- " + texto);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void comandoIncorrecto() {
		guardarError("", "Comando incorrecto --");
	}

	public Boolean profesorTitular(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getCategoria().contains("titular"))
			return true;
		else
			return false;
	}

	public Boolean existeAlumno(TreeMap<String, Alumno> alumnos, String dni) {
		if (alumnos.containsKey(dni))
			return true;
		else
			return false;
	}

	public Boolean existeProfesor(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.containsKey(dni))
			return true;
		else
			return false;
	}

	public Boolean existeAsignatura(TreeMap<Integer, Asignatura> asignaturas, String siglas) {
		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		Boolean flag = false;
		while (it.hasNext())
			if (asignaturas.get(it.next()).getSiglas().contentEquals(siglas))
				flag = true;
		if (flag)
			return true;
		else
			return false;
	}

	public Boolean coordinadorDosMaterias(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getAsignaturasCoordinadas().size() == 2)
			return true;
		else
			return false;
	}

	public static GregorianCalendar stringToCalendar(String fechaEntrada) {
		String[] aux = fechaEntrada.split("/");
		GregorianCalendar fecha = new GregorianCalendar(Integer.parseInt(aux[2]), Integer.parseInt(aux[1]) - 1, Integer.parseInt(aux[0]));
		return fecha;
	}

	public static boolean validarFecha(GregorianCalendar fecha) {
		Calendar fechaMinima = new GregorianCalendar(1950, 1, 1);
		Calendar fechaMaxima = new GregorianCalendar(2020, 1, 1);
		fecha.setLenient(false);
		try {
			fecha.getTime();
		} catch (IllegalArgumentException excepcion) {
			System.out.println("ERROR: fecha introducida incorrecta");
			return true;
		}
		if (fecha.before(fechaMinima) || fecha.after(fechaMaxima))
			return true;
		else
			return false;
	}

	public static boolean validarEdad(GregorianCalendar fechaNacimiento, GregorianCalendar fechaInscripcion) {
		int anho1 = fechaNacimiento.get(GregorianCalendar.YEAR);
		int anho2 = fechaInscripcion.get(GregorianCalendar.YEAR);
		int n_years = 0;
		while (anho1 < anho2) {
			n_years++;
			anho1++;
		}
		if (n_years < 15 || n_years > 65)
			return true;
		else
			return false;
	}

	public static boolean validarDNI(String dni) {
		// COMPRUEBO SI EL TAMA�O ES CORRECTO
		if (dni.length() != 9)
			return false;

		// COMPRUEBO SI TIENE 8 NUMEROS
		String numero = dni.substring(0, 8);
		try {
			Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			return false;
		}

		// COMPRUEBO QUE SEA UNA LETRA (MAYUS O MINUS)
		char letra = dni.charAt(8);
		if (letra < 65 || letra > 122 || (letra > 90 && letra < 97))
			return false;

		return true;
	}

	/* CONSTRUCTORES */
	public Funcionalidades() {
	}
}