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
    private JobAssignedLabelChecker checker = new JobAssignedLabelChecker(false, false);

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
        // TODO: create label
    }
}