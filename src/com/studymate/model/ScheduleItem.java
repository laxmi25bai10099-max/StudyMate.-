package com.studymate.model;
import java.io.Serializable;
public class ScheduleItem implements Serializable { private static final long serialVersionUID=1L; private String day,timeSlot,subject,room;
 public ScheduleItem(String day,String timeSlot,String subject,String room){this.day=day;this.timeSlot=timeSlot;this.subject=subject;this.room=room;}
 public String getDay(){return day;} public String getTimeSlot(){return timeSlot;} public String getSubject(){return subject;} public String getRoom(){return room;}
 public void setDay(String v){day=v;} public void setTimeSlot(String v){timeSlot=v;} public void setSubject(String v){subject=v;} public void setRoom(String v){room=v;}
}
