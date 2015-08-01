package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.slaves.DumbSlave;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertTrue;

/**
 * SlaveDescriptionChecker Test Case.
 *
 * @author Victor Martinez
 */
public class SlaveDescriptionCheckerTestCase {
    private SlaveDescriptionChecker checker = new SlaveDescriptionChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultSlave() throws Exception {
        DumbSlave slave = j.createSlave();
        assertTrue(checker.executeCheck(slave));
    }
    @Test public void testEmptySlaveName() throws Exception {
        DumbSlave slave = j.createSlave();
        slave.setNodeName("");
        assertTrue(checker.executeCheck(slave));
    }
    @Test public void testSlaveDescription() throws Exception {
        DumbSlave slave = j.createSlave();
        slave.setNodeName("blablabla");
        assertTrue(checker.executeCheck(slave));
    }
    /**
    @Test public void testControlComment() throws Exception {
        DumbSlave slave = j.createSlave();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }*/
}
