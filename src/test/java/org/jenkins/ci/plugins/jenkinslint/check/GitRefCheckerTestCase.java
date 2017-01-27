package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import hudson.plugins.git.extensions.GitSCMExtension;
import hudson.plugins.git.extensions.impl.CleanCheckout;
import hudson.plugins.git.extensions.impl.CloneOption;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * GitRefChecker Test Case.
 *
 * @author Victor Martinez
 */
public class GitRefCheckerTestCase {
    private GitRefChecker checker = new GitRefChecker();

    @Rule public JenkinsRule j = new JenkinsRule();

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
    @Test public void testGitSCMWithCloneOptionExtensionNoRefJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(false, "", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testGitSCMWithCloneOptionExtensionRefJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        ArrayList<GitSCMExtension> extensions = new ArrayList<GitSCMExtension>();
        extensions.add(new CloneOption(false, "${WORKSPACE}/.git-cache", 0));
        project.setScm(new hudson.plugins.git.GitSCM(null, null, false, null, null, "", extensions));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
