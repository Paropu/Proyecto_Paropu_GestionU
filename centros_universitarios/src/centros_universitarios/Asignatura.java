package centros_universitarios;

import java.util.TreeMap;

public class Asignatura {

	/* ATRIBUTOS */
	private Integer idAsignatura;
	private String nombre;
	private String siglas;
	private Integer curso;
	private Profesor coordinador;//coordinador
	private TreeMap<Integer, Asignatura> prerrequisitos; //prerrequisitos
	private TreeMap<Integer, Grupo> gruposA;//grupos A
	private TreeMap<Integer, Grupo> gruposB;//grupos B



	/*METODOS */


	/* GETTERS & SETTERS */
	public Integer getIdAsignatura() {
		return idAsignatura;
	}
	@Override
	public String toString() {
		return idAsignatura + " " + nombre + " " + siglas + " " + curso;
	}
	public void setIdAsignatura(Integer idAsignatura) {
		this.idAsignatura = idAsignatura;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSiglas() {
		return siglas;
	}
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}
	public Integer getCurso() {
		return curso;
	}
	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Profesor getCoordinador() {
		return coordinador;
	}
	public void setCoordinador(Profesor coordinador) {
		this.coordinador = coordinador;
	}
	public TreeMap<Integer, Asignatura> getPrerrequisitos() {
		return prerrequisitos;
	}
	public void setPrerrequisitos(TreeMap<Integer, Asignatura> prerrequisitos) {
		this.prerrequisitos = prerrequisitos;
	}
	public TreeMap<Integer, Grupo> getGruposA() {
		return gruposA;
	}
	public void setGruposA(TreeMap<Integer, Grupo> gruposA) {
		this.gruposA = gruposA;
	}
	public TreeMap<Integer, Grupo> getGruposB() {
		return gruposB;
	}
	public void setGruposB(TreeMap<Integer, Grupo> gruposB) {
		this.gruposB = gruposB;
	}
	
	/* CONSTRUCTORES */
	public Asignatura(Integer idAsignatura, String nombre, String siglas, Integer curso) {
		super();
		this.idAsignatura = idAsignatura;
		this.nombre = nombre;
		this.siglas = siglas;
		this.curso = curso;
	}
}
