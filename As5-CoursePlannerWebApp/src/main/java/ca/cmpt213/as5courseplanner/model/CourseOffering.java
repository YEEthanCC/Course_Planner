package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Store information about a single course offering.
 */
public class CourseOffering implements Comparable<CourseOffering>{
	private static AtomicLong nextId = new AtomicLong();

	private long courseOfferingId;
	private Course course;
	private Semester semester;
	private String location;
	private List<String> instructors = new ArrayList<>();
	private List<OfferingSection> sections = new ArrayList<>();

	public CourseOffering(Course course, Semester semester, String location) {
		this.course = course;
		this.semester = semester;
		this.location = location;
		this.courseOfferingId = nextId.getAndIncrement();
	}

	public long getCourseOfferingId() {
		return courseOfferingId;
	}

	@JsonIgnore
	public Course getCourse() {
		return course;
	}
	@JsonIgnore
	public Semester getSemester() {
		return semester;
	}
	public String getLocation() {
		return location;
	}
	
	public String getInstructors() {
		String strInstructors = "";
		for (String instructor : instructors) {
			if (strInstructors.length() > 0) {
				strInstructors += ", ";
			}
			strInstructors += instructor;
		}
		return strInstructors;
	}
	
	public OfferingSection addSection(String sectionType, int enrollmentCap, int enrollmentTotal, String instructor) {
		addNewInstructors(instructor);

		for (OfferingSection component : sections) {
			if (component.getType().equals(sectionType)) {
				component.addToSection(enrollmentCap, enrollmentTotal);
				return component;
			}
		}
		
		OfferingSection newComponent = new OfferingSection(sectionType, enrollmentCap, enrollmentTotal);
		sections.add(newComponent);
		Collections.sort(sections);

		return newComponent;
	}

	private void addNewInstructors (String manyInstructor) {
		// null / empty means always ignore.
		if (manyInstructor == null || manyInstructor.length() == 0) {
			return;
		}


		// Split into sub instructors
		String[] newInstructors = manyInstructor.split(",");
		for (String instructor : newInstructors) {
		    instructor = instructor.trim();
			if (!instructor.isEmpty() && !instructors.contains(instructor)) {
				instructors.add(instructor);
			}
		}
	}

	public Iterable<OfferingSection> components() {
		return Collections.unmodifiableList(sections);
	}
	
	public boolean matches(Semester semester, String location) {
		return getSemester().equals(semester) && getLocation().equals(location);
	}

	public String getTerm() {
		return semester.getTerm();
	}
	public long getYear() {
	    return semester.getYear();
    }
    public int getSemesterCode() {
	    return semester.getSemesterCode();
    }
	
	@Override
	public int compareTo(CourseOffering other) {
		if (!semester.equals(other.getSemester())) {
			return semester.compareTo(other.getSemester());
		}
		return location.compareTo(other.getLocation());
	}

	@Override
	public String toString() {
		return semester + " in " + location + " by " + getInstructors();
	}
}
