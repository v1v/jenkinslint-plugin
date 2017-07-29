package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStep;
import hudson.tasks.Shell;
import org.jenkins_ci.plugins.run_condition.core.AlwaysRun;
import org.jenkins_ci.plugins.run_condition.core.StringsMatchCondition;
import org.jenkins_ci.plugins.run_condition.logic.ConditionContainer;
import org.jenkins_ci.plugins.run_condition.logic.Or;
import org.jenkinsci.plugins.conditionalbuildstep.ConditionalBuilder;
import org.jenkins_ci.plugins.run_condition.BuildStepRunner;
import org.jenkins_ci.plugins.run_condition.core.BooleanCondition;
import org.jenkins_ci.plugins.run_condition.logic.And;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CyclomaticComplexityChecker Test Case.
 *
 * @author Victor Martinez
 */
public class CyclomaticComplexityCheckerTestCase extends AbstractCheckerTestCase {
    private CyclomaticComplexityChecker checker = new CyclomaticComplexityChecker();

    private ConditionalBuilder booleanCondition() {
        ArrayList<BuildStep> list = new ArrayList<BuildStep>();
        list.add(new Shell("ls"));
        list.add(new Shell("ls"));
        return new ConditionalBuilder(new BooleanCondition("true"), new BuildStepRunner.Run(), list);
    }
    private ConditionalBuilder andCondition() {
        ArrayList<BuildStep> list = new ArrayList<BuildStep>();
        list.add(new Shell("ls"));
        list.add(new Shell("ls"));
        ArrayList<ConditionContainer> conditions = new ArrayList<ConditionContainer>();
        conditions.add(new ConditionContainer(new AlwaysRun()));
        conditions.add(new ConditionContainer(new StringsMatchCondition("test", "test", false)));
        return new ConditionalBuilder(new And(conditions), new BuildStepRunner.Run(), list);
    }
    private ConditionalBuilder orCondition() {
        ArrayList<BuildStep> list = new ArrayList<BuildStep>();
        list.add(new Shell("ls"));
        list.add(new Shell("ls"));
        ArrayList<ConditionContainer> conditions = new ArrayList<ConditionContainer>();
        conditions.add(new ConditionContainer(new AlwaysRun()));
        conditions.add(new ConditionContainer(new StringsMatchCondition("test", "test", false)));
        return new ConditionalBuilder(new Or(conditions), new BuildStepRunner.Run(), list);
    }
    @Test public void testEmptyJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testConditionJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(booleanCondition());
        project.getBuildersList().add(booleanCondition());
        project.getBuildersList().add(andCondition());
        project.getBuildersList().add(andCondition());
        project.getBuildersList().add(orCondition());
        project.getBuildersList().add(orCondition());
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
    }
    // https://github.com/jenkinsci/jenkins/blob/1868c8486bdb7a39a7beec0787bb40e15b80de48/core/src/test/java/hudson/slaves/ChannelPingerTest.java
}
