package ca.cmpt213.as5courseplanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade to group model functionality into one interface for centralize access.
 */
public class Model {
	private DepartmentManager departmentManager = new DepartmentManager();
	private List<Watcher> watchers = new ArrayList<>();

    public Model(File dataFile) throws FileNotFoundException {
        loadDataFromFile(dataFile);
    }

    /*
     * Adding new data
     */
    public void loadDataFromFile(File file) throws FileNotFoundException {
        departmentManager.loadDataFromFile(file);
    }
    public OfferingSection addNewOffering(OfferingDataObject newOffering) {
        return departmentManager.addOffering(newOffering);
    }

    /*
	 * Access to department/course/sections/offerings
	 */
	public Iterable<Department> departments(){
		return departmentManager;
	}
    public Department getDepartment(long deptId) {
        return departmentManager.getDepartmentById(deptId);
    }

	public Iterable<Course> getCoursesForDepartment(long deptId) {
	    Department department = departmentManager.getDepartmentById(deptId);
        return department.courses();
    }
    public Course getCourse(long deptId, long courseId) {
	    Department dept = departmentManager.getDepartmentById(deptId);
        return dept.getCourseById(courseId);
    }


    public Iterable<CourseOffering> getOfferingsForCourse(long departmentId, long courseId) {
        Department department = departmentManager.getDepartmentById(departmentId);
        Course course = department.getCourseById(courseId);
        return course.offerings();
    }

	public Iterable<OfferingSection> getSectionsForCourseOffering(long departmentId, long courseId, long offeringId) {
        Department department = departmentManager.getDepartmentById(departmentId);
        Course course = department.getCourseById(courseId);
        CourseOffering offering = course.getOfferingById(offeringId);
        return offering.components();
    }

    /*
     * Graphs
     */
    public List<GraphMaker.DataPoint> getStudentsPerSemester(long deptId) {
        Department department = departmentManager.getDepartmentById(deptId);
        GraphMaker graphMaker = new GraphMaker(department);
        return graphMaker.getData();
    }


    /*
	 * Debug routines to display the contents of the model to the console.
	 */
	public void dumpModelToConsole() {
		for (Department department : departments()) {
			dumpDepartmentToConsole(department);
		}
	}
	private void dumpDepartmentToConsole(Department department) {
		for (Course course : department.courses()) {
			System.out.println(course);
			dumpCourseToConsole(course);
		}
	}
	private void dumpCourseToConsole(Course course) {
		for (CourseOffering offering : course.offerings()) {
			dumpComponentsToConsole(offering);
		}
	}
	private void dumpComponentsToConsole(CourseOffering offering) {
		System.out.println("\t" + offering);
		
		String componentInfo = "";
		for (OfferingSection component : offering.components()) {
			componentInfo += "\t\t" + component + "\n";
		}
		
		System.out.println("\t\t" + componentInfo.trim());
	}

	/*
	    Watcher Support
	 */
    public List<Watcher> getWatchers() {
        return watchers;
    }

    public Watcher addWatcher(Watcher watcher) {
	    watcher.initializeId();
	    watchers.add(watcher);
	    return watcher;
    }

}
