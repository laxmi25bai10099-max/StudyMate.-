package com.studymate.model;
import java.io.Serializable; import java.time.LocalDateTime;
public class Reminder implements Serializable { private static final long serialVersionUID=1L; private String title,message; private LocalDateTime dateTime; private boolean done;
 public Reminder(String title,String message,LocalDateTime dateTime){this.title=title;this.message=message;this.dateTime=dateTime;}
 public String getTitle(){return title;} public String getMessage(){return message;} public LocalDateTime getDateTime(){return dateTime;} public boolean isDone(){return done;}
 public void setTitle(String v){title=v;} public void setMessage(String v){message=v;} public void setDateTime(LocalDateTime v){dateTime=v;} public void setDone(boolean v){done=v;}
}
