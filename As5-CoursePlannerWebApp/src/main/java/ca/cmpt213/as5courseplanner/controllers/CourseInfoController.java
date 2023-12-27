package ca.cmpt213.as5courseplanner.controllers;

import ca.cmpt213.as5courseplanner.controllers.wrappers.*;
import ca.cmpt213.as5courseplanner.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Offering controller class to generate the REST interface via Spring Boot.
 */
@RestController
@RequestMapping("/api")
public class CourseInfoController {
    @Autowired
    private Model model;


    // ------------------------
    //  About
    // ------------------------
    @GetMapping("/about")
    public ApiAboutWrapper getAbout() {
        return new ApiAboutWrapper("Brian's Awesome Sample App", "Brian Fraser");
    }

    // ------------------------
    //  Dump Model
    // ------------------------
    @GetMapping("/dump-model")
    public void dumpModel() {
        model.dumpModelToConsole();
    }

    // ------------------------
    //  Department
    // ------------------------
    @GetMapping("/departments")
    public Iterable<ApiDepartmentWrapper> getDepartments() {
        return WrapperBuilder.listFromJson(
                model.departments(),
                ApiDepartmentWrapper.class
        );
    }

    // ------------------------
    //  Get Courses
    // ------------------------
    @GetMapping("/departments/{deptId}/courses")
    public Iterable<ApiCourseWrapper> getDepartmentCourses(
            @PathVariable("deptId") long deptId
    ) {
        return WrapperBuilder.listFromJson(
                model.getCoursesForDepartment(deptId),
                ApiCourseWrapper.class
        );
    }


    // ------------------------
    //  Get Offerings
    // ------------------------
    @GetMapping("/departments/{deptId}/courses/{courseId}/offerings")
    public Iterable<ApiCourseOfferingWrapper> getCourseOfferings(
            @PathVariable("deptId") long deptId,
            @PathVariable("courseId") long courseId
    ) {
        return WrapperBuilder.listFromJson(model.getOfferingsForCourse(deptId, courseId), ApiCourseOfferingWrapper.class);
    }

    // ------------------------
    //  Get Sections of offerings
    // ------------------------
    @GetMapping("/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    public Iterable<ApiOfferingSectionWrapper> getCourseOfferings(
            @PathVariable("deptId") long deptId,
            @PathVariable("courseId") long courseId,
            @PathVariable("offeringId") long offeringId

    ) {
        return WrapperBuilder.listFromJson(model.getSectionsForCourseOffering(deptId, courseId, offeringId), ApiOfferingSectionWrapper.class);
    }


    // ------------------------
    //  Add new section
    // ------------------------
    @PostMapping("/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiOfferingSectionWrapper addOffering(
            @RequestBody OfferingDataObject newOffering
    ) {
        return WrapperBuilder.fromJson(model.addNewOffering(newOffering), ApiOfferingSectionWrapper.class);
    }



    // ------------------------
    //  Get Graph Data
    // ------------------------
    @GetMapping("/stats/students-per-semester")
    public Iterable<ApiGraphDataPointWrapper> getStudentsPerSemester(
            @RequestParam("deptId") long deptId
    ) {
        return WrapperBuilder.listFromJson(
                model.getStudentsPerSemester(deptId),
                ApiGraphDataPointWrapper.class
        );
    }

    // ----------------------------
    //  Get data for each semester
    // ----------------------------
    @GetMapping("/stats/data-for_each-semester")
    public Iterable<ApiGraphDataPointWrapper> getDataForEachSemester(
            @RequestParam("deptId") long deptId
    ) {
        return WrapperBuilder.listFromJson(
                model.getStudentsPerSemester(deptId + 1),
                ApiGraphDataPointWrapper.class
        );
    }
}
