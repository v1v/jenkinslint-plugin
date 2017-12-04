package org.jenkins.ci.plugins.jenkinslint.check;

import com.sonyericsson.jenkins.plugins.bfa.model.ScannerJobProperty;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * BFAChecker Test Case.
 *
 * @author Victor Martinez
 */
public class BFACheckerTestCase extends AbstractTestCase {
    private BFAChecker checker = new BFAChecker(true);

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testDisabledBFAJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.addProperty(new ScannerJobProperty(true));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testEnabledBFAJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.addProperty(new ScannerJobProperty(false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject("doNotScan");
        project.addProperty(new ScannerJobProperty(true));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("Scan");
        project.addProperty(new ScannerJobProperty(false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMavenModule() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        project.addProperty(new ScannerJobProperty(true));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("Scan");
        project.addProperty(new ScannerJobProperty(false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
    @Test public void testWorkflowJob() throws Exception {
        WorkflowJob project = createWorkflow(null, false);
        assertTrue(checker.executeCheck(project));
        project.addProperty(new ScannerJobProperty(false));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = createWorkflow(null, false);
        project.addProperty(new ScannerJobProperty(true));
        assertTrue(checker.executeCheck(project));
    }
}
