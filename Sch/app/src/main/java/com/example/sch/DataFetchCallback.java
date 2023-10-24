package com.example.sch;

import java.util.List;
import java.util.Map;

public interface DataFetchCallback {
    void onDataFetched(List<String> studentNames, List<String> rollNumbers, List<String> studentSubjects, Map<String, Map<String, Map<String, String>>> attendanceMap,List<String> userIds);
}


