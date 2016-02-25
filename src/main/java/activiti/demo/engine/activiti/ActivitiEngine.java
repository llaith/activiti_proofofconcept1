package activiti.demo.engine.activiti;

import activiti.demo.engine.WorkflowLauncher;
import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nos on 25/02/16.
 */
public class ActivitiEngine {

    private static final Logger log = LoggerFactory.getLogger(WorkflowLauncher.class);

    public final ProcessEngine processEngine;
    public final RuntimeService runtimeService;
    public final RepositoryService repositoryService;
    public final TaskService taskService;
    public final ManagementService managementService;
    public final IdentityService identityService;
    public final HistoryService historyService;
    public final FormService formService;

    public ActivitiEngine() {

        this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.runtimeService = processEngine.getRuntimeService();
        this.repositoryService = processEngine.getRepositoryService();
        this.taskService = processEngine.getTaskService();
        this.managementService = processEngine.getManagementService();
        this.identityService = processEngine.getIdentityService();
        this.historyService = processEngine.getHistoryService();
        this.formService = processEngine.getFormService();

    }

    public ActivitiEngine deploy(final String processFile) {

        this.repositoryService.createDeployment()
                .addClasspathResource(processFile)
                .deploy();

        log.info("Number of process definitions: " + this.repositoryService.createProcessDefinitionQuery().count());

        return this;

    }

    public ActivitiConnector connector() {

        return new ActivitiConnector(this);

    }

}
