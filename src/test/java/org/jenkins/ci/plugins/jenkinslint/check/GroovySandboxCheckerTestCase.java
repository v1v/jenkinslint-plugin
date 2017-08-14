package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * GroovySandbox Test Case.
 *
 * @author Victor Martinez
 */
public class GroovySandboxCheckerTestCase extends AbstractTestCase {
    private GroovySandboxChecker checker = new GroovySandboxChecker(true);

    public static final String PIPELINE =   "node('remote') {\n" +
                                            "    ws {\n" +
                                            "        archive '**'\n" +
                                            "    }\n" +
                                            "}";

    @Test
    public void testEmptyAndNullPipeline() throws Exception {
        WorkflowJob project = createWorkflow(null, false);
        assertFalse(checker.executeCheck(project));
        project.delete();project = createWorkflow("", false);
        assertFalse(checker.executeCheck(project));
        project.delete();
    }

    @Test
    public void testGroovySandboxInPipeline() throws Exception {
        WorkflowJob project = createWorkflow(PIPELINE, false);
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = (createWorkflow(PIPELINE, true));
        assertFalse(checker.executeCheck(project));
        project.delete();
    }

    @Test
    public void testGroovySandboxInJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
}
