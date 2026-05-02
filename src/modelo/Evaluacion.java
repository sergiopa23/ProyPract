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

public class Evaluacion {
    private int idEvaluacion;
    private double nota;
    private String observaciones;
    private Date fecha;
    private int idPractica;
    private int idDocente;
    private int idEstudiante;

    public Evaluacion() {}

    public Evaluacion(int idEvaluacion, double nota, String observaciones, Date fecha,
                      int idPractica, int idDocente, int idEstudiante) {
        this.idEvaluacion = idEvaluacion;
        this.nota = nota;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.idPractica = idPractica;
        this.idDocente = idDocente;
        this.idEstudiante = idEstudiante;
    }

    // Getters y Setters...

    @Override
    public String toString() {
        return "Evaluacion{" +
                "idEvaluacion=" + idEvaluacion +
                ", nota=" + nota +
                ", observaciones='" + observaciones + '\'' +
                ", fecha=" + fecha +
                ", idPractica=" + idPractica +
                ", idDocente=" + idDocente +
                ", idEstudiante=" + idEstudiante +
                '}';
    }

    int getIdEvaluacion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    double getNota() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    String getObservaciones() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Date getFecha() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdPractica() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdDocente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdEstudiante() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

