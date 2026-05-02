/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
public class Docente {
    private int idDocente;
    private String especialidad;
    private int idUsuario;

    public Docente() {}

    public Docente(int idDocente, String especialidad, int idUsuario) {
        this.idDocente = idDocente;
        this.especialidad = especialidad;
        this.idUsuario = idUsuario;
    }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return "Docente{" +
                "idDocente=" + idDocente +
                ", especialidad='" + especialidad + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
