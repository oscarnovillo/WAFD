/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wafd;

import control.ControlCarga;
import java.awt.Panel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Asignatura;

/**
 * FXML Controller class
 *
 * @author oscar
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private TextField fxUser;

    @FXML
    private PasswordField fxPass;

    @FXML
    private ImageView fxSplashScreen;
    
    @FXML
    private Pane fxPanelLoad;
    

    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException {

        //fxSplashScreen.setImage(new Image(this.getClass().getResourceAsStream("/img/load.gif")));
        fxPanelLoad.setVisible(true);
        final Stage stage = (Stage) fxUser.getScene().getWindow();

        Task task;
        task = new Task<Parent>() {
            
            
            @Override
            protected Parent call() throws Exception {
                ControlCarga cg = new ControlCarga();
                Map<String, String> cookies = cg.login(fxUser.getText(), fxPass.getText());
                //get reference to the button's stage       
                stage.getProperties().put("cookies", cookies);
                Map asignaturas = cg.cargarAsignaturas(cookies);
                stage.getProperties().put("asignaturas", cg.cargarAsignaturas(cookies));
                
                return null;
            }
        };
        task.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    Parent root;
                    //load up OTHER FXML document
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLWafd.fxml"));
                    root = loader.load();
                    FXMLWafdController controller = loader.getController();
                    controller.setStage(stage);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        new Thread(task).start();
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
        fxPanelLoad.setVisible(false);
        // fxSplashScreen.setImage(new Image(getClass().getResourceAsStream("/img/load.gif")));

    }

}
