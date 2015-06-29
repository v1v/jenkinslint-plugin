package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import hudson.tasks.ArtifactArchiver;
import hudson.tasks.JavadocArchiver;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * ArtifactChecker Test Case.
 *
 * @author Victor Martinez
 */
public class ArtifactCheckerTestCase {
    private ArtifactChecker checker = new ArtifactChecker(false, false);

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testPublisherJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getPublishersList().add(new JavadocArchiver("",false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyArtifactPublisher() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getPublishersList().add(new ArtifactArchiver("","",false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testArtifactPublisher() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getPublishersList().add(new ArtifactArchiver("something","",false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored());
        project.setDescription("#lint:ignored:" + checker.getClass().getSimpleName());
        checker.setIgnored(project.getDescription());
        assertTrue(checker.isIgnored());
    }
}
