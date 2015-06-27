package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JobNameChecker Test Case.
 *
 * @author Victor Martinez
 */
public class JobAssignedLabelCheckerTestCase {
    private JobNameChecker checker = new JobNameChecker("JobNameChecker", false, false);

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyJobName() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testJobNameWithSpaces() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("Name A");
        assertTrue(checker.executeCheck(project));
        project = j.createFreeStyleProject(" Name A");
        assertTrue(checker.executeCheck(project));
        project = j.createFreeStyleProject("Name ");
        assertTrue(checker.executeCheck(project));
        project = j.createFreeStyleProject("Name A B C");
        assertTrue(checker.executeCheck(project));
    }
}