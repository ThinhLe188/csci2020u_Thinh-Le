package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {
    private String currentFilename = "";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Lab 08 Solutions");

        Menu menu = new Menu("File");
        MenuItem menuNew = new MenuItem("New");
        MenuItem menuOpen = new MenuItem("Open");
        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuSaveAs = new MenuItem("Save As");
        MenuItem menuExit = new MenuItem("Exit");

        menu.getItems().add(menuNew);
        menu.getItems().add(menuOpen);
        menu.getItems().add(menuSave);
        menu.getItems().add(menuSaveAs);
        menu.getItems().add(menuExit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

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
        table.getColumns().addAll(sidCol,assignmentsCol,midtermCol,finalExamCol,finalMarkCol,letterGradeCol);

        Label getSIDLb = new Label("SID:");
        Label getAssignmentsLb = new Label("Assignments:");
        Label getMidtermLb = new Label("Midterm:");
        Label getFinalExamLb = new Label("Final Exam:");
        Label invalidInput = new Label();
        invalidInput.setTextFill(Color.RED);

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
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);

        VBox vBoxMenu = new VBox(menuBar);
        VBox vBox = new VBox(vBoxMenu, table);

        grid.add(vBox,0,0,5,1);
        grid.add(getSIDLb,1,2);
        grid.add(getSIDTx,2,2);
        grid.add(getAssignmentsLb, 3,2);
        grid.add(getAssignmentsTx, 4,2);
        grid.add(getMidtermLb,1,3);
        grid.add(getMidtermTx,2,3);
        grid.add(getFinalExamLb,3,3);
        grid.add(getFinalExamTx,4,3);
        grid.add(invalidInput,2,4);
        grid.add(btn, 2,5);

        menuNew.setOnAction(actionEvent -> table.getItems().clear());

        menuOpen.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                currentFilename = selectedFile.getAbsolutePath();
                FileLoader fileLoader = new FileLoader(currentFilename);
                fileLoader.loadFile();
                table.setItems(FileLoader.getMarks());
            }
        });

        menuSave.setOnAction(actionEvent -> {
            if (currentFilename == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Resource File");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                File selectedFile = fileChooser.showSaveDialog(primaryStage);
                if (selectedFile != null) {
                    currentFilename = selectedFile.getAbsolutePath();
                }
            }
            FileSaver fileSaver = new FileSaver(currentFilename);
            fileSaver.setMarks(table.getItems());
            fileSaver.saveFile();
        });

        menuSaveAs.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Resource File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showSaveDialog(primaryStage);
            if (selectedFile != null) {
                currentFilename = selectedFile.getAbsolutePath();
                FileSaver fileSaver = new FileSaver(currentFilename);
                fileSaver.setMarks(table.getItems());
                fileSaver.saveFile();
            }
        });

        btn.setOnAction(actionEvent -> {
            invalidInput.setText("");
            try {
                if (9 != getSIDTx.getText().length()) throw new Exception();
                String studentID = getSIDTx.getText();
                float assignments = Float.parseFloat(getAssignmentsTx.getText());
                float midterm = Float.parseFloat(getMidtermTx.getText());
                float finalExam = Float.parseFloat(getFinalExamTx.getText());
                if ((assignments > 100 || assignments < 0) || (midterm > 100 || midterm < 0)
                        || (finalExam > 100 || finalExam < 0)) throw new Exception();
                StudentRecord student = new StudentRecord(studentID,assignments,midterm,finalExam);
                table.getItems().add(student);
            } catch (Exception e) {
                // e.printStackTrace();
                invalidInput.setText("Invalid Input!");
            } finally {
                getSIDTx.clear();
                getAssignmentsTx.clear();
                getMidtermTx.clear();
                getFinalExamTx.clear();
            }
        });

        menuExit.setOnAction(actionEvent -> primaryStage.close());

        Scene scene = new Scene(grid,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
