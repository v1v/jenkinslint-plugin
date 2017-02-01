package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.plugins.groovy.ScriptSource;
import hudson.plugins.groovy.StringScriptSource;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * GroovySystemExitChecker Test Case.
 *
 * @author Victor Martinez
 */
public class GroovySystemExitCheckerTestCase {
    private GroovySystemExitChecker checker = new GroovySystemExitChecker();

    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void testDefaultJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testEmptyJobName() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("");
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMavenJobName() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    //@Issue("JENKINS-29444")
    @Test public void testMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithSystemGroovy() throws Exception {
        MatrixProject project = j.createMatrixProject("WithoutSystem");
        project.getBuildersList().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("println 'hi'"),null,null));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithSystem");
        project.getBuildersList().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("System.exit(0)"),null,null));
        project.save();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testJobWithSystemGroovy() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithoutSystem");
        project.getBuildersList().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("println 'hi'"),null,null));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithSystem");
        project.getBuildersList().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("System.exit(0)"),null,null));
        project.save();
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testControlComment() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.isIgnored(project.getDescription()));
        project.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(project.getDescription()));
        project.delete();
        MavenModuleSet mavenProject = j.createMavenProject();
        assertFalse(checker.isIgnored(mavenProject.getDescription()));
        mavenProject.setDescription("#lint:ignore:" + checker.getClass().getSimpleName());
        assertTrue(checker.isIgnored(mavenProject.getDescription()));
    }
    //@Issue("JENKINS-29427")
    @Test public void testAnotherBuilders() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("MsBuildBuilder");
        project.getBuildersList().add(new hudson.plugins.msbuild.MsBuildBuilder("", "", "", true, true, true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Ant");
        project.getBuildersList().add(new hudson.tasks.Ant("","","","",""));
        assertFalse(checker.executeCheck(project));
        project.delete();
    }
    //@Issue("JENKINS-38616")
    @Test public void testMavenModuleJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    //@Issue("JENKINS-38616")
    @Test public void testMavenModuleJobbWithGroovy() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithoutSystem");
        project.getPrebuilders().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("println 'hi'"),null,null));
        project.save();
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithSystem");
        project.getPrebuilders().add(new hudson.plugins.groovy.SystemGroovy(new StringScriptSource("System.exit(0)"),null,null));
        assertTrue(checker.executeCheck(project));
    }
}
