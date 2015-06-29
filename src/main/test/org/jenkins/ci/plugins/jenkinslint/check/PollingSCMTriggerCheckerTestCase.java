package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import hudson.triggers.SCMTrigger;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * PollingSCMTrigger Test Case.
 *
 * @author Victor Martinez
 */
public class PollingSCMTriggerCheckerTestCase {
    private PollingSCMTriggerChecker checker = new PollingSCMTriggerChecker(false, false);

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testTriggerSCMJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.addTrigger(new SCMTrigger("", true));
        assertFalse(checker.executeCheck(project));
    }
}