package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import hudson.model.Slave;
import hudson.os.windows.ManagedWindowsServiceLauncher;
import hudson.slaves.DumbSlave;
import hudson.slaves.JNLPLauncher;
import hudson.slaves.NodeProperty;
import hudson.slaves.RetentionStrategy;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * WindowsSlaveLaunchChecker Test Case.
 *
 * @author Victor Martinez
 */
public class WindowsSlaveLaunchCheckerTestCase {
    private WindowsSlaveLaunchChecker checker = new WindowsSlaveLaunchChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultSlave() throws Exception {
        Slave slave = createLinuxSlave("default", "", "");
        assertFalse(checker.executeCheck(slave));
    }

    @Test public void testWindowsSlave() throws Exception {
        Slave slave = createWindowsSlave("default", "", "somelabel");
        assertFalse(checker.executeCheck(slave));
    }

    @Test public void testControlComment() throws Exception {
        Slave slave = createLinuxSlave("default", "", "");
        assertFalse(checker.isIgnored(slave.getNodeDescription()));
        slave = createLinuxSlave("default", "#lint:ignore:" + checker.getClass().getSimpleName(), "");
        assertTrue(checker.isIgnored(slave.getNodeDescription()));
    }

    private Slave createLinuxSlave(String name, String description, String label) throws Exception {
        return new DumbSlave(name, description, "/wherever", "1", Node.Mode.NORMAL, label, new JNLPLauncher(), RetentionStrategy.NOOP, Collections.<NodeProperty<?>>emptyList());
    }

    private Slave createWindowsSlave(String name, String description, String label) throws Exception {
        return new DumbSlave(name, description, "/wherever", "1", Node.Mode.NORMAL, label, new ManagedWindowsServiceLauncher("user", "pass"), RetentionStrategy.NOOP, Collections.<NodeProperty<?>>emptyList());
    }

    @Test public void testMaster() throws Exception {
        assertFalse(checker.executeCheck(j.hudson));
    }

}
