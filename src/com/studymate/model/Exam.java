package com.studymate.model;
import java.io.Serializable; import java.time.LocalDate;
public class Exam implements Serializable {
 private static final long serialVersionUID=1L; private String subject,examName,time,room; private LocalDate date;
 public Exam(String subject,String examName,LocalDate date,String time,String room){this.subject=subject;this.examName=examName;this.date=date;this.time=time;this.room=room;}
 public String getSubject(){return subject;} public String getExamName(){return examName;} public LocalDate getDate(){return date;} public String getTime(){return time;} public String getRoom(){return room;}
 public void setSubject(String v){subject=v;} public void setExamName(String v){examName=v;} public void setDate(LocalDate v){date=v;} public void setTime(String v){time=v;} public void setRoom(String v){room=v;}
}
