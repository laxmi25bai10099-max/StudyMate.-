package com.studymate.model;
import java.io.Serializable;
public class Goal implements Serializable { private static final long serialVersionUID=1L; private String title,category; private int currentProgress,maxProgress;
 public Goal(String title,String category,int currentProgress,int maxProgress){this.title=title;this.category=category;this.currentProgress=currentProgress;this.maxProgress=maxProgress;}
 public String getTitle(){return title;} public String getCategory(){return category;} public int getCurrentProgress(){return currentProgress;} public int getMaxProgress(){return maxProgress;}
 public void setTitle(String v){title=v;} public void setCategory(String v){category=v;} public void setCurrentProgress(int v){currentProgress=Math.max(0,Math.min(v,maxProgress));} public void setMaxProgress(int v){maxProgress=Math.max(1,v); if(currentProgress>maxProgress)currentProgress=maxProgress;}
}
