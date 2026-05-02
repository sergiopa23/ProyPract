/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
import java.sql.Date;

public class Practica {
    private int idPractica;
    private Date fechaInicio;
    private Date fechaFin;
    private int horas;
    private String estado;
    private int idEstudiante;
    private int idDocente;
    private int idCoordinador;
    private int idInstitucion;

    public Practica() {}

    public Practica(int idPractica, Date fechaInicio, Date fechaFin, int horas, String estado,
                    int idEstudiante, int idDocente, int idCoordinador, int idInstitucion) {
        this.idPractica = idPractica;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horas = horas;
        this.estado = estado;
        this.idEstudiante = idEstudiante;
        this.idDocente = idDocente;
        this.idCoordinador = idCoordinador;
        this.idInstitucion = idInstitucion;
    }

    // Getters y Setters...

    @Override
    public String toString() {
        return "Practica{" +
                "idPractica=" + idPractica +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", horas=" + horas +
                ", estado='" + estado + '\'' +
                ", idEstudiante=" + idEstudiante +
                ", idDocente=" + idDocente +
                ", idCoordinador=" + idCoordinador +
                ", idInstitucion=" + idInstitucion +
                '}';
    }

    Date getFechaInicio() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Date getFechaFin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getHoras() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    String getEstado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdEstudiante() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdDocente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdCoordinador() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdInstitucion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdPractica() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

