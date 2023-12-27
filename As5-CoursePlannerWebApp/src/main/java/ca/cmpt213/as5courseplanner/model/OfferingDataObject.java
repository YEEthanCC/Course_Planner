package ca.cmpt213.as5courseplanner.model;

/**
 * Store the information found on one line of the CSV file, or data coming in via end-point
 */
public class OfferingDataObject {
    private String semester;
    private String subjectName;
    private String catalogNumber;
    private String location;
    private int enrollmentCap;
    private String component;
    private int enrollmentTotal;
    private String instructor;

    public OfferingDataObject() {
    }

    public OfferingDataObject(String semester, String subjectName, String catalogNumber,
                              String location, int enrollmentCap, String component,
                              int enrollmentTotal, String instructor
    ) {
        this.semester = semester;
        this.subjectName = subjectName;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrollmentCap = enrollmentCap;
        this.component = component;
        this.enrollmentTotal = enrollmentTotal;
        this.instructor = instructor;
    }

    public String getSemester() {
        return semester;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public String getComponent() {
        return component;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public String getInstructor() {
        return instructor;
    }
}