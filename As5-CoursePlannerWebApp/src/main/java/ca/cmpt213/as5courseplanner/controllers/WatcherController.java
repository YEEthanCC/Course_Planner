
package ca.cmpt213.as5courseplanner.controllers;

import ca.cmpt213.as5courseplanner.controllers.wrappers.ApiCourseWrapper;
import ca.cmpt213.as5courseplanner.controllers.wrappers.ApiWatcherWrapper;
import ca.cmpt213.as5courseplanner.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

/**
 * Watcher controller class to generate the REST interface via Spring Boot.
 */
@RestController()
@RequestMapping("/api/watchers")
public class WatcherController {
    @Autowired
    private Model model;


    // ------------------------
    //  List watchers
    // ------------------------
    @GetMapping()
    public Iterable<ApiWatcherWrapper> getAbout() {
        return WrapperBuilder.listFromJson(
                model.getWatchers(),
                ApiWatcherWrapper.class
        );
    }

    // ------------------------
    //  New Watcher
    // ------------------------
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiWatcherWrapper newWatcher(@RequestBody WatcherArgs watcherInfo) {
        // Find the course
        Department department = model.getDepartment(watcherInfo.deptId);
        Course course = model.getCourse(watcherInfo.deptId, watcherInfo.courseId);

        // Build the watcher
        Watcher watcher = new Watcher(department, course);

        return WrapperBuilder.fromJson(
                model.addWatcher(watcher),
                ApiWatcherWrapper.class
        );
    }
    public static final class WatcherArgs {
        public long deptId;
        public long courseId;
    }

    // ------------------------
    //  Get specific watcher
    // ------------------------
    @GetMapping("/{id}")
    public ApiWatcherWrapper getWatcher(@PathVariable("id") long id) {
        for (Watcher watcher : model.getWatchers()) {
            if (watcher.getId() == id) {
                return WrapperBuilder.fromJson(
                        watcher,
                        ApiWatcherWrapper.class
                );
            }
        }
        throw new ResourceNotFoundException("Unable to find requested watcher.");
    }

    // ------------------------
    //  Delete specific watcher
    // ------------------------
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWatcher(@PathVariable("id") long id) {
        Iterator<Watcher> itr = model.getWatchers().iterator();
        while (itr.hasNext()) {
            Watcher watcher = itr.next();
            if (watcher.getId() == id) {
                itr.remove();
                return;
            }
        }
        throw new ResourceNotFoundException("Unable to find requested watcher.");
    }
}
