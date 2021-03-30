package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
    private String filename;
    private static ObservableList<StudentRecord> marks;

    public FileLoader(String filename){
        this.filename = filename;
        this.marks = FXCollections.observableArrayList();
    }

    public static ObservableList<StudentRecord> getMarks() {
        return marks;
    }

    public void loadFile(){
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.filename));
            while ((line = br.readLine()) != null){
                String[] columns = line.split(",");
                String studentID = columns[0];
                Float assignments = Float.parseFloat(columns[1]);
                Float midterm = Float.parseFloat(columns[2]);
                Float finalExam = Float.parseFloat(columns[3]);
                marks.add(new StudentRecord(studentID, assignments, midterm, finalExam));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
