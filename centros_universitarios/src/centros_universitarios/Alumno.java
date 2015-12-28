package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class Alumno extends Persona {

	/* ATRIBUTOS */
	private GregorianCalendar fechaIngreso;
	private TreeMap<Integer, Grupo> docenciaRecibidaA;// docencia recibida
	private TreeMap<Integer, Grupo> docenciaRecibidaB;
	private TreeMap<Integer, NotaFinal> asignaturasSuperadas;// asignaturas aprobadas
	private String[] arrayAsignaturasSuperadas;
	private TreeMap<Integer, Asignatura> asignaturasMatriculadas;
	private String[] arrayDocenciaRecibida;
	private TreeMap<Integer, Asignatura> asignaturasSinGrupo;

	/* METODOS */
	@Override
	public String toString() { // Metodo toString sobreescrito reciclando el metodo de la clase padre.
		return super.toString() + " " + fechaIngreso.get(Calendar.DATE) + "/" + fechaIngreso.get(Calendar.MONTH) + "/"
				+ fechaIngreso.get(Calendar.YEAR);
	}

	/* GETTERS & SETTERS */
	public GregorianCalendar getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(GregorianCalendar fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public TreeMap<Integer, Grupo> getDocenciaRecibidaA() {
		return docenciaRecibidaA;
	}

	public void setDocenciaRecibidaA(TreeMap<Integer, Grupo> docenciaRecibidaA) {
		this.docenciaRecibidaA = docenciaRecibidaA;
	}

	public TreeMap<Integer, NotaFinal> getAsignaturasSuperadas() {
		return asignaturasSuperadas;
	}

	public void setAsignaturasSuperadas(TreeMap<Integer, NotaFinal> asignaturasSuperadas) {
		this.asignaturasSuperadas = asignaturasSuperadas;
	}

	public String[] getArrayAsignaturasSuperadas() {
		return arrayAsignaturasSuperadas;
	}

	public void setArrayAsignaturasSuperadas(String[] arrayAsignaturasSuperadas) {
		this.arrayAsignaturasSuperadas = arrayAsignaturasSuperadas;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasMatriculadas() {
		return asignaturasMatriculadas;
	}

	public void setAsignaturasMatriculadas(TreeMap<Integer, Asignatura> asignaturasMatriculadas) {
		this.asignaturasMatriculadas = asignaturasMatriculadas;
	}

	public String[] getArrayDocenciaRecibida() {
		return arrayDocenciaRecibida;
	}

	public void setArrayDocenciaRecibida(String[] arrayDocenciaRecibida) {
		this.arrayDocenciaRecibida = arrayDocenciaRecibida;
	}

	public TreeMap<Integer, Grupo> getDocenciaRecibidaB() {
		return docenciaRecibidaB;
	}

	public void setDocenciaRecibidaB(TreeMap<Integer, Grupo> docenciaRecibidaB) {
		this.docenciaRecibidaB = docenciaRecibidaB;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasSinGrupo() {
		return asignaturasSinGrupo;
	}

	public void setAsignaturasSinGrupo(TreeMap<Integer, Asignatura> asignaturasSinGrupo) {
		this.asignaturasSinGrupo = asignaturasSinGrupo;
	}

	/* CONSTRUCTORES */
	public Alumno(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, GregorianCalendar fechaIngreso,
			TreeMap<Integer, Grupo> docenciaRecibidaA, TreeMap<Integer, Grupo> docenciaRecibidaB,
			TreeMap<Integer, NotaFinal> asignaturasSuperadas, String[] arrayAsignaturasSuperadas,
			TreeMap<Integer, Asignatura> asignaturasMatriculadas, String[] arrayDocenciaRecibida) { // Constructor.
		super(dni, nombre, apellidos, fechaNacimiento); // Llamada al metodo constructor de la clase padre.
		this.fechaIngreso = fechaIngreso;
		this.docenciaRecibidaA = docenciaRecibidaA;
		this.docenciaRecibidaB = docenciaRecibidaB;
		this.asignaturasSuperadas = asignaturasSuperadas;
		this.arrayAsignaturasSuperadas = arrayAsignaturasSuperadas;
		this.asignaturasMatriculadas = asignaturasMatriculadas;
		this.arrayDocenciaRecibida = arrayDocenciaRecibida;
	}
}