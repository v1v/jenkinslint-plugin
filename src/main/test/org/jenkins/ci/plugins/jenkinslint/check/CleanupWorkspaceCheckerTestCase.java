package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import hudson.plugins.ws_cleanup.PreBuildCleanup;
import hudson.plugins.ws_cleanup.WsCleanup;
import hudson.tasks.ArtifactArchiver;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CleanupWorkspaceChecker Test Case.
 *
 * @author Victor Martinez
 */
public class CleanupWorkspaceCheckerTestCase {
    private CleanupWorkspaceChecker checker = new CleanupWorkspaceChecker();

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testPublisherJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getPublishersList().add(new ArtifactArchiver("","",false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testCleanupWorkspacePublisher() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getPublishersList().add(new WsCleanup(null, true, true, true, true, true, true, true, true, ""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testPreBuildCleanupPublisher() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildWrappersList().add(new PreBuildCleanup(null, true, "", ""));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignored:" + checker.getClass().getSimpleName());
    }
}
