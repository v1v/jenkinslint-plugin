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
 * HardcodedScriptChecker Test Case.
 *
 * @author Victor Martinez
 */
public class HardcodedScriptCheckerTestCase extends AbstractTestCase {
    private HardcodedScriptChecker checker = new HardcodedScriptChecker(true, HardcodedScriptChecker.THRESHOLD, false);
    private final String SINGLE_LINE_BASH = "#!/bin/bash #single line";
    private final String MULTI_LINE_BASH = "#!/bin/bash\nline1\nline2\nline3\nline4\nline5\nline6";
    private final String SINGLE_LINE_BATCH = "echo first";
    private final String MULTI_LINE_BATCH = "echo first\nline1\nline2\nline3\nline4\nline5\nline6";
    private final String MULTI_EMPTY_LINE_BASH = "#!/bin/bash\n\n\n\n\n\n";
    private final String MULTI_SPACE_LINE1_BASH = "echo first\n\t\n\t\n\t\n\t\n\t\n\t";
    private final String MULTI_SPACE_LINE2_BASH = "echo first\n \n \n \n \n \n ";
    private final String MULTI_LINE_WITH_BLANK1_BASH = "echo first\n\n\n\n\n\nline1";
    private final String MULTI_LINE_WITH_BLANK2_BASH = "echo first\n\n\n\n\n\nline1\nline2";
    private final String MULTI_LINE_WITH_COMMENTS_SHELL = "echo first\n# \n# \n# \n# \n# \n# line1\n #";
    private final String MULTI_LINE_WITH_COMMENTS_BATCH = "echo first\nREM \nREM \nREM \nREM \nREM \nREM line1\nline2";

    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyJobName() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-38616")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-38616")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithHardcodedScript() throws Exception {
        assertFalse(getMatrixProject(SINGLE_LINE_BASH, true));
        assertTrue(getMatrixProject(MULTI_LINE_BASH, true));
        assertFalse(getMatrixProject(SINGLE_LINE_BATCH, false));
        assertTrue(getMatrixProject(MULTI_LINE_BATCH, false));
        MatrixProject project = j.createMatrixProject();
        int threshold = checker.getThreshold();
        checker.setThreshold(14);
        project = j.createMatrixProject();
        project.getBuildersList().add(new hudson.tasks.BatchFile(MULTI_LINE_BATCH));
        assertFalse(checker.executeCheck(project));
        checker.setThreshold(threshold);
    }
    @Test public void testJobWithHardcodedScript() throws Exception {
        assertFalse(getJob(SINGLE_LINE_BASH, true));
        assertTrue(getJob(MULTI_LINE_BASH, true));
        assertFalse(getJob(SINGLE_LINE_BATCH, false));
        assertTrue(getJob(MULTI_LINE_BATCH, false));
        FreeStyleProject project = j.createFreeStyleProject();
        int threshold = checker.getThreshold();
        checker.setThreshold(14);
        project = j.createFreeStyleProject();
        project.getBuildersList().add(new hudson.tasks.BatchFile(MULTI_LINE_BATCH));
        assertFalse(checker.executeCheck(project));
        checker.setThreshold(threshold);
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
        project.getBuildersList().add(new hudson.tasks.Ant("", "", "", "", ""));
        assertFalse(checker.executeCheck(project));
        project.delete();
    }
    @Issue("JENKINS-38616")
    @Test public void testMavenModuleJobbWithHardcodedScript() throws Exception {
        assertFalse(getMavenModule(SINGLE_LINE_BASH, true));
        assertTrue(getMavenModule(MULTI_LINE_BASH, true));
        assertFalse(getMavenModule(SINGLE_LINE_BATCH, true));
        assertFalse(getMavenModule(SINGLE_LINE_BASH, false));
        assertTrue(getMavenModule(MULTI_LINE_BASH, false));
        assertFalse(getMavenModule(SINGLE_LINE_BATCH, false));
        MavenModuleSet project = j.createMavenProject();
        int threshold = checker.getThreshold();
        checker.setThreshold(14);
        project = j.createMavenProject();
        project.getPrebuilders().add(new hudson.tasks.BatchFile(MULTI_LINE_BATCH));
        assertFalse(checker.executeCheck(project));
        checker.setThreshold(threshold);
    }
    @Issue("JENKINS-46035")
    @Test public void testJobWithHardcodedAndBlankLinesScript() throws Exception {
        assertFalse(getJob(MULTI_EMPTY_LINE_BASH, true));
        assertFalse(getJob(MULTI_SPACE_LINE1_BASH, true));
        assertFalse(getJob(MULTI_SPACE_LINE2_BASH, true));
        assertFalse(getJob(MULTI_LINE_WITH_BLANK1_BASH, true));
        assertTrue(getJob(MULTI_LINE_WITH_BLANK2_BASH, true));
    }
    @Issue("JENKINS-46035")
    @Test public void testMavenModuleJobWithHardcodedAndBlankLinesScript() throws Exception {
        assertFalse(getMavenModule(MULTI_EMPTY_LINE_BASH, true));
        assertFalse(getMavenModule(MULTI_SPACE_LINE1_BASH, true));
        assertFalse(getMavenModule(MULTI_SPACE_LINE2_BASH, true));
        assertFalse(getMavenModule(MULTI_LINE_WITH_BLANK1_BASH, true));
        assertTrue(getMavenModule(MULTI_LINE_WITH_BLANK2_BASH, true));
    }
    @Issue("JENKINS-46035")
    @Test public void testMatrixProjectWithHardcodedAndBlankLinesScript() throws Exception {
        assertFalse(getMatrixProject(MULTI_EMPTY_LINE_BASH, true));
        assertFalse(getMatrixProject(MULTI_SPACE_LINE1_BASH, true));
        assertFalse(getMatrixProject(MULTI_SPACE_LINE2_BASH, true));
        assertFalse(getMatrixProject(MULTI_LINE_WITH_BLANK1_BASH, true));
        assertTrue(getMatrixProject(MULTI_LINE_WITH_BLANK2_BASH, true));
    }
    @Issue("JENKINS-46146")
    @Test public void testJobWithCommentedLinesScript() throws Exception {
        assertTrue(getJob(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertTrue(getJob(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(true);
        assertFalse(getJob(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertFalse(getJob(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(false);
    }
    @Issue("JENKINS-46146")
    @Test public void testMavenModuleWithCommentedLinesScript() throws Exception {
        assertTrue(getMavenModule(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertTrue(getMavenModule(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(true);
        assertFalse(getMavenModule(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertFalse(getMavenModule(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(false);
    }
    @Issue("JENKINS-46146")
    @Test public void testMatrixProjectWithCommentedLinesScript() throws Exception {
        assertTrue(getMatrixProject(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertTrue(getMatrixProject(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(true);
        assertFalse(getMatrixProject(MULTI_LINE_WITH_COMMENTS_SHELL, true));
        assertFalse(getMatrixProject(MULTI_LINE_WITH_COMMENTS_BATCH, false));
        checker.setIgnoreComment(false);
    }
    private boolean getMatrixProject(String shell, boolean isUnix) throws Exception {
        MatrixProject project = j.createMatrixProject();
        if (isUnix) {
            project.getBuildersList().add(new hudson.tasks.Shell(shell));
        } else {
            project.getBuildersList().add(new hudson.tasks.BatchFile(shell));
        }
        boolean status = checker.executeCheck(project);
        project.delete();
        return status;
    }

    private boolean getMavenModule(String shell, boolean isUnix) throws Exception {
        MavenModuleSet project = j.createMavenProject();
        if (isUnix) {
            project.getPrebuilders().add(new hudson.tasks.Shell(shell));
        } else {
            project.getPrebuilders().add(new hudson.tasks.BatchFile(shell));
        }
        boolean status = checker.executeCheck(project);
        project.delete();
        return status;
    }

    private boolean getJob(String shell, boolean isUnix) throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        if (isUnix) {
            project.getBuildersList().add(new hudson.tasks.Shell(shell));
        } else {
            project.getBuildersList().add(new hudson.tasks.BatchFile(shell));
        }
        boolean status = checker.executeCheck(project);
        project.delete();
        return status;
    }
}
