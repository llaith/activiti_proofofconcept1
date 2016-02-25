package activiti.demo.engine.activiti;

import activiti.demo.engine.activiti.ActivitiConnector.Command;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by nos on 25/02/16.
 */
public class ActivitiCommandFactory {

    private static final Logger log = LoggerFactory.getLogger(ActivitiCommandFactory.class);

    public Command newTaskAcceptCommand(final String assignee) {

        return activiti -> {

            log.info("Checking for new tasks.");

            // Fetch all un-assigned tasks
            TaskService taskService = activiti.processEngine.getTaskService();
            List<Task> tasks = taskService.createTaskQuery().taskUnassigned().list();
            for (Task task : tasks) {
                log.info("New task available: " + task.getName());

                taskService.setAssignee(task.getId(),assignee);

                log.info("Assigned to: " + assignee);

            }

        };

    }


    public Command newTaskCompleteCommand(final String assignee) {

        return activiti -> {

            log.info("Checking for completed tasks.");

            // Fetch all un-assigned tasks
            TaskService taskService = activiti.processEngine.getTaskService();
            List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
            for (Task task : tasks) {
                log.info("Completed task available: " + task.getName());

                taskService.complete(task.getId());
                log.info("Completed: " + task.getId());

            }

        };

    }

    public Command startProcess(final String processName, final Map<String, Object> vars) {

        return activiti -> {

            activiti.runtimeService.startProcessInstanceByKey(processName, vars);

            // Verify that we started a new process instance
            log.info(String.format(
                    "Number of process instances: %s",
                    activiti.runtimeService.createProcessInstanceQuery().count()));


        };

    }

    public Command newUser(final String userId) {

        return activiti -> {

            final User user = activiti.identityService.newUser(userId);

            activiti.identityService.saveUser(user);

        };

    }


}






