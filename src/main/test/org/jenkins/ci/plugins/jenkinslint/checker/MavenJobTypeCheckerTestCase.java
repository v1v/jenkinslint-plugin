package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.triggers.SCMTrigger;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MavenJobTypeChecker Test Case.
 *
 * @author Victor Martinez
 */
public class MavenJobTypeCheckerTestCase {
    private MavenJobTypeChecker checker = new MavenJobTypeChecker("MavenJobTypeChecker", false, false);

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject("test");
        assertTrue(checker.executeCheck(project));
    }
}