package com.studymate.model;
import java.io.Serializable; import java.time.LocalDate;
public class Note implements Serializable {
 private static final long serialVersionUID=1L; private String title,subject,content,tags; private LocalDate date;
 public Note(String title,String subject,String content,String tags,LocalDate date){this.title=title;this.subject=subject;this.content=content;this.tags=tags;this.date=date;}
 public String getTitle(){return title;} public String getSubject(){return subject;} public String getContent(){return content;} public String getTags(){return tags;} public LocalDate getDate(){return date;}
 public void setTitle(String v){title=v;} public void setSubject(String v){subject=v;} public void setContent(String v){content=v;} public void setTags(String v){tags=v;} public void setDate(LocalDate v){date=v;}
}
