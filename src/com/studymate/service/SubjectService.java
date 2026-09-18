package com.studymate.service;

import com.studymate.model.Subject;
import com.studymate.repository.DataStore;

public class SubjectService {
    private final DataStore data;
    public SubjectService(DataStore data) { this.data = data; }
    public String validateNew(String code, String name, String instructor, String room) {
        String[] values = {code, name, instructor, room};
        String[] fields = {"Subject code", "Subject name", "Instructor", "Room"};
        for (int i = 0; i < values.length; i++) { String e = ValidationUtil.required(values[i], fields[i]); if (e != null) return e; }
        for (Subject s : data.getSubjects()) if (s.getCode().equalsIgnoreCase(code.trim())) return "Subject code already exists.";
        return null;
    }
}
