package com.studymate.service;

import com.studymate.model.Task;
import com.studymate.repository.DataStore;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProgressService {
    private final DataStore data;
    public ProgressService(DataStore data) { this.data = data; }
    public Map<String, Integer> subjectCompletion() {
        Map<String, Integer> result = new LinkedHashMap<>();
        Map<String, int[]> counts = new LinkedHashMap<>();
        for (Task task : data.getTasks()) {
            int[] c = counts.get(task.getSubject());
            if (c == null) { c = new int[2]; counts.put(task.getSubject(), c); }
            c[0]++;
            if ("Completed".equalsIgnoreCase(task.getStatus())) c[1]++;
        }
        for (Map.Entry<String, int[]> e : counts.entrySet()) {
            int[] c = e.getValue(); result.put(e.getKey(), c[0] == 0 ? 0 : c[1] * 100 / c[0]);
        }
        return result;
    }
}
