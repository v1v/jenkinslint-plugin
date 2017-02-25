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
 * HardcodedScriptChecker Test Case.
 *
 * @author Victor Martinez
 */
public class HardcodedScriptCheckerTestCase {
    private HardcodedScriptChecker checker = new HardcodedScriptChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyJobName() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMavenJobName() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-38616")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithHardcodedScript() throws Exception {
        MatrixProject project = j.createMatrixProject("Bash_Single_Line");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash #single line"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("Bash_Multiple_Line");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("Batch_Single_Line");
        project.getBuildersList().add(new hudson.tasks.BatchFile("echo first"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("Batch_Multiple_Line");
        project.getBuildersList().add(new hudson.tasks.BatchFile("echo first\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testJobWithHardcodedScript() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("Bash_Single_Line");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash #single line"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Bash_Multiple_Line");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Batch_Single_Line");
        project.getBuildersList().add(new hudson.tasks.BatchFile("echo first"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Batch_Multiple_Line");
        project.getBuildersList().add(new hudson.tasks.BatchFile("echo first\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
        MavenModuleSet mavenProject = j.createMavenProject();
        assertFalse(checker.isIgnored(mavenProject.getDescription()));
        mavenProject.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(mavenProject.getDescription()));
    }
    @Issue("JENKINS-38616")
    @Test public void testAnotherBuilders() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("MsBuildBuilder");
        project.getBuildersList().add(new hudson.plugins.msbuild.MsBuildBuilder("", "", "", true, true, true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Ant");
        project.getBuildersList().add(new hudson.tasks.Ant("","","","",""));
        assertFalse(checker.executeCheck(project));
        project.delete();
    }
    @Issue("JENKINS-38616")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-38616")
    @Test public void testMavenModuleJobbWithHardcodedScript() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        project.getPrebuilders().add(new hudson.tasks.Shell("#!/bin/bash #single line"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("Bash_Multiple_Line");
        project.getPrebuilders().add(new hudson.tasks.Shell("#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("Batch_Single_Line");
        project.getPrebuilders().add(new hudson.tasks.BatchFile("echo first"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("Batch_Multiple_Line");
        project.getPrebuilders().add(new hudson.tasks.BatchFile("echo first\nline1\nline2\nline3\nline4\nline5\nline6"));
        assertTrue(checker.executeCheck(project));
    }
}
