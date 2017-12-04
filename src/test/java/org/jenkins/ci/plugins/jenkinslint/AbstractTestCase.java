package org.jenkins.ci.plugins.jenkinslint;

import hudson.model.Item;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Before;
import org.junit.ClassRule;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;

/**
 * AbstractTestCase to speed up Unit tests.
 *
 * @author Victor Martinez
 */
public abstract class AbstractTestCase {
    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    @Before
    public void setUp() throws IOException, InterruptedException {
        for (Item i: j.getInstance().getItems()) {
            i.delete();
        }
    }

    protected WorkflowJob createWorkflow(String script, boolean sandbox) throws Exception {
        WorkflowJob project = j.jenkins.createProject(WorkflowJob.class, "testPipeline");
        if (script!=null && !script.isEmpty()) {
            project.setDefinition(new CpsFlowDefinition(script, sandbox));
        }
        return project;
    }
}
