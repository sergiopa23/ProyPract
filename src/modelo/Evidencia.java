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

public class Evidencia {
    private int idEvidencia;
    private String descripcion;
    private String archivo;
    private Date fecha;
    private int idPractica;
    private int idEstudiante;

    public Evidencia() {}

    public Evidencia(int idEvidencia, String descripcion, String archivo, Date fecha, int idPractica, int idEstudiante) {
        this.idEvidencia = idEvidencia;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.fecha = fecha;
        this.idPractica = idPractica;
        this.idEstudiante = idEstudiante;
    }

    // Getters y Setters...

    @Override
    public String toString() {
        return "Evidencia{" +
                "idEvidencia=" + idEvidencia +
                ", descripcion='" + descripcion + '\'' +
                ", archivo='" + archivo + '\'' +
                ", fecha=" + fecha +
                ", idPractica=" + idPractica +
                ", idEstudiante=" + idEstudiante +
                '}';
    }

    int getIdEvidencia() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    String getDescripcion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    String getArchivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Date getFecha() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdPractica() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getIdEstudiante() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
