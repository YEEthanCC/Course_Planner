package ca.cmpt213.as5courseplanner.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Store information about a department (such as CMPT or MATH)
 * and manage its courses.
 */
public class Department implements Comparable<Department>{
	private static AtomicLong nextId = new AtomicLong();

	private long deptId;
	private String name; 
	private List<Course> courses = new ArrayList<>();

	public Department(String name) {
		this.name = name;
		this.deptId = nextId.getAndIncrement();
	}

	public long getDeptId() {
		return deptId;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}
	
	public Iterable<Course> courses() {
		return () -> Collections.unmodifiableList(courses).iterator();
	}

	public String getName() {
		return name;
	}
	
	public Course findOrMakeCourse(String catalogNumber) {
		for (Course course : courses) {
			if (course.matchesCatalogNumber(catalogNumber)) {
				return course;
			}
		}
		Course newCourse = new Course(this, catalogNumber);
		courses.add(newCourse);
		Collections.sort(courses);
		return newCourse;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int compareTo(Department other) {
		return name.compareTo(other.getName());
	}

    public Course getCourseById(long courseId) {
	    for (Course course : courses) {
	        if (course.getCourseId() == courseId) {
	            return course;
            }
        }
        throw new CourseNotFoundException("Course of ID " + courseId + " not found.");
    }

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String s) {
            super (s);
        }
    }
}
