package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Store information about a course and manage its course offerings.
 */
public class Course implements Comparable<Course>{
	private final static int MAX_UNDER_GRAD_NUMBER = 499;
	private static AtomicLong nextId = new AtomicLong();

	private long courseId;
	private Department department;
	private String catalogNumber;
	private List<CourseOffering> offerings = new ArrayList<>();

	private List<CourseChangeObserver> observers = new ArrayList<>();



	public Course(Department department, String catalogNumber) {
		this.department = department;
		this.catalogNumber = catalogNumber;
        this.courseId = nextId.getAndIncrement();
    }

	public long getCourseId() {
		return courseId;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	private CourseOffering findOrAddNewOffering(Semester semester, String location) {
		for (CourseOffering offering : offerings) {
			if (offering.matches(semester, location)) {
				return offering;
			}
		}
		CourseOffering newOffering = new CourseOffering(this, semester, location);
		offerings.add(newOffering);
		Collections.sort(offerings);
		return newOffering;
	}

    public OfferingSection addSection(Semester semester, String location,
                                      String componentType, int enrollmentCap, int enrollmentTotal, String instructor) {

        CourseOffering offering = findOrAddNewOffering(semester, location);
        OfferingSection section  = offering.addSection(componentType, enrollmentCap, enrollmentTotal, instructor);
        notifyOfferingAdded(offering, new OfferingSection(componentType, enrollmentCap, enrollmentTotal));
        return section;
    }


    public Iterable<CourseOffering> offerings() {
		return new Iterable<CourseOffering>() {
			@Override
			public Iterator<CourseOffering> iterator() {
				return Collections.unmodifiableList(offerings).iterator();
			}
			
		};
	}

	@JsonIgnore
	public boolean isGrad() {
		return getCourseNumberAsInt() > MAX_UNDER_GRAD_NUMBER;
	}
	@JsonIgnore
	public boolean isUndergrad() {
		return getCourseNumberAsInt() <= MAX_UNDER_GRAD_NUMBER;
	}
	private int getCourseNumberAsInt() {
		try {
			return Integer.valueOf("0" + catalogNumber.replaceAll("(\\d*).*", "$1"));
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean matchesCatalogNumber(String catalogNumber) {
		return getCatalogNumber().equals(catalogNumber);
	}

	@Override
	public int compareTo(Course other) {
		if (department.compareTo(other.department) != 0){
			return department.compareTo(other.department); 
		} else {
			return getCatalogNumber().compareTo(other.getCatalogNumber());
		}
	}
	
	@Override
	public String toString() {
		return department.getName() + " " + catalogNumber;
	}

    public CourseOffering getOfferingById(long offeringId) {
        for (CourseOffering offering : offerings) {
            if (offering.getCourseOfferingId() == offeringId) {
                return offering;
            }
        }
        throw new OfferingNotFoundException ("Course offering of ID " + offeringId + " not found.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class OfferingNotFoundException extends RuntimeException {
        public OfferingNotFoundException (String s) {
            super (s);
        }
    }

    /*
        Observable
     */
    public interface CourseChangeObserver {
        void added(CourseOffering newOffering, OfferingSection newSection);
    }

    void registerForCourseChange(CourseChangeObserver obs) {
        observers.add(obs);
    }
    void notifyOfferingAdded(CourseOffering newOffering, OfferingSection newSection) {
        for (CourseChangeObserver obs : observers) {
            obs.added(newOffering, newSection);
        }
    }
}
