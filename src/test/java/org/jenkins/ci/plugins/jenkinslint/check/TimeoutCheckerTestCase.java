package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.plugins.build_timeout.BuildTimeoutWrapper;
import hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy;
import hudson.plugins.build_timeout.impl.DeadlineTimeOutStrategy;
import hudson.plugins.build_timeout.impl.ElasticTimeOutStrategy;
import hudson.plugins.build_timeout.impl.LikelyStuckTimeOutStrategy;
import hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TimeoutChecker Test Case.
 *
 * @author Victor Martinez
 */
public class TimeoutCheckerTestCase {
    private TimeoutChecker checker = new TimeoutChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenJobName() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertTrue(checker.executeCheck(project));
    }
    //@Issue("JENKINS-29444")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithTimeout() throws Exception {
        MatrixProject project = j.createMatrixProject("NoActivityTimeOut");
        project.getBuildWrappersList().add(createNoActivityTimeOut());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("AbsoluteTimeOutStrategy");
        project.getBuildWrappersList().add(createAbsoluteTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("DeadlineTimeOutStrategy");
        project.getBuildWrappersList().add(createDeadlineTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("ElasticTimeOutStrategy");
        project.getBuildWrappersList().add(createElasticTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("LikelyStuckTimeOutStrategy");
        project.getBuildWrappersList().add(createLikelyStuckTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testJobWithTimeout() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("NoActivityTimeOut");
        project.getBuildWrappersList().add(createNoActivityTimeOut());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("AbsoluteTimeOutStrategy");
        project.getBuildWrappersList().add(createAbsoluteTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("DeadlineTimeOutStrategy");
        project.getBuildWrappersList().add(createDeadlineTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("ElasticTimeOutStrategy");
        project.getBuildWrappersList().add(createElasticTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("LikelyStuckTimeOutStrategy");
        project.getBuildWrappersList().add(createLikelyStuckTimeOutStrategy());
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
    }
    //@Issue("JENKINS-29427")
    @Test public void testAnotherBuilders() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("MsBuildBuilder");
        project.getBuildersList().add(new hudson.plugins.msbuild.MsBuildBuilder("", "", "", true, true, true));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Ant");
        project.getBuildersList().add(new hudson.tasks.Ant("","","","",""));
        assertTrue(checker.executeCheck(project));
        project.delete();
    }

    private BuildTimeoutWrapper createNoActivityTimeOut() {
        NoActivityTimeOutStrategy strategy = new NoActivityTimeOutStrategy("120");
        return new BuildTimeoutWrapper(strategy, null, null);
    }

    private BuildTimeoutWrapper createAbsoluteTimeOutStrategy() {
        AbsoluteTimeOutStrategy strategy = new AbsoluteTimeOutStrategy("120");
        return new BuildTimeoutWrapper(strategy, null, null);
    }

    private BuildTimeoutWrapper createDeadlineTimeOutStrategy() {
        DeadlineTimeOutStrategy strategy = new DeadlineTimeOutStrategy("120", 120);
        return new BuildTimeoutWrapper(strategy, null, null);
    }

    private BuildTimeoutWrapper createElasticTimeOutStrategy() {
        ElasticTimeOutStrategy strategy = new ElasticTimeOutStrategy("90","60","5");
        return new BuildTimeoutWrapper(strategy, null, null);
    }

    private BuildTimeoutWrapper createLikelyStuckTimeOutStrategy() {
        LikelyStuckTimeOutStrategy strategy = new LikelyStuckTimeOutStrategy();
        return new BuildTimeoutWrapper(strategy, null, null);
    }
}
