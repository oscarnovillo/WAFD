/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedHashMap;

/**
 *
 * @author oscar
 */
public class FaltasSesion {
    
    private SesionAsignatura sesion;
    private String fecha;
    private LinkedHashMap<String,FaltaSesionAlumno> faltas;

    public FaltasSesion(SesionAsignatura sesion, String fecha) {
        this.sesion = sesion;
        this.fecha = fecha;
        faltas = new LinkedHashMap<>();
        
    }

    public SesionAsignatura getSesion() {
        return sesion;
    }

    public String getFecha() {
        return fecha;
    }

    public LinkedHashMap<String, FaltaSesionAlumno> getFaltas() {
        return faltas;
    }

    public void setSesion(SesionAsignatura sesion) {
        this.sesion = sesion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
    
    
}
