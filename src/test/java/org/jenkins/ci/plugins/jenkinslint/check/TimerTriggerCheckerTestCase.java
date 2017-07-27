package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.triggers.TimerTrigger;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TimerTriggerCheckerTestCase Test Case.
 *
 * @author Victor Martinez
 */
public class TimerTriggerCheckerTestCase extends AbstractCheckerTestCase {
    private TimerTriggerChecker checker = new TimerTriggerChecker();

    private static final String TIMER_WITHOUT_H = "20 * * * *";
    private static final String TIMER_WITH_H = "H/15 * * * *";
    private static final String TIMER_WITH_COMMENT = " # H 15 1 * * *";

    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyTriggerLabel() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42337")
    @Test public void testWithAtTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger("@daily");
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
        project.delete();
        newTrigger = new TimerTrigger(TIMER_WITHOUT_H + "\n" +"@daily");
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
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
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testCommentedTimerTrigger() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H + "\n#" + TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        project.delete();
        assertTrue(checker.executeCheck(project));
        newTrigger = new TimerTrigger(TIMER_WITH_COMMENT);
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMavenTimerTrigger() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H);
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject();
        newTrigger = new TimerTrigger(TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Issue("JENKINS-42310")
    @Test public void testMatrixTimerTrigger() throws Exception {
        MatrixProject project = j.createMatrixProject();
        TimerTrigger newTrigger = new TimerTrigger(TIMER_WITHOUT_H);
        project.addTrigger(newTrigger);
        project.save();
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject();
        newTrigger = new TimerTrigger(TIMER_WITH_H);
        project.addTrigger(newTrigger);
        project.save();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
