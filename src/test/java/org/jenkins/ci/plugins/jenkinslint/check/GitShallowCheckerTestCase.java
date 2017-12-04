package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.plugins.git.extensions.GitSCMExtension;
import hudson.plugins.git.extensions.impl.CleanCheckout;
import hudson.plugins.git.extensions.impl.CloneOption;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CleanupWorkspaceChecker Test Case.
 *
 * @author Victor Martinez
 */
public class GitShallowCheckerTestCase extends AbstractTestCase {
    private GitShallowChecker checker = new GitShallowChecker(true);

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testGitSCMJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithFurtherValuesJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", null));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithEmptyExtensionJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithSomeExtensionJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CleanCheckout());
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
        extensions.add(new CloneOption(false, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithCloneOptionExtensionNoShallowJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(false, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithCloneOptionExtensionShallowJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(true, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertFalse(checker.executeCheck(project));
    }

    @Issue("JENKINS-42310")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMavenGit() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(false, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject();
        extensions.add(new CloneOption(true, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProjectGit() throws Exception {
        MatrixProject project = j.createMatrixProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(false, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject();
        extensions.add(new CloneOption(true, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
    @Test public void testWorkflowJob() throws Exception {
        assertFalse(checker.executeCheck(createWorkflow(null, true)));
    }
}
