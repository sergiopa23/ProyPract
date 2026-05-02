/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
public class Estudiante {
    private int idEstudiante;
    private String codigo;
    private String programa;
    private int idUsuario;

    public Estudiante() {}

    public Estudiante(int idEstudiante, String codigo, String programa, int idUsuario) {
        this.idEstudiante = idEstudiante;
        this.codigo = codigo;
        this.programa = programa;
        this.idUsuario = idUsuario;
    }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudiante=" + idEstudiante +
                ", codigo='" + codigo + '\'' +
                ", programa='" + programa + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
