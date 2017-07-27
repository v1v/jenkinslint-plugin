package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JobNameChecker Test Case.
 *
 * @author Victor Martinez
 */
public class JobNameCheckerTestCase extends AbstractCheckerTestCase {
    private JobNameChecker checker = new JobNameChecker();

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
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
