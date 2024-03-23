package ca.cmpt213.as5courseplanner.model;

import java.util.*;

public class GraphMaker {
    private final static String LECTURE_SECTION_TYPE = "LEC";
    private Department department;

    public GraphMaker(Department department) {
        this.department = department;
    }

    public List<DataPoint> getData() {
        Map<Integer, Integer> data = new HashMap<>();
        for (Course course : department.courses()) {
            for (CourseOffering offering : course.offerings()) {
                int semesterCode = offering.getSemesterCode();
                for (OfferingSection section : offering.components()) {
                    if (section.getType().equalsIgnoreCase(LECTURE_SECTION_TYPE)) {
                        int count = 0;
                        if (data.containsKey(semesterCode)) {
                            count = data.get(semesterCode);
                        }
                        count += section.getEnrollmentTotal();
                        data.put(semesterCode, count);
                    }
                }
            }
        }

        // Handle no values:
        if (data.isEmpty()) {
            return new ArrayList<>();
        }

        // Find the first and last semesters
        List<Integer> semesterList = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : data.entrySet()) {
            semesterList.add(entry.getKey());
        }
        Collections.sort(semesterList);

        List<DataPoint> dataList = new ArrayList<>();
        Semester startSem = new Semester(semesterList.get(0));
        Semester endSem = new Semester(semesterList.get(semesterList.size() - 1));

        // Build return data
        for (Semester curSem = startSem; curSem.compareTo(endSem) <= 0; curSem = curSem.getNextSemester()) {
            int value = 0;
            if (data.containsKey(curSem.getSemesterCode())) {
                value = data.get(curSem.getSemesterCode());
            }
            dataList.add(
                    new DataPoint(curSem.getSemesterCode(), value)
            );

        }
        return dataList;
    }

    public class DataPoint implements Comparable<DataPoint> {
        public long semesterCode;
        public long totalCoursesTaken;

        public DataPoint(long semesterCode, long totalCoursesTaken) {
            this.semesterCode = semesterCode;
            this.totalCoursesTaken = totalCoursesTaken;
        }


        @Override
        public int compareTo(DataPoint o) {
            return Long.compare(this.semesterCode, o.semesterCode);
        }
    }
}
