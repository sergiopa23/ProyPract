/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author parra
 */


import javax.swing.*;
import reportes.JasperManager;
import javax.swing.JFileChooser;

public class ReportesFrame extends JFrame {

    public ReportesFrame() {

        setTitle("Reportes");
        setSize(500,300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JButton btnEstudiantes =
                new JButton("Estudiantes por Práctica");

        JButton btnHoras =
                new JButton("Horas Acumuladas");

        JButton btnActividades =
                new JButton("Actividades Docente");

        JButton btnEvidencias =
        new JButton("Evidencias");

        JButton btnExportarHoras =
        new JButton("Exportar Horas PDF");

        panel.add(btnEstudiantes);
        panel.add(btnHoras);
        panel.add(btnActividades);
        panel.add(btnEvidencias);
        panel.add(btnExportarHoras);

        add(panel);

        btnEstudiantes.addActionListener(e -> {

            JasperManager.mostrarReporte(
                    "src/reportesJRXML/estudiantes_practica.jrxml"
            );

        });

        btnHoras.addActionListener(e -> {

            JasperManager.mostrarReporte(
                    "src/reportesJRXML/horas_estudiante.jrxml"
            );

        });
        
                
        btnExportarHoras.addActionListener(e -> {

           JFileChooser chooser = new JFileChooser();

           if (chooser.showSaveDialog(this)
               == JFileChooser.APPROVE_OPTION) {

            String ruta =
                   chooser.getSelectedFile().getAbsolutePath();

           JasperManager.exportarPDF(
                    "src/reportesJRXML/horas_estudiante.jrxml",
                     ruta + ".pdf"
                );
            }
        });
        
        System.out.println(JasperCompileManager.class.getName());

            JasperReport reporte =
                 JasperCompileManager.compileReport(jrxml);  
    }
}