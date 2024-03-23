package ca.cmpt213.as5courseplanner.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Watcher implements Course.CourseChangeObserver {
    private static AtomicLong nextId = new AtomicLong();
    private long id;
    private Department department;
    private Course course;
    private List<String> events = new ArrayList<>();

    public Watcher(Department deptartment, Course course) {
        this.department = deptartment;
        this.course = course;
        course.registerForCourseChange(this);
    }

    public void initializeId() {
        id = nextId.incrementAndGet();
    }

    @Override
    public void added(CourseOffering offering, OfferingSection section) {
        String message = new Date().toString()
                + ": Added section " + section.getType()
                + " with enrollment (" + section.getEnrollmentTotal()
                + " / " + section.getEnrollmentCap()
                + ") to offering " + offering.getTerm() + " " + offering.getYear();
        events.add(message);
    }

    public long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }
    public Course getCourse() {
        return course;
    }

    public List<String> getEvents() {
        return events;
    }
}
