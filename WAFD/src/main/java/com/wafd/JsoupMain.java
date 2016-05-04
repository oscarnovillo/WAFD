package com.wafd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import control.ControlCarga;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Alumno;
import model.Asignatura;
import model.SesionAsignatura;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author oscar
 */
public class JsoupMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Connection.Response response = null;
//    Jsoup.connect("https://gestiona.madrid.org/wafd/ValidaUsuario.icm")
//        .method(Connection.Method.GET)
//        .execute();

        response = Jsoup.connect("https://gestiona.madrid.org/wafd/ValidaUsuario.icm")
                .data("USUARIO", "onc1")
                .data("CLAVE", "clase2016")
                // .cookies(response.cookies())
                .method(Connection.Method.POST)
                .execute();
        ControlCarga cg = new ControlCarga();
        LinkedHashMap<String, Asignatura> asig = cg.cargarAsignaturas(response.cookies());

        ArrayList<Alumno> alumnos = new ArrayList<>();
        for (Alumno a: asig.get("0488").getAlumnos().values())
            alumnos.add(a);
        
        
        meterIncidencia(response.cookies(),alumnos, asig.get("0488"), asig.get("0488").getSesiones().get("M$1"), "19/04/2016");
        System.out.println(asig);

        System.out.println("OK");

    }

    public static void meterIncidencia(Map<String, String> cookies, List<Alumno> alumnos, Asignatura asignatura, SesionAsignatura sesion, String fecha) {
       Connection con = Jsoup.connect("https://gestiona.madrid.org/wafd/IntroIncidenciasCentro.icm")
                .data("optionsProfesores", "null")
                .data("codigoAsignatura", asignatura.getCodigo())
                .data("codigoIdioma", asignatura.getIdioma())
                .data("codigoReligion", asignatura.getReligion())
                .data("esGrupoMateria", asignatura.getEsGrupoMateria())
                //.data("codigoSesion", "1")
                .data("diaSemana", sesion.getDiaSemana())
                //.data("nombreProfesor", "OSCAR NOVILLO CAMACHO")
                //.data("franja","08:30:09:25")
                //.data("haySesiones", "0")
                .data("sesion", sesion.getSesion())
                .data("grupo", asignatura.getGrupo())
                .data("asignatura", asignatura.getDescripcion())
                .data("pantallaIncidencia", "S")
                .data("profesor", asignatura.getProfesor())
                .data("dia", fecha)
                .data("alumnos", asignatura.getCodAlumnos());
        
        alumnos.stream().forEach((alumno) -> {
            con.data("asistencia" + alumno.getId(), "-1")
                    .data("amonestacion" + alumno.getId(), "-1")
                    .data("dirty" + alumno.getId(), "S");
        });
        try {
            con.cookies(cookies)
                    .post();
            System.out.println(con.response().body().indexOf("guardar"));
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
