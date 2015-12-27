package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Gestion {

	/* MAIN */

	public static void main(String args[]) {

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		// NOTA IMPORTANTE:No se hace en un metodo directamente, hay que hacerlo por partes para poder cargar todos los datos sin problemas. No se puede cargar toda la info de golpe, ya que se necesita relacionar las clases.
		profesores = cargarProfesores(); // Carga toda la informacioon de los profesores excepto la docencia impartida, que requiere que existan los grupos relacionados.
		alumnos = cargarAlumnos(); // Carga toda la informacion de los alumnos excepto las asignaturas aprobadas y la docencia recibida.
		asignaturas = cargarAsignaturas(profesores); // Carga toda la info de las asignaturas en dos fases. En la primera fase carga los datos básico y en la segunda, los prerrequisitos.
		cargarAsignaturasSuperadas(alumnos, asignaturas); // Actualiza la informacion de las asignaturas superadas de los alumnos.
		cargarDocenciaImpartida(profesores, asignaturas); // Actualiza la informacion de la docencia impartida por los profesores.
		cargarDocenciaRecibida(alumnos, asignaturas); // Actualiza la informacion de la docencia recibida por los alumnos.
		ejecucion(profesores, alumnos, asignaturas);
		guardarFicheroPersonas(profesores, alumnos, asignaturas); // Guarda la información de las personas contenidas en el sistema en el fichero "personas.txt".
		guardarFicheroAsignaturas(profesores, asignaturas); // Guarda la informacion de las asignaturas contenidas en el sistema en el fichero "asignaturas.txt".

	}

	/* METODOS */

	public static TreeMap<String, Profesor> cargarProfesores() {

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("profesor")) { // Se recogen los datos del profesor.
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				String categoria = entrada.nextLine();
				String departamento = entrada.nextLine();
				Integer horasDocenciaAsignables = Integer.parseInt(entrada.nextLine());
				String[] arrayDocenciaImpartida = entrada.nextLine().split("; ");// Carga de docencia impartida por el profesor.
				TreeMap<Integer, Grupo> docenciaImpartidaA = new TreeMap<Integer, Grupo>(); // VACIO
				TreeMap<Integer, Grupo> docenciaImpartidaB = new TreeMap<Integer, Grupo>(); // VACIO
				TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura>(); // VACIO. En principio vacio, luego se completa al cargar las asignaturas.
				Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables,
						docenciaImpartidaA, docenciaImpartidaB, asignaturasCoordinadas, arrayDocenciaImpartida);
				profesores.put(dni, profesor);
			} else { // Se salta el bloque del alumno.
				int i;
				for (i = 0; i < 7; i++)
					linea = entrada.nextLine();
			}
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();
		return profesores;
	}

	public static TreeMap<String, Alumno> cargarAlumnos() {

		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("alumno")) { // Se recogen los datos del alumno.
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				linea = entrada.nextLine();
				fecha = linea.split("/");
				GregorianCalendar fechaIngreso = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				String[] arrayAsignaturasSuperadas = entrada.nextLine().split("; ");// OMISION de asignaturas superadas del alumno.
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
				String[] arrayDocenciaRecibida = entrada.nextLine().split("; ");// Carga de la docencia recibida por el alumno.
				TreeMap<Integer, Grupo> docenciaRecibidaA = new TreeMap<Integer, Grupo>();
				// TreeMap<Integer, Grupo> docenciaRecibidaA =null;//--------------------------------------
				TreeMap<Integer, Grupo> docenciaRecibidaB = new TreeMap<Integer, Grupo>();
				// TreeMap<Integer, Grupo> docenciaRecibidaB =null;
				TreeMap<Integer, Asignatura> asignaturasMatriculadas = new TreeMap<Integer, Asignatura>();
				Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso, docenciaRecibidaA, docenciaRecibidaB,
						asignaturasSuperadas, arrayAsignaturasSuperadas, asignaturasMatriculadas, arrayDocenciaRecibida);
				alumnos.put(dni, alumno);
			} else { // Se salta el bloque del profesor.
				int i;
				for (i = 0; i < 8; i++)
					linea = entrada.nextLine();
			}
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();
		return alumnos;
	}

	public static TreeMap<Integer, Asignatura> cargarAsignaturas(TreeMap<String, Profesor> profesores) {
		// PRIMERA FASE.
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>(); // TreeMap que contendrá las asignaturas
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("asignaturas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"asignaturas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			Integer idAsignatura = Integer.parseInt(entrada.nextLine());
			String nombre = entrada.nextLine();
			String siglas = entrada.nextLine();
			Integer curso = Integer.parseInt(entrada.nextLine());
			String dniCoordinador = entrada.nextLine();// Carga del coordinador de la asignatura.
			String caracterVacio = "";
			String[] arrayPrerrequisitos = entrada.nextLine().split(", ");
			TreeMap<Integer, Asignatura> prerrequisitos = new TreeMap<Integer, Asignatura>();
			Asignatura asignatura = new Asignatura(idAsignatura, nombre, siglas, curso, new Profesor(), prerrequisitos,
					new TreeMap<Integer, Grupo>(), new TreeMap<Integer, Grupo>(), arrayPrerrequisitos);// AÑADIR posteriormente los grupos A y B.
			if (dniCoordinador.compareTo(caracterVacio) != 0) {
				Profesor coordinador = profesores.get(dniCoordinador);
				asignatura.setCoordinador(coordinador);// Se le asigna un coordinador a la asignatura.
				profesores.get(dniCoordinador).getAsignaturasCoordinadas().put(idAsignatura, asignatura); // AÑADE asignatura coordinada al profesor.
			}
			TreeMap<Integer, Grupo> gruposA = new TreeMap<Integer, Grupo>(); // CARGAR gruposA
			linea = entrada.nextLine(); // Formato: ID_grupo dia horaini horafin
			String[] arrayGruposA = linea.split("; ");

			int i;
			if (arrayGruposA[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < arrayGruposA.length; i++) {
					String[] grupo = arrayGruposA[i].split(" ");
					Integer idGrupo = Integer.parseInt(grupo[0]);
					String dia = grupo[1];
					Integer horaInicio = Integer.parseInt(grupo[2]);
					Integer horaFin = Integer.parseInt(grupo[3]);
					Grupo grupoA = new Grupo("A", idGrupo, dia, horaInicio, horaFin, asignatura);// AÑADIR asignatura posteriormente.
					gruposA.put(idGrupo, grupoA);// Se añade el grupo al Treemap de grupos A de la asignatura.
				}
			}
			TreeMap<Integer, Grupo> gruposB = new TreeMap<Integer, Grupo>(); // CARGAR gruposB.
			linea = entrada.nextLine(); // Formato: ID_grupo dia horaini horafin
			String[] arrayGruposB = linea.split("; ");
			if (arrayGruposB[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < arrayGruposB.length; i++) {
					String[] grupo = arrayGruposB[i].split(" ");
					Integer idGrupo = Integer.parseInt(grupo[0]);
					String dia = grupo[1];
					Integer horaInicio = Integer.parseInt(grupo[2]);
					Integer horaFin = Integer.parseInt(grupo[3]);
					Grupo grupoA = new Grupo("B", idGrupo, dia, horaInicio, horaFin, asignatura);// AÑADIR asignatura posteriormente.
					gruposB.put(idGrupo, grupoA);// Se añade el grupo al Treemap de grupos B de la asignatura.
				}
			}
			asignatura.setGruposA(gruposA);
			asignatura.setGruposB(gruposB);
			asignaturas.put(asignatura.getIdAsignatura(), asignatura); // Se añade la asignatura al TreeMap de asignaturas.
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();

		// SEGUNDA FASE - ACTUALIZACION DE PRERREQUISITOS
		Set<Integer> setAsignaturas = asignaturas.keySet(); // CREACION de un Set con las claves de las asignaturas en el TreeMap asignaturas.
		Iterator<Integer> it = setAsignaturas.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Asignatura asignatura = asignaturas.get(it.next()); // Se recoge la asignatura del TreeMap mediante la key
			String caracterVacio = "";
			if (asignatura.getArrayPrerrequisitos()[0].compareTo(caracterVacio) != 0) { // Se cargan los prerrequisitos solo en caso de existir alguna dependencia.
				TreeMap<Integer, Asignatura> nuevosPrerrequisitos = new TreeMap<Integer, Asignatura>(); // Nuevo TreeMap donde se guardaran los nuevos prerrequisitos, para posteriormente añadirlos a la asignatura mediante un setPrerrequisitos().
				int i;
				for (i = 0; i < asignatura.getArrayPrerrequisitos().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					nuevosPrerrequisitos.put(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i]),
							asignaturas.get(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i])));
				}
				asignatura.setPrerrequisitos(nuevosPrerrequisitos);
				System.out.println();
			}
		}
		return asignaturas;
	}

	public static void cargarAsignaturasSuperadas(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setAlumnos = alumnos.keySet(); // CREACION de un Set con las claves de los alumnos en el TreeMap alumnos.
		Iterator<String> it = setAlumnos.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Alumno alumno = alumnos.get(it.next()); // Se recoge el alumno del TreeMap mediante la key
			String caracterVacio = "";
			if (alumno.getArrayAsignaturasSuperadas()[0].compareTo(caracterVacio) != 0) { // Se cargan los prerrequisitos solo en caso de existir alguna dependencia.
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>(); // Nuevo TreeMap donde se guardaran las asignaturas superadas, para posteriormente añadirlos al alumno mediante un set()..
				int i;
				for (i = 0; i < alumno.getArrayAsignaturasSuperadas().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = alumno.getArrayAsignaturasSuperadas()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String cursoAcademico = campos[1];
					Float nota = Float.parseFloat(campos[2]);
					asignaturasSuperadas.put(idAsignatura,
							new NotaFinal(idAsignatura, cursoAcademico, nota, asignaturas.get(idAsignatura)));
				}
				alumno.setAsignaturasSuperadas(asignaturasSuperadas);
			}
		}
	}

	public static void cargarDocenciaImpartida(TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setProfesores = profesores.keySet(); // CREACION de un Set con las claves de los profesores en el TreeMap profesores.
		Iterator<String> it = setProfesores.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Profesor profesor = profesores.get(it.next()); // Se recoge el profesor del TreeMap mediante la key
			String caracterVacio = "";
			if (profesor.getArrayDocenciaImpartida()[0].compareTo(caracterVacio) != 0) {
				TreeMap<Integer, Grupo> docenciaImpartidaA = new TreeMap<Integer, Grupo>(); // Nuevo TreeMap donde se guardaran los grupos impartidos por el profesor, para posteriormente añadirlos al profesor mediante un set()..
				TreeMap<Integer, Grupo> docenciaImpartidaB = new TreeMap<Integer, Grupo>();
				int i;
				for (i = 0; i < profesor.getArrayDocenciaImpartida().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = profesor.getArrayDocenciaImpartida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String tipoGrupo = campos[1];
					Integer idGrupo = Integer.parseInt(campos[2]);
					if (tipoGrupo.contains("A"))
						docenciaImpartidaA.put(idGrupo, asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
					else
						docenciaImpartidaB.put(idGrupo, asignaturas.get(idAsignatura).getGruposB().get(idGrupo));
				}
				profesor.setDocenciaImpartidaA(docenciaImpartidaA);
				profesor.setDocenciaImpartidaB(docenciaImpartidaB);
			}
		}
	}

	public static void cargarDocenciaRecibida(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		Set<String> setAlumnos = alumnos.keySet(); // CREACION de un Set con las claves de los alumnos en el TreeMap alumnos.
		Iterator<String> it = setAlumnos.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Alumno alumno = alumnos.get(it.next()); // Se recoge el alumno del TreeMap mediante la key
			TreeMap<Integer, Grupo> docenciaRecibidaA = new TreeMap<Integer, Grupo>(); // Nuevo TreeMap donde se guardaran los grupos a los que asiste el alumno, para posteriormente añadirlos al alumno mediante un set().
			TreeMap<Integer, Grupo> docenciaRecibidaB = new TreeMap<Integer, Grupo>();
			TreeMap<Integer, Asignatura> asignaturasSinGrupo = new TreeMap<Integer, Asignatura>();
			int i;
			String caracterVacio = "";
			if (alumno.getArrayDocenciaRecibida()[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < alumno.getArrayDocenciaRecibida().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = alumno.getArrayDocenciaRecibida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					if (campos.length > 1) {
						String tipoGrupo = campos[1];
						Integer idGrupo = Integer.parseInt(campos[2]);
						if (tipoGrupo.contains("A"))
							docenciaRecibidaA.put(idGrupo, asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
						else
							docenciaRecibidaB.put(idGrupo, asignaturas.get(idAsignatura).getGruposB().get(idGrupo));
					} else {// asignaturasSinGrupo -------------------
						asignaturasSinGrupo.put(idAsignatura, asignaturas.get(idAsignatura));// NEW
					}
					alumno.getAsignaturasMatriculadas().put(idAsignatura, asignaturas.get(idAsignatura));
				}
			}
			alumno.setDocenciaRecibidaA(docenciaRecibidaA);
			alumno.setDocenciaRecibidaB(docenciaRecibidaB);
			alumno.setAsignaturasSinGrupo(asignaturasSinGrupo);
		}
	}

	public static void guardarFicheroPersonas(TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos,
			TreeMap<Integer, Asignatura> asignaturas) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("salidapersonas.txt");
			pw = new PrintWriter(fichero);
			// Guardado de profesores.
			Set<String> setProfesores = profesores.keySet();
			Iterator<String> it0 = setProfesores.iterator();
			while (it0.hasNext()) {
				Profesor profesor = profesores.get(it0.next());
				pw.println("profesor");
				pw.println(profesor.getDni());
				pw.println(profesor.getNombre());
				pw.println(profesor.getApellidos());
				pw.println(profesor.getfechaNacimiento().get(Calendar.DATE) + "/" + profesor.getfechaNacimiento().get(Calendar.MONTH) + "/"
						+ profesor.getfechaNacimiento().get(Calendar.YEAR));
				pw.println(profesor.getCategoria());
				pw.println(profesor.getDepartamento());
				pw.println(profesor.getHorasDocenciaAsignables());
				Set<Integer> setDocenciaImpartidaA = profesor.getDocenciaImpartidaA().keySet();
				Iterator<Integer> it1 = setDocenciaImpartidaA.iterator();
				Set<Integer> setDocenciaImpartidaB = profesor.getDocenciaImpartidaB().keySet();
				Iterator<Integer> it2 = setDocenciaImpartidaB.iterator();
				if (!setDocenciaImpartidaA.isEmpty()) {
					while (it1.hasNext()) {
						Grupo grupoA = profesor.getDocenciaImpartidaA().get(it1.next());
						pw.print(grupoA.getAsignatura().getIdAsignatura() + " " + grupoA.getTipoGrupo() + " " + grupoA.getIdGrupo());
						if (it1.hasNext())
							pw.print("; ");
						else if (setDocenciaImpartidaB.isEmpty()) {
							pw.print("\n");
						} else
							pw.print("; ");
					}
				}
				if (!setDocenciaImpartidaB.isEmpty()) {
					while (it2.hasNext()) {
						Grupo grupoB = profesor.getDocenciaImpartidaB().get(it2.next());
						pw.print(grupoB.getAsignatura().getIdAsignatura() + " " + grupoB.getTipoGrupo() + " " + grupoB.getIdGrupo());
						if (it2.hasNext())
							pw.print("; ");
						else
							pw.print("\n");
					}
				} else
					pw.println();
				if (it0.hasNext())
					pw.print("*\n");
			}
			// Guardado de alumnos.
			if (!setProfesores.isEmpty())
				pw.println("*");
			Set<String> setAlumnos = alumnos.keySet();
			Iterator<String> it3 = setAlumnos.iterator();
			while (it3.hasNext()) {
				Alumno alumno = alumnos.get(it3.next());
				pw.println("alumno");
				pw.println(alumno.getDni());
				pw.println(alumno.getNombre());
				pw.println(alumno.getApellidos());
				pw.println(alumno.getfechaNacimiento().get(Calendar.DATE) + "/" + alumno.getfechaNacimiento().get(Calendar.MONTH) + "/"
						+ alumno.getfechaNacimiento().get(Calendar.YEAR));
				pw.println(alumno.getFechaIngreso().get(Calendar.DATE) + "/" + alumno.getFechaIngreso().get(Calendar.MONTH) + "/"
						+ alumno.getFechaIngreso().get(Calendar.YEAR));
				Set<Integer> setAsignaturasSuperadas = alumno.getAsignaturasSuperadas().keySet();
				Iterator<Integer> it4 = setAsignaturasSuperadas.iterator();
				if (setAsignaturasSuperadas.isEmpty())
					pw.println();
				else {
					while (it4.hasNext()) {
						NotaFinal notaFinal = alumno.getAsignaturasSuperadas().get(it4.next());
						pw.print(notaFinal.getAsignatura().getIdAsignatura() + " " + notaFinal.getCursoAcademico() + " "
								+ notaFinal.getNota());
						if (it4.hasNext())
							pw.print("; ");
						else
							pw.print("\n");
					}
				}
				Set<Integer> setDocenciaRecibidaA = alumno.getDocenciaRecibidaA().keySet();
				Iterator<Integer> it5 = setDocenciaRecibidaA.iterator();
				Set<Integer> setDocenciaRecibidaB = alumno.getDocenciaRecibidaB().keySet();
				Iterator<Integer> it6 = setDocenciaRecibidaB.iterator();
				Set<Integer> setAsignaturasSinGrupo = alumno.getAsignaturasSinGrupo().keySet();
				Iterator<Integer> it7 = setAsignaturasSinGrupo.iterator();
				if (!setDocenciaRecibidaA.isEmpty()) {
					while (it5.hasNext()) {
						Grupo grupoA = alumno.getDocenciaRecibidaA().get(it5.next());
						pw.print(grupoA.getAsignatura().getIdAsignatura() + " " + grupoA.getTipoGrupo() + " " + grupoA.getIdGrupo());
						if (it5.hasNext())
							pw.print("; ");
						else {
							if (setDocenciaRecibidaB.isEmpty() & setAsignaturasSinGrupo.isEmpty()) {// ----
								pw.print("\n");
							} else
								pw.print("; ");
						}
					}
				}
				if (!setDocenciaRecibidaB.isEmpty()) {
					while (it6.hasNext()) {
						Grupo grupoB = alumno.getDocenciaRecibidaB().get(it6.next());
						pw.print(grupoB.getAsignatura().getIdAsignatura() + " " + grupoB.getTipoGrupo() + " " + grupoB.getIdGrupo());
						if (it6.hasNext())
							pw.print("; ");
						else {
							if (setAsignaturasSinGrupo.isEmpty()) {
								pw.print("\n");// NEW
							} else
								pw.print("; ");
						}
					}
				} else if (setAsignaturasSinGrupo.isEmpty())
					pw.println();
				while (it7.hasNext()) {
					Asignatura asignatura = alumno.getAsignaturasSinGrupo().get(it7.next());
					pw.print(asignatura.getIdAsignatura());
					if (it7.hasNext())
						pw.print("; ");
					else
						pw.print("\n");
				}
				if (it3.hasNext())
					pw.println("*");
			}
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

	public static void guardarFicheroAsignaturas(TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("salidaasignaturas.txt");
			pw = new PrintWriter(fichero);
			Set<Integer> setAsignaturas = asignaturas.keySet();
			Iterator<Integer> it0 = setAsignaturas.iterator();
			while (it0.hasNext()) {
				Asignatura asignatura = asignaturas.get(it0.next());
				pw.println(asignatura.getIdAsignatura());
				pw.println(asignatura.getNombre());
				pw.println(asignatura.getSiglas());
				pw.println(asignatura.getCurso());
				if (asignatura.getCoordinador().getDni() == null)
					pw.print("\n");
				else
					pw.println(asignatura.getCoordinador().getDni());
				Set<Integer> setPrerrequisitos = asignatura.getPrerrequisitos().keySet();
				Iterator<Integer> it1 = setPrerrequisitos.iterator();
				if (!setPrerrequisitos.isEmpty()) {

					while (it1.hasNext()) {
						Asignatura prerrequisito = asignaturas.get(it1.next());
						pw.print(prerrequisito.getIdAsignatura());
						if (it1.hasNext())
							pw.print(", ");
						else
							pw.print("\n");
					}
				} else
					pw.print("\n");
				Set<Integer> setGruposA = asignatura.getGruposA().keySet();
				Iterator<Integer> it2 = setGruposA.iterator();
				if (!setGruposA.isEmpty()) {
					while (it2.hasNext()) {
						Grupo grupo = asignatura.getGruposA().get(it2.next());
						pw.print(grupo.getIdGrupo() + " " + grupo.getDia() + " " + grupo.getHoraInicio() + " " + grupo.getHoraFin());
						if (it2.hasNext())
							pw.print("; ");
						else
							pw.print("\n");
					}
				} else
					pw.print("\n");

				Set<Integer> setGruposB = asignatura.getGruposB().keySet();
				Iterator<Integer> it3 = setGruposB.iterator();
				if (!setGruposB.isEmpty()) {
					while (it3.hasNext()) {
						Grupo grupo = asignatura.getGruposB().get(it3.next());
						pw.print(grupo.getIdGrupo() + " " + grupo.getDia() + " " + grupo.getHoraInicio() + " " + grupo.getHoraFin());
						if (it3.hasNext())
							pw.print("; ");
						else
							pw.print("\n");
					}
				} else
					pw.print("\n");
				if (it0.hasNext())
					pw.print("*\n");
			}
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

	public static void ejecucion(TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos,
			TreeMap<Integer, Asignatura> asignaturas) {

		Funcionalidades funcionalidad = new Funcionalidades(); // Composicion de Funcionalidades.

		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("ejecucion.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----------------------------------------------------- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (!(linea.charAt(0) == '*')) {
				String[] campos = linea.split(" ");
				switch (campos[0]) {
				case "InsertaPersona":
					funcionalidad.insertarPersona(linea);
					break;

				case "AsignaCoordinador":
					if (campos.length != 3) {
						funcionalidad.comandoIncorrecto();
						break;
					}
					funcionalidad.asignarCoordinador(linea, profesores, asignaturas);
					break;

				case "AsignaCargaDocente":
					break;

				case "Matricula":
					break;

				case "AsignaGrupo":
					break;

				case "Evalua":
					break;

				case "Expediente":
					break;

				case "ObtenerCalendarioClases":
					break;

				default:
					funcionalidad.comandoIncorrecto();
					break;
				}
			}
		}
		entrada.close();
	}

}
