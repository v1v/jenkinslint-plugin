package org.jenkins.ci.plugins.jenkinslint.check;

import com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject;
import hudson.model.FreeStyleProject;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MultibranchJobTypeChecker Test Case.
 *
 * @author Victor Martinez
 */
public class MultibranchJobTypeCheckerTestCase extends AbstractTestCase {
    private MultibranchJobTypeChecker checker = new MultibranchJobTypeChecker(true);

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
