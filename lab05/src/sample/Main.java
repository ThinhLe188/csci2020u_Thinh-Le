package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = primaryStage;
        primaryStage.setTitle("Lab 05 Solutions");

        TableView<StudentRecord> table;

        TableColumn<StudentRecord,String> sidCol = new TableColumn<>("SID");
        sidCol.setMinWidth(100);
        sidCol.setCellValueFactory(new PropertyValueFactory<>("studentID"));

        TableColumn<StudentRecord,Float> assignmentsCol = new TableColumn<>("Assignments");
        assignmentsCol.setMinWidth(100);
        assignmentsCol.setCellValueFactory(new PropertyValueFactory<>("assignments"));

        TableColumn<StudentRecord,Float> midtermCol = new TableColumn<>("Midterm");
        midtermCol.setMinWidth(100);
        midtermCol.setCellValueFactory(new PropertyValueFactory<>("midterm"));

        TableColumn<StudentRecord,Float> finalExamCol = new TableColumn<>("Final Exam");
        finalExamCol.setMinWidth(100);
        finalExamCol.setCellValueFactory(new PropertyValueFactory<>("finalExam"));

        TableColumn<StudentRecord,Float> finalMarkCol = new TableColumn<>("Final Mark");
        finalMarkCol.setMinWidth(100);
        finalMarkCol.setCellValueFactory(new PropertyValueFactory<>("finalMark"));


        TableColumn<StudentRecord,String> letterGradeCol = new TableColumn<>("Letter Grade");
        letterGradeCol.setMinWidth(100);
        letterGradeCol.setCellValueFactory(new PropertyValueFactory<>("letterGrade"));

        table = new TableView<>();
        table.setItems(DataSource.getAllMarks());
        table.getColumns().addAll(sidCol,assignmentsCol,midtermCol,finalExamCol,finalMarkCol,letterGradeCol);

        Label getSIDLb = new Label("SID:");
        Label getAssignmentsLb = new Label("Assignments:");
        Label getMidtermLb = new Label("Midterm:");
        Label getFinalExamLb = new Label("Final Exam:");

        TextField getSIDTx = new TextField("");
        getSIDTx.setPromptText("SID");
        getSIDTx.setMaxWidth(150);
        TextField getAssignmentsTx = new TextField();
        getAssignmentsTx.setPromptText("Assignments/100");
        getAssignmentsTx.setMaxWidth(150);
        TextField getMidtermTx = new TextField();
        getMidtermTx.setPromptText("Midterm/100");
        getMidtermTx.setMaxWidth(150);
        TextField getFinalExamTx = new TextField();
        getFinalExamTx.setPromptText("Final Exam/100");
        getFinalExamTx.setMaxWidth(150);

        Button btn = new Button("Add");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(table,0,0,4,1);
        grid.add(getSIDLb,0,1);
        grid.add(getSIDTx,1,1);
        grid.add(getAssignmentsLb, 2,1);
        grid.add(getAssignmentsTx, 3,1);
        grid.add(getMidtermLb,0,2);
        grid.add(getMidtermTx,1,2);
        grid.add(getFinalExamLb,2,2);
        grid.add(getFinalExamTx,3,2);
        grid.add(btn, 1,3);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String studentID = getSIDTx.getText();
                Float assignments = Float.parseFloat(getAssignmentsTx.getText());
                Float midterm = Float.parseFloat(getMidtermTx.getText());
                Float finalExam = Float.parseFloat(getFinalExamTx.getText());
                StudentRecord student = new StudentRecord(studentID,assignments,midterm,finalExam);
                table.getItems().add(student);
                getSIDTx.clear();
                getAssignmentsTx.clear();
                getMidtermTx.clear();
                getFinalExamTx.clear();
            }
        });

        Scene scene = new Scene(grid,650,450);
        stage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
