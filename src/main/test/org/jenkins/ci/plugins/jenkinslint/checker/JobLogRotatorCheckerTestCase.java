package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.FreeStyleProject;
import hudson.tasks.LogRotator;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JobLogRotatorChecker Test Case.
 *
 * @author Victor Martinez
 */
public class JobLogRotatorCheckerTestCase {
    private JobLogRotatorChecker checker = new JobLogRotatorChecker(false, false);

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testEmptyLogRotatorJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        project.setBuildDiscarder(new LogRotator(null,null,null,null));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testLogRotatorJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        project.setBuildDiscarder(new LogRotator(-1,-1,-1,-1));
        assertTrue(checker.executeCheck(project));
    }
}