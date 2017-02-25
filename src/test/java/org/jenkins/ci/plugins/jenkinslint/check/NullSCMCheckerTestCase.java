package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * NullSCMChecker Test Case.
 *
 * @author Victor Martinez
 */
public class NullSCMCheckerTestCase {
    private NullSCMChecker checker = new NullSCMChecker();

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertFalse(checker.executeCheck(project));
    }

    @Issue("JENKINS-42310")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertTrue(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMavenGitSCM() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertTrue(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixGitSCM() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject();
        project.setScm(new hudson.plugins.git.GitSCM(""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
