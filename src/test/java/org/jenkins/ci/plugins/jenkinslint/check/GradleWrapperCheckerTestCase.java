package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * GradleWrapperChecker Test Case.
 *
 * @author Victor Martinez
 */
public class GradleWrapperCheckerTestCase extends AbstractTestCase {
    private GradleWrapperChecker checker = new GradleWrapperChecker(true);

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
    @Test public void testMatrixProjectWithGradlew() throws Exception {
        MatrixProject project = j.createMatrixProject("WithoutGradle");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash #single line"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithoutWrapper");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6"));
        project.getBuildersList().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", false, false, false, false));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithWrapper");
        project.getBuildersList().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", true, false, false, false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testJobWithGradlew() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithoutGradle");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash #single line"));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithoutWrapper");
        project.getBuildersList().add(new hudson.tasks.Shell("#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6"));
        project.getBuildersList().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", false, false, false, false));
        project.save();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithWrapper");
        project.getBuildersList().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", true, false, false, false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
        project.delete();
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
        project = j.createMavenProject("WithoutWrapper");
        project.getPrebuilders().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", false, false, false, false));
        project.save();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithWrapper");
        project.getPrebuilders().add(new hudson.plugins.gradle.Gradle("description","switches","tasks","rootBuildScriptDir","buildFile","gradleName", true, false, false, false));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testWorkflowJob() throws Exception {
        assertFalse(checker.executeCheck(createWorkflow(null, true)));
    }
}
