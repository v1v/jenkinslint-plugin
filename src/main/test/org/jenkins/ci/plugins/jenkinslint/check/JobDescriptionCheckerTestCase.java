package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JobDescriptionChecker Test Case.
 *
 * @author Victor Martinez
 */
public class JobDescriptionCheckerTestCase {
    private JobDescriptionChecker checker = new JobDescriptionChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testEmptyJobName() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testJobDescription() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.setDescription("Some Description");
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignored:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
