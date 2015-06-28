package org.jenkins.ci.plugins.jenkinslint.checker;

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
 * NullSCMChecker Test Case.
 *
 * @author Victor Martinez
 */
public class NullSCMCheckerTestCase {
    private NullSCMChecker checker = new NullSCMChecker(false, false);

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
}