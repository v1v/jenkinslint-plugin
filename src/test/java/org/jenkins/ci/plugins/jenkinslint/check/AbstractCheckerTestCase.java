package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.junit.Before;
import org.junit.ClassRule;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;

/**
 * AbstractCheckerTestCase to speed up Unit tests.
 *
 * @author Victor Martinez
 */
public abstract class AbstractCheckerTestCase {
    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    @Before
    public void setUp() throws IOException, InterruptedException {
        for (Item i: j.getInstance().getItems()) {
            i.delete();
        }
    }
}
