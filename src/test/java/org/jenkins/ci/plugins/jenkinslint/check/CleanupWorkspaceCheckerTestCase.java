package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.plugins.ws_cleanup.PreBuildCleanup;
import hudson.plugins.ws_cleanup.WsCleanup;
import hudson.tasks.ArtifactArchiver;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CleanupWorkspaceChecker Test Case.
 *
 * @author Victor Martinez
 */
public class CleanupWorkspaceCheckerTestCase extends AbstractTestCase {
    private CleanupWorkspaceChecker checker = new CleanupWorkspaceChecker(true);

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
    @Issue("JENKINS-42310")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertTrue(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMavenCleanupWorkspacePublisher() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithPublisher");
        project.getPublishersList().add(new WsCleanup(null, true, true, true, true, true, true, true, true, ""));
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertTrue(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProjectCleanupWorkspacePublisher() throws Exception {
        MatrixProject project = j.createMatrixProject("WithoutSystem");
        project.getPublishersList().add(new WsCleanup(null, true, true, true, true, true, true, true, true, ""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
    }
}
