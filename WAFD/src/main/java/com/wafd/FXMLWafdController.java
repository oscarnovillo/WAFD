/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wafd;

import control.ControlCarga;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Alumno;
import model.Asignatura;
import model.SesionAsignatura;

/**
 * FXML Controller class
 *
 * @author oscar
 */
public class FXMLWafdController implements Initializable {

    @FXML
    private AnchorPane fxPane;

    @FXML
    private ComboBox<Asignatura> fxAsignaturas;

    @FXML
    private Button fxBtFalta;
    @FXML
    private Button fxBtJustificacion;

    @FXML
    private ListView<Alumno> fxListaAlumnos;

    @FXML
    private DatePicker fxDateInicio;
    @FXML
    private DatePicker fxDateFin;

    @FXML
    private VBox fxVBoxLunes;
    @FXML
    private VBox fxVBoxMartes;
    @FXML
    private VBox fxVBoxMiercoles;
    @FXML
    private VBox fxVBoxJueves;
    @FXML
    private VBox fxVBoxViernes;

    LinkedHashMap<String, Asignatura> asignaturas;
    Map<String, String> cookies;

    @FXML
    private void handleChangeAsignatura(ActionEvent event) {
        System.out.println(fxAsignaturas.getSelectionModel().getSelectedItem());
        Asignatura asignatura = asignaturas.get(fxAsignaturas.getSelectionModel().getSelectedItem().getCodigo());
        fxVBoxLunes.getChildren().clear();
        fxVBoxMartes.getChildren().clear();
        fxVBoxMiercoles.getChildren().clear();
        fxVBoxJueves.getChildren().clear();
        fxVBoxViernes.getChildren().clear();

        for (SesionAsignatura sesion : asignatura.getSesiones().values()) {
            CheckBox ck = new CheckBox(sesion.getDiaSemana() + " " + sesion.getHoraInicio());
            ck.getProperties().put("sesion", sesion);
            ck.setSelected(true);
            VBox vbox = null;
            switch (sesion.getDiaSemana()) {
                case "L":
                    vbox = fxVBoxLunes;
                    break;
                case "M":
                    vbox = fxVBoxMartes;
                    break;
                case "X":
                    vbox = fxVBoxMiercoles;
                    break;
                case "J":
                    vbox = fxVBoxJueves;
                    break;
                case "V":
                    vbox = fxVBoxViernes;
                    break;

            }
            vbox.getChildren().add(ck);
        }

        fxListaAlumnos.getItems().clear();
        for (Alumno alumno : asignatura.getAlumnos().values()) {
            fxListaAlumnos.getItems().add(alumno);
        }
    }

    @FXML
    private void handleFalta(ActionEvent event) {
       

        LocalDate date = fxDateInicio.getValue();
        LocalDate fin = fxDateFin.getValue();
         String mensaje = "";
        do {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEEE", new Locale("es", "ES"));
            DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/y", new Locale("es", "ES"));
            // poner sesiones del día.
            VBox vbox = null;
            switch (date.format(formatter).toUpperCase()) {
                case "L":
                    vbox = fxVBoxLunes;
                    break;
                case "M":
                    vbox = fxVBoxMartes;
                    break;
                case "X":
                    vbox = fxVBoxMiercoles;
                    break;
                case "J":
                    vbox = fxVBoxJueves;
                    break;
                case "V":
                    vbox = fxVBoxViernes;
                    break;
            }
            // para S y D
            if (vbox != null) {
                for (Node node : vbox.getChildren()) {
                    CheckBox ck = (CheckBox) node;
                    if (ck.isSelected()) {
                        SesionAsignatura sesion = ((SesionAsignatura) ck.getProperties().get("sesion"));
                        ControlCarga cg = new ControlCarga();
                        Asignatura asignatura = asignaturas.get(fxAsignaturas.getSelectionModel().getSelectedItem().getCodigo());
                       
                        if (cg.meterIncidencia(this.cookies, fxListaAlumnos.getSelectionModel().getSelectedItems(),
                                asignatura, sesion, date.format(formatterFecha)))
                        {
                            mensaje +="\n Cambios Guardados para "+date.format(formatterFecha)+" en sesion "+sesion.getId();
                        }
                        else mensaje += "\n Error al actualizar faltas del dia "+date.format(formatterFecha)+" en sesion "+sesion.getId();
                        
                       
                        //fxAux.getItems().add(a.getNombre() + " " + date.format(formatterFecha) + " " + sesion.getDiaSemana() + " " + sesion.getHoraInicio());

                    }
                }
            }
            date = date.plus(1, ChronoUnit.DAYS);
        } while (fin != null && date.compareTo(fin) <= 0);
        Alert a = new Alert(Alert.AlertType.INFORMATION,mensaje,ButtonType.CLOSE);
                        a.show();
    }

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.cookies = (Map<String, String>) stage.getProperties().get("cookies");
        asignaturas = (LinkedHashMap<String, Asignatura>) stage.getProperties().get("asignaturas");
        fxAsignaturas.setItems(FXCollections.observableArrayList());
        for (Asignatura asig : asignaturas.values()) {
            fxAsignaturas.getItems().add(asig);
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fxListaAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fxListaAlumnos.setItems(FXCollections.observableArrayList());
       
    }

}
