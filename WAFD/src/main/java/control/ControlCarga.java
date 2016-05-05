/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

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
import model.FaltaSesionAlumno;
import model.FaltasSesion;
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
public class ControlCarga {

    public Map<String, String> login(String user, String pass) {
        Map<String, String> cookies = null;
        Connection.Response response = null;

        try {
            response = Jsoup.connect("https://gestiona.madrid.org/wafd/ValidaUsuario.icm")
                    .data("USUARIO", user)
                    .data("CLAVE", pass)
                    // .cookies(response.cookies())
                    .method(Connection.Method.POST)
                    .execute();
            cookies = response.cookies();
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cookies;
    }

    public LinkedHashMap<String, Asignatura> cargarAsignaturas(Map<String, String> cookies) {
        Document homePage = null;
        LinkedHashMap<String, Asignatura> asignaturas = new LinkedHashMap<>();
        try {
            homePage = Jsoup.connect("https://gestiona.madrid.org/wafd/HorarioProfesor.icm")
                    .cookies(cookies)
                    .post();

            Elements ele1 = homePage.getElementsByTag("form");
            Element form = ele1.get(0);
            String profesor = form.getElementsByAttributeValue("name", "profesor").attr("value");
            Elements ele = homePage.select("a[href*=irA]");
            ListIterator l = ele.listIterator();
            while (l.hasNext()) {
                Element a = (Element) l.next();
                System.out.println(a.attr("href"));
                Asignatura asig = parseaLineaHorario(asignaturas, a.attr("href"));
                asig.setProfesor(profesor);
            }
            for (Asignatura a : asignaturas.values()) {
                sacarAlumnosAsignatura(cookies, a);
            }

        } catch (Exception e) {
        }

        return asignaturas;
    }

    private Asignatura parseaLineaHorario(LinkedHashMap<String, Asignatura> asignaturas, String href) {
        Asignatura asig = null;
        href = href.substring(href.indexOf("(") + 1);
        String datos[] = href.split(",");
        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].substring(1, datos[i].length() - 1);
        }
        asig = asignaturas.get(datos[2]);

        if (asig == null) {
            asig = new Asignatura(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]);
            asignaturas.put(asig.getCodigo(), asig);
        }

        LinkedHashMap<String, SesionAsignatura> sesiones = asig.getSesiones();

        SesionAsignatura ses = new SesionAsignatura(datos[7], datos[8], datos[9], datos[10]);

        sesiones.put(ses.getId(), ses);

        return asig;
    }

    private void sacarAlumnosAsignatura(Map<String, String> cookies, Asignatura asig) {
        Document homePage = null;

        //cogemos la primera sesion
        SesionAsignatura sesion = asig.getSesiones().values().iterator().next();
        Calendar ahora = Calendar.getInstance();
        ahora.set(Calendar.DAY_OF_WEEK, sesion.getDiaSemanaCalendar());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String datestring = dateFormat.format(ahora.getTime());

        try {
            homePage = Jsoup.connect("https://gestiona.madrid.org/wafd/IntroIncidenciasCentro.icm")
                    .data("optionsProfesores", "null")
                    .data("codigoAsignatura", asig.getCodigo())
                    .data("codigoIdioma", asig.getIdioma())
                    .data("codigoReligion", asig.getReligion())
                    .data("esGrupoMateria", asig.getEsGrupoMateria())
                    .data("codigoSesion", sesion.getSesion())
                    .data("diaSemana", sesion.getDiaSemana())
                    .data("nombreProfesor", "")
                    //.data("franja","08:30:09:25")
                    .data("haySesiones", "0")
                    .data("sesion", "1")
                    .data("grupo", asig.getGrupo())
                    .data("asignatura", asig.getDescripcion())
                    .data("pantallaIncidencia", "S")
                    .data("profesor", "")
                    .data("dia", datestring)
                    .data("diaold", datestring)
                    .cookies(cookies)
                    .post();
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        Elements ele = homePage.getElementsByTag("form");
        Element form = ele.get(0);
        String codAlumnos = form.getElementsByAttributeValue("name", "alumnos").attr("value");
        asig.setCodAlumnos(codAlumnos);
        String[] alumnos = codAlumnos.split("#");
        Elements tds = form.getElementsByAttributeValue("width", "250");
        tds = form.select("td > a");

        Element td = null;
        for (int i = 0; i < alumnos.length; i++) {
            td = tds.get(i);
            Alumno a = new Alumno(alumnos[i], td.text());
            asig.getAlumnos().put(a.getId(), a);
        }

    }

    public void sacarFaltaSesion(Map<String, String> cookies, List<Alumno> alumnos, Asignatura asignatura,
            SesionAsignatura sesion, String fecha)
    {
        Document homePage = null;
        boolean ok = false;
        try {
            homePage = Jsoup.connect("https://gestiona.madrid.org/wafd/IntroIncidenciasCentro.icm")
                    .data("optionsProfesores", "null")
                    .data("codigoAsignatura", asignatura.getCodigo())
                    .data("codigoIdioma", asignatura.getIdioma())
                    .data("codigoReligion", asignatura.getReligion())
                    .data("esGrupoMateria", asignatura.getEsGrupoMateria())
                    .data("codigoSesion", sesion.getSesion())
                    .data("diaSemana", sesion.getDiaSemana())
                    .data("nombreProfesor", "")
                    //.data("franja","08:30:09:25")
                    .data("haySesiones", "0")
                    .data("sesion", sesion.getSesion())
                    .data("grupo", asignatura.getGrupo())
                    .data("asignatura", asignatura.getDescripcion())
                    .data("pantallaIncidencia", "S")
                    .data("profesor", asignatura.getProfesor())
                    .data("dia", fecha)
                    .data("diaold", fecha)
                    .cookies(cookies)
                    .post();
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        Elements ele = homePage.getElementsByTag("form");
        Element form = ele.get(0);
        // cargar los datos de la sesion
        FaltasSesion fs = new FaltasSesion(sesion, fecha);
        sesion.getFaltas().put(fecha,fs);
        
        ArrayList<FaltaSesionAlumno> faltas = new ArrayList<>();

        asignatura.getAlumnos().values().stream().forEach((alumno) -> {
            FaltaSesionAlumno falta = new FaltaSesionAlumno();
            falta.setAlumno(alumno);
            falta.setSesion(sesion);
            String asistencia = form.getElementsByAttributeValue("name", "asistencia" + alumno.getId()).select("option[selected]").val();
            String amonestacion = form.getElementsByAttributeValue("name", "amonestacion" + alumno.getId()).select("option[selected]").val();
            String dirty = form.getElementsByAttributeValue("name", "dirty" + alumno.getId()).val();
            falta.setAsistencia(asistencia);
            falta.setAmonestacion(amonestacion);
            falta.setDirty(dirty);
            fs.getFaltas().put(alumno.getId(), falta);
        });

    }
    
    public boolean meterIncidencia(Map<String, String> cookies, List<Alumno> alumnos, Asignatura asignatura,
            SesionAsignatura sesion, String fecha) {
        Document homePage = null;
        boolean ok = false;
        try {
            homePage = Jsoup.connect("https://gestiona.madrid.org/wafd/IntroIncidenciasCentro.icm")
                    .data("optionsProfesores", "null")
                    .data("codigoAsignatura", asignatura.getCodigo())
                    .data("codigoIdioma", asignatura.getIdioma())
                    .data("codigoReligion", asignatura.getReligion())
                    .data("esGrupoMateria", asignatura.getEsGrupoMateria())
                    .data("codigoSesion", sesion.getSesion())
                    .data("diaSemana", sesion.getDiaSemana())
                    .data("nombreProfesor", "")
                    //.data("franja","08:30:09:25")
                    .data("haySesiones", "0")
                    .data("sesion", sesion.getSesion())
                    .data("grupo", asignatura.getGrupo())
                    .data("asignatura", asignatura.getDescripcion())
                    .data("pantallaIncidencia", "S")
                    .data("profesor", asignatura.getProfesor())
                    .data("dia", fecha)
                    .data("diaold", fecha)
                    .cookies(cookies)
                    .post();
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        Elements ele = homePage.getElementsByTag("form");
        Element form = ele.get(0);
        // cargar los datos de la sesion
        ArrayList<FaltaSesionAlumno> faltas = new ArrayList<>();

        asignatura.getAlumnos().values().stream().forEach((alumno) -> {
            FaltaSesionAlumno falta = new FaltaSesionAlumno();
            falta.setAlumno(alumno);
            falta.setSesion(sesion);
            String asistencia = form.getElementsByAttributeValue("name", "asistencia" + alumno.getId()).select("option[selected]").val();
            String amonestacion = form.getElementsByAttributeValue("name", "amonestacion" + alumno.getId()).select("option[selected]").val();
            String dirty = form.getElementsByAttributeValue("name", "dirty" + alumno.getId()).val();

            if (alumnos.indexOf(alumno) != -1) {
                dirty = "S";
                asistencia = "F";
            }

            falta.setAsistencia(asistencia);
            falta.setAmonestacion(amonestacion);
            falta.setDirty(dirty);
            faltas.add(falta);
        });

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

        faltas.stream().forEach((falta) -> {

            con.data("asistencia" + falta.getAlumno().getId(), falta.getAsistencia())
                    .data("amonestacion" + falta.getAlumno().getId(), falta.getAmonestacion())
                    .data("dirty" + falta.getAlumno().getId(), falta.getDirty());
        });
        try {
            con.cookies(cookies)
                    .post();
            System.out.println(con.response().body().indexOf("guardar"));
            ok = true;
        } catch (IOException ex) {
            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ok;
//        Connection con = Jsoup.connect("https://gestiona.madrid.org/wafd/IntroIncidenciasCentro.icm")
//                //.data("optionsProfesores", "null")
//                .data("codigoAsignatura", asignatura.getCodigo())
//                .data("codigoIdioma", asignatura.getIdioma())
//                .data("codigoReligion", asignatura.getReligion())
//                .data("esGrupoMateria", asignatura.getEsGrupoMateria())
//                //.data("codigoSesion",sesion.getSesion())
//                .data("diaSemana", sesion.getDiaSemana())
//                .data("nom_prof", "OSCAR NOVILLO CAMACHO")
//                //.data("franja","08:30:09:25")
//                //.data("haySesiones", "0")
//                .data("sesion", sesion.getSesion())
//                .data("grupo", asignatura.getGrupo())
//                .data("asignatura", asignatura.getDescripcion())
//                .data("pantallaIncidencia", "S")
//                .data("profesor", "51946107D")
//                .data("dia", fecha)
//                .data("alumnos", asignatura.getCodAlumnos());
//        
//        for (Alumno alumno : alumnos) {
//            con.data("asistencia" + alumno.getId(), "F")
//                    .data("amonestacion" + alumno.getId(), "-1")
//                    .data("dirty" + alumno.getId(), "S");
//        }
//        try {
//            con.cookies(cookies)
//                    .post();
//            System.out.println(con.response().body());
//        } catch (IOException ex) {
//            Logger.getLogger(ControlCarga.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
