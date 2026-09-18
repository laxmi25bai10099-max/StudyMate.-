import com.studymate.model.*;
import com.studymate.repository.DataStore;
import com.studymate.service.StudyAnalytics;
import com.studymate.service.SubjectService;
import com.studymate.service.TaskService;

import java.time.LocalDate;

/** Lightweight dependency-free validation tests for the VITyarthi submission. */
public class StudyMateSelfTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        DataStore ds = new DataStore();
        ds.getSubjects().add(new Subject("CS101", "Java", "Teacher", "LAB-1"));
        ds.getTasks().add(new Task("Lab", "Java", "Assignment", LocalDate.now().plusDays(1), "High", "Pending"));
        ds.getTasks().add(new Task("Revision", "Java", "Study", LocalDate.now().plusDays(10), "Low", "Completed"));
        ds.getGoals().add(new Goal("Solve problems", "Academic", 5, 10));
        ds.getHabits().add(new Habit("Study", new boolean[]{true,true,true,false,false,false,false}, 3, 3));

        check("Subject duplicate validation", new SubjectService(ds).validateNew("CS101", "Java 2", "Teacher", "LAB-2") != null);
        check("Task required validation", new TaskService(ds).validate("", "Java", "Assignment", LocalDate.now().toString()) != null);
        check("Task completion percent", StudyAnalytics.taskCompletionPercent(ds) == 50);
        check("Goal progress percent", StudyAnalytics.goalProgressPercent(ds) == 50);
        check("Habit consistency percent", StudyAnalytics.habitConsistencyPercent(ds) == 42);
        check("Priority engine returns pending task", StudyAnalytics.prioritizedTasks(ds).size() == 1);

        System.out.println("StudyMate self-test: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) System.exit(1);
    }

    private static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("PASS: " + name); }
        else { failed++; System.out.println("FAIL: " + name); }
    }
}
