/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wafd;

 import javafx.application.Application;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.geometry.Insets;
    import javafx.scene.Scene;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.TableCell;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.TextField;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import javafx.util.Callback;

    public class TableCellWithMultipleComponent extends Application {
        @SuppressWarnings("rawtypes")
        TableColumn answerTypeCol; 
        @SuppressWarnings("rawtypes")
        TableColumn answerCol; 
        ObservableList<String> namesChoiceList;
        @SuppressWarnings("rawtypes")
        ComboBox comboBox;
        TextField textField;

        public static void main(String[] args) {
            launch(args);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void start(final Stage primaryStage) {
            primaryStage.setTitle("Table Cell With Multiple Components");

             TableView<Person> table = new TableView<Person>();
             table.setEditable(true);
              final ObservableList<Person> data = 
                        FXCollections.observableArrayList(
                            new Person("A", "Multiple Choice"),
                            new Person("JOHN", "Free Text"),
                            new Person("123", "Free Text"),
                            new Person("D", "Multiple Choice")
                        );



            GridPane gridpane = new GridPane();
            gridpane.setPadding(new Insets(5));
            gridpane.setHgap(5);
            gridpane.setVgap(5);


            namesChoiceList = FXCollections.observableArrayList("A", "B", "C", "D", "INVALID_ANSWER", "NO_ANSWER");

            answerCol = new TableColumn();
            answerCol.setText("Answers");
            answerCol.setMinWidth(210);
            answerCol.setEditable(true);
            answerCol.setCellValueFactory(new PropertyValueFactory("answers"));


            answerCol.setCellFactory( new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                @Override
                public TableCell<String, String> call(TableColumn<String, String> arg0) {
                    return new anyMethod();
                }
            });



            answerTypeCol = new TableColumn();
            answerTypeCol.setText("Answers Type");
            answerTypeCol.setMinWidth(210);
            answerTypeCol.setEditable(true);
            answerTypeCol.setCellValueFactory(new PropertyValueFactory("answersType"));



            table.setItems(data);
            table.getColumns().addAll(answerCol, answerTypeCol);

            StackPane root = new StackPane();

            Scene scene =new Scene(root, 500, 550);

            gridpane.add(table, 1, 5,1,20 );


            root.getChildren().addAll(gridpane);
            primaryStage.setScene(scene);
            primaryStage.show();


       }


        private class anyMethod extends TableCell <String, String>{

            @SuppressWarnings({ "unchecked", "rawtypes" })
            public anyMethod(){

                comboBox = new ComboBox();
                textField = new TextField();
                comboBox.setItems(namesChoiceList);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                     if (empty) {
                    setText(null);
                   setGraphic(null);
                    System.out.println("In empty");
                 } else {
                    if( getTableView().getColumns().get(1).getCellData(getIndex()).toString().startsWith("M")){

                     System.out.println("Making ComboBox");
                     setGraphic(comboBox);
                    }
                    else{
                        setGraphic(textField);
                    }
                 }

            }

        }


        public static class Person {
            private final SimpleStringProperty answers;
            private final SimpleStringProperty answersType;


            private Person(String answers, String answersType) {
                this.answers = new SimpleStringProperty(answers);
                this.answersType = new SimpleStringProperty(answersType);
            }

            public String getAnswers() {
                return answers.get();
            }
            public void setAnswers(String answers) {
                this.answers.set(answers);
            }

            public String getAnswersType() {
                return answersType.get();
            }
            public void setAnswersType(String answersType) {
                this.answersType.set(answersType);
            }
        }



    }
