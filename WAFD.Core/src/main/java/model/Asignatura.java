package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author oscar
 */
public class Asignatura {
  //grupo,esGM,codAsig,desAsig,idioma,religion,fecha,diaSemana,codSesion,horaInicio,horaFin, permiso
   
  private String profesor;
  private String grupo;
  private String esGrupoMateria;
  private String codigo;
  private String descripcion;
  private String idioma;
  private String religion;
  private String codAlumnos;
 
  private LinkedHashMap<String,SesionAsignatura> sesiones;
  private LinkedHashMap<String,Alumno> alumnos;

  public Asignatura(String grupo, String esGrupoMateria, String codigo, String descripcion, String idioma, String religion) {
    this.grupo = grupo;
    this.esGrupoMateria = esGrupoMateria;
    this.codigo = codigo;
    this.descripcion = descripcion;
    this.idioma = idioma;
    this.religion = religion;
    this.sesiones = new LinkedHashMap<>();
    this.alumnos = new LinkedHashMap<>();

  }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getCodAlumnos() {
        return codAlumnos;
    }

    public void setCodAlumnos(String codAlumnos) {
        this.codAlumnos = codAlumnos;
    }

  public String getGrupo() {
    return grupo;
  }

  public String getEsGrupoMateria() {
    return esGrupoMateria;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getIdioma() {
    return idioma;
  }

  public String getReligion() {
    return religion;
  }

  public LinkedHashMap<String, SesionAsignatura> getSesiones() {
    return sesiones;
  }

  public LinkedHashMap<String, Alumno> getAlumnos() {
    return alumnos;
  }

  @Override
  public String toString() {
//    String imprimir =  "Asignatura{" + "grupo=" + grupo + ", esGrupoMateria=" + esGrupoMateria + ", codigo=" + codigo + ", descripcion=" + descripcion + ", idioma=" + idioma + ", religion=" + religion + ", sesiones: " ;
//     imprimir += "\n";
//    for (SesionAsignatura s : this.sesiones.values())
//    {
//      imprimir += s +"\n";
//    }
//    
//    imprimir += "\n";
//    for (Alumno a : this.alumnos.values())
//    {
//      imprimir += a +"\n";
//    }
        
    
    return this.descripcion;
  }
  
  
  
  
}
