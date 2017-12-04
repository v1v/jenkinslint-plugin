package org.jenkins.ci.plugins.jenkinslint.check;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

import static org.junit.Assert.assertFalse;

/**
 * Random Test Case. Using another TestClass to be able to inject the LocalData using the Rule annotation
 *
 * @author Victor Martinez
 */
public class RandomTestCase {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Issue("JENKINS-46424")
    @LocalData
    @Test public void testMultiJobWithNPEParameters() throws Exception {
        assertFalse(new GroovySandboxChecker(true).executeCheck(j.getInstance().getItem("test_multi_stage")));
        assertFalse(new GroovySystemExitChecker(true).executeCheck(j.getInstance().getItem("test_multi_stage")));
    }

    @Issue("JENKINS-46383")
    @LocalData
    @Test public void testDoubleQuoteLabels() throws Exception {
        assertFalse(new JobAssignedLabelChecker(true).executeCheck(j.getInstance().getItem("doublespaces")));
    }
}
