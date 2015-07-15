package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
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
    //@Issue("JENKINS-29444")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
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
    }
    //@Issue("JENKINS-29427")
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
}
