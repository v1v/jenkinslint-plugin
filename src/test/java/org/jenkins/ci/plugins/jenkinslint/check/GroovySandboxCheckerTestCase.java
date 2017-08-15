package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.plugins.groovy.StringScriptSource;
import hudson.plugins.groovy.StringSystemScriptSource;
import hudson.plugins.groovy.SystemGroovy;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Test;
import org.jvnet.hudson.plugins.groovypostbuild.GroovyPostbuildRecorder;
import org.jvnet.hudson.test.Issue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * GroovySandbox Test Case.
 *
 * @author Victor Martinez
 */
public class GroovySandboxCheckerTestCase extends GroovyAbstractCheckerTestCase {
    private GroovySandboxChecker checker = new GroovySandboxChecker(true);

    public static final String PIPELINE =   "node('remote') {\n" +
                                            "    ws {\n" +
                                            "        archive '**'\n" +
                                            "    }\n" +
                                            "}";

    @Test
    public void testEmptyAndNullPipeline() throws Exception {
        WorkflowJob project = createWorkflow(null, false);
        assertFalse(checker.executeCheck(project));
        project.delete();project = createWorkflow("", false);
        assertFalse(checker.executeCheck(project));
        project.delete();
    }

    @Test
    public void testGroovySandboxInPipeline() throws Exception {
        WorkflowJob project = createWorkflow(PIPELINE, false);
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = (createWorkflow(PIPELINE, true));
        assertFalse(checker.executeCheck(project));
        project.delete();
    }

    @Test
    public void testGroovySandboxInJob() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        assertFalse(checker.executeCheck(project));
    }

    @Test public void testGroovySandboxInMavenJob() throws Exception {
        MavenModuleSet project = j.createMavenProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testGroovySandboxInMatrixProject() throws Exception {
        MatrixProject project = j.createMatrixProject();
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testJobWithSystemGroovy() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("SystemWithSandbox");
        project.getBuildersList().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", true, null))));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("SystemWithoutSandbox");
        project.getBuildersList().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", false, null))));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Groovy");
        project.getBuildersList().add(new hudson.plugins.groovy.Groovy(new StringScriptSource("println 'hi'"),"System", "","", "", "" ,""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMatrixGroovyWithSystemGroovy() throws Exception {
        MatrixProject project = j.createMatrixProject("WithSandbox");
        project.getBuildersList().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", true, null))));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("SystemWithoutSandbox");
        project.getBuildersList().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", false, null))));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("Groovy");
        project.getBuildersList().add(new hudson.plugins.groovy.Groovy(new StringScriptSource("println 'hi'"),"System", "","", "", "" ,""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testMavenGroovyWithSystemGroovy() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithSandbox");
        project.getPrebuilders().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", true, null))));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("SystemWithoutSandbox");
        project.getPrebuilders().add(new SystemGroovy(new StringSystemScriptSource(new SecureGroovyScript("bindingVar = null", false, null))));
        assertTrue(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("Groovy");
        project.getPrebuilders().add(new hudson.plugins.groovy.Groovy(new StringScriptSource("println 'hi'"),"System", "","", "", "" ,""));
        assertFalse(checker.executeCheck(project));
    }
    @Test public void testJobWithGroovyPostBuild() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithSandbox");
        SecureGroovyScript without = new SecureGroovyScript("println 'hi'",true,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(without, 0, false));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithoutSandbox");
        SecureGroovyScript with = new SecureGroovyScript("println 'hi'",false,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(with, 0, false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMatrixGroovyPostBuild() throws Exception {
        MatrixProject project = j.createMatrixProject("WithSandbox");
        SecureGroovyScript without = new SecureGroovyScript("println 'hi'",true,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(without, 0, false));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithoutSandbox");
        SecureGroovyScript with = new SecureGroovyScript("println 'hi'",false,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(with, 0, false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenGroovyPostBuild() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithSandbox");
        SecureGroovyScript without = new SecureGroovyScript("println 'hi'",true,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(without, 0, false));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithoutSandbox");
        SecureGroovyScript with = new SecureGroovyScript("println 'hi'",false,null);
        project.getPublishersList().add(new GroovyPostbuildRecorder(with, 0, false));
        assertTrue(checker.executeCheck(project));
    }

    @Test public void testJobWithChoiceParameterGroovy() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithSandbox");
        project.addProperty(createChoiceParameter("println 'hi'",true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithoutSandbox");
        project.addProperty(createChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }

    @Test public void testJobWithCascadeChoiceParameterGroovy() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'",true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithoutSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testJobWithDynamicReferenceParameterGroovy() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("WithSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'",true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("WithoutSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenModuleWithChoiceParameterGroovy() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithSandbox");
        project.addProperty(createChoiceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithoutSandbox");
        project.addProperty(createChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenModuleWithCascadeChoiceParameterGroovy() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithoutSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMavenModuleWithDynamicReferenceParameterGroovy() throws Exception {
        MavenModuleSet project = j.createMavenProject("WithSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMavenProject("WithoutSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithChoiceParameterGroovy() throws Exception {
        MatrixProject project = j.createMatrixProject("WithSandbox");
        project.addProperty(createChoiceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithoutSandbox");
        project.addProperty(createChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithCascadeChoiceParameterGroovy() throws Exception {
        MatrixProject project = j.createMatrixProject("WithSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithoutSandbox");
        project.addProperty(createCascadeChoiceParameter("println 'hi'", false));
        assertTrue(checker.executeCheck(project));
    }
    @Test public void testMatrixProjectWithDynamicReferenceParameterGroovy() throws Exception {
        MatrixProject project = j.createMatrixProject("WithSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'", true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createMatrixProject("WithoutSandbox");
        project.addProperty(createDynamicReferenceParameter("println 'hi'", false));
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
    @Issue("JENKINS-38616")
    @Test public void testAnotherBuilders() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject("MsBuildBuilder");
        project.getBuildersList().add(new hudson.plugins.msbuild.MsBuildBuilder("", "", "", true, true, true));
        assertFalse(checker.executeCheck(project));
        project.delete();
        project = j.createFreeStyleProject("Ant");
        project.getBuildersList().add(new hudson.tasks.Ant("","","","",""));
        assertFalse(checker.executeCheck(project));
    }
}
