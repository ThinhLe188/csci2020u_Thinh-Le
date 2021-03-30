package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class FileSaver {
    private String filename;
    private static ObservableList<StudentRecord> marks;

    public FileSaver(String filename){
        this.filename = filename;
        marks = FXCollections.observableArrayList();
    }

    public void setMarks(ObservableList<StudentRecord> marks) {
        this.marks = FXCollections.observableArrayList(marks);
    }

    public void saveFile() {
        Writer writer;
        try {
            writer = new BufferedWriter(new FileWriter(this.filename));
            for (StudentRecord student : marks) {
                String text = student.getStudentID() + "," + student.getAssignments() + ","
                        + student.getMidterm() + "," + student.getFinalExam() + "\n";
                writer.write(text);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
