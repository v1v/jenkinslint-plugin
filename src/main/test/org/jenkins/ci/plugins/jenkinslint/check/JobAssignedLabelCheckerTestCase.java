package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JobAssignedLabelChecker Test Case.
 *
 * @author Victor Martinez
 */
public class JobAssignedLabelCheckerTestCase {
    private JobAssignedLabelChecker checker = new JobAssignedLabelChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testEmptyAssignedLabel() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testWithAssignedLabel() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        j.createSlave("test",null);
        project.setAssignedLabel(j.jenkins.getLabel("test"));
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignored:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
