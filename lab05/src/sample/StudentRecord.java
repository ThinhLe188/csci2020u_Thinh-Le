package sample;

public class StudentRecord {
    private final String studentID;
    private final Float assignments, midterm, finalExam, finalMark;
    private final Character letterGrade;

    public StudentRecord (String sid, Float assignments, Float midterm, Float finalExam) {
        this.studentID = sid;
        this.assignments = assignments;
        this.midterm = midterm;
        this.finalExam = finalExam;
        this.finalMark = ((this.assignments*2)+(this.midterm*3)+(this.finalExam*5))/10;
        if (this.finalMark < 50) {
            this.letterGrade = 'F';
        } else if (this.finalMark < 60) {
            this.letterGrade = 'D';
        } else  if (this.finalMark < 70) {
            this.letterGrade = 'C';
        } else if (this.finalMark < 80) {
            this.letterGrade = 'B';
        } else {
            this.letterGrade = 'A';
        }
    }

    public String getStudentID() {
        return  this.studentID;
    }

    public Float getAssignments () {
        return  this.assignments;
    }

    public Float getMidterm() {
        return  this.midterm;
    }

    public Float getFinalExam() {
        return  this.finalExam;
    }

    public Float getFinalMark() {
        return  this.finalMark;
    }

    public Character getLetterGrade() {
        return  this.letterGrade;
    }
}
