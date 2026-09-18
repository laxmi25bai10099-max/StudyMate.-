package com.studymate.model;
import java.io.Serializable;
public class Subject implements Serializable {
 private static final long serialVersionUID=1L;
 private String code,name,instructor,room;
 public Subject(String code,String name,String instructor,String room){this.code=code;this.name=name;this.instructor=instructor;this.room=room;}
 public String getCode(){return code;} public String getName(){return name;} public String getInstructor(){return instructor;} public String getRoom(){return room;}
 public void setCode(String v){code=v;} public void setName(String v){name=v;} public void setInstructor(String v){instructor=v;} public void setRoom(String v){room=v;}
 public String toString(){return code+" - "+name;}
}
