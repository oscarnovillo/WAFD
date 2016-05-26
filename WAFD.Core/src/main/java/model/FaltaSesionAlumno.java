/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author oscar
 */
public class FaltaSesionAlumno {
    
    private Alumno alumno;
    private SesionAsignatura sesion;
    private String fecha;
    private String justificar;
    private String observacion;
    private String asistencia;
    private String amonestacion;
    private String dirty;

    public String getJustificar() {
        return justificar;
    }

    public void setJustificar(String justificar) {
        this.justificar = justificar;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    
    
    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public SesionAsignatura getSesion() {
        return sesion;
    }

    public void setSesion(SesionAsignatura sesion) {
        this.sesion = sesion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public String getAmonestacion() {
        return amonestacion;
    }

    public void setAmonestacion(String amonestacion) {
        this.amonestacion = amonestacion;
    }

    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }
    
    
    
}
