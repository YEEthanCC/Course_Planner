package ca.cmpt213.as5courseplanner.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manage the collection of departments and reads data from CSV file.
 */
public class DepartmentManager implements Iterable<Department>{
	private List<Department> departments = new ArrayList<>();

	public Department findOrMakeDepartment(String subjectName) {
		for (Department department : departments) {
			if (department.getName().equals(subjectName)) {
				return department;
			}
		}
		Department newDepartment = new Department(subjectName);
		departments.add(newDepartment);
		Collections.sort(departments);
		return newDepartment;
	}

	public Iterator<Department> iterator() {
		return Collections.unmodifiableList(departments).iterator();
	}
	
	
	public void loadDataFromFile(File file) throws FileNotFoundException {
		CSVFileReader reader = new CSVFileReader(file);
		
		for (CSVFileLine fileLine : reader){ 
			addDataFromCSVLine(fileLine);
		}
	}
	private void addDataFromCSVLine(CSVFileLine fileLine) {
		String semesterStr       = fileLine.get(CSVFileLine.COLUMN_SEMESTER);
		String subjectName       = fileLine.get(CSVFileLine.COLUMN_SUBJECT);
		String catalogNumber     = fileLine.get(CSVFileLine.COLUMN_CATALOG_NUMBER);
		String location          = fileLine.get(CSVFileLine.COLUMN_LOCATION); 
		int enrollmentCap        = fileLine.getInt(CSVFileLine.COLUMN_ENROLLMENT_CAP);
		String component         = fileLine.get(CSVFileLine.COLUMN_COMPONENT);
		int enrollmentTotal      = fileLine.getInt(CSVFileLine.COLUMN_ENROLLMENT_TOTAL);
		String instructor        = fileLine.get(CSVFileLine.COLUMN_INSTRUCTOR);


		OfferingDataObject data = new OfferingDataObject(
		        semesterStr,
                subjectName,
                catalogNumber,
                location,
                enrollmentCap,
                component,
                enrollmentTotal,
                instructor);

        addOffering(data);
	}

    public OfferingSection addOffering(OfferingDataObject data) {
        Department department = findOrMakeDepartment(data.getSubjectName());
        Semester semester = new Semester(data.getSemester());
        Course course = department.findOrMakeCourse(data.getCatalogNumber());
        return course.addSection(
                semester,
                data.getLocation(),
                data.getComponent(),
                data.getEnrollmentCap(),
                data.getEnrollmentTotal(),
                data.getInstructor());
    }


    public Department getDepartmentById(long deptId) {
		for (Department department : departments) {
			if (department.getDeptId() == deptId) {
				return department;
			}
		}
		throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
    }

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public class DepartmentNotFoundException extends RuntimeException {
		public DepartmentNotFoundException(String s) {
			super (s);
		}
	}

}
