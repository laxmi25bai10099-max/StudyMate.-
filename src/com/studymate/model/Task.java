package com.studymate.model;
import java.io.Serializable; import java.time.LocalDate;
public class Task implements Serializable {
 private static final long serialVersionUID=1L; private String title,subject,type,priority,status; private LocalDate dueDate;
 public Task(String title,String subject,String type,LocalDate dueDate,String priority,String status){this.title=title;this.subject=subject;this.type=type;this.dueDate=dueDate;this.priority=priority;this.status=status;}
 public String getTitle(){return title;} public String getSubject(){return subject;} public String getType(){return type;} public LocalDate getDueDate(){return dueDate;} public String getPriority(){return priority;} public String getStatus(){return status;}
 public void setTitle(String v){title=v;} public void setSubject(String v){subject=v;} public void setType(String v){type=v;} public void setDueDate(LocalDate v){dueDate=v;} public void setPriority(String v){priority=v;} public void setStatus(String v){status=v;}
}
