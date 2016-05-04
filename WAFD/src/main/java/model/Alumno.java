package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author oscar
 */
public class Alumno {
  
  
  private String id;
  private String nombre;

  public Alumno(String id, String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

  public String getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  @Override
  public String toString() {
    return nombre;
  }
  
  
}
