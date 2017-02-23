package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.FreeStyleProject;
import hudson.triggers.TimerTrigger;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TimerTriggerCheckerTestCase Test Case.
 *
 * @author Victor Martinez
 */
public class TimerTriggerCheckerTestCase {
    private TimerTriggerChecker checker = new TimerTriggerChecker();

    private static final String TIMER_WITHOUT_H = "20 * * * *";
    private static final String TIMER_WITH_H = "H/15 * * * *";


    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyTriggerLabel() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testWithTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H);
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testWithHTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testWithMultipleTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H + "\n" + TIMER_WITHOUT_H);
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testWithMultipleHTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H + "\n" + TIMER_WITH_H +  "\n" + TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testCommentedTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H + "\n#" + TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
