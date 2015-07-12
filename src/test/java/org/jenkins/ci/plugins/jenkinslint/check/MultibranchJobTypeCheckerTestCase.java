package org.jenkins.ci.plugins.jenkinslint.check;

import com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MultibranchJobTypeChecker Test Case.
 *
 * @author Victor Martinez
 */
public class MultibranchJobTypeCheckerTestCase {
    private MultibranchJobTypeChecker checker = new MultibranchJobTypeChecker();

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyFreeStyleMultiBranchJob() throws Exception {
        FreeStyleMultiBranchProject project = new FreeStyleMultiBranchProject(j.jenkins,"test");
        j.jenkins.add(project, "test");
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleMultiBranchProject project = new FreeStyleMultiBranchProject(j.jenkins,"test");
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
}
