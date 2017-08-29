package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.matrix.Axis;
import hudson.matrix.AxisList;
import hudson.matrix.MatrixProject;
import hudson.matrix.TextAxis;
import hudson.model.Action;
import hudson.model.FreeStyleProject;
import jenkins.model.GlobalConfiguration;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author victor.martinez.
 */
public class JenkinsLintActionTestCase extends AbstractTestCase{

    private static final String EMPTY_TABLE = "<tbody id=\"jenkinsLintTableBody\"></tbody></table>";
    private static final String URL = "jenkinslint";

    @Test
    public void testEmptyJob() throws Exception {
        HtmlPage page = j.createWebClient().goTo(URL);
        WebAssert.assertTextPresent(page, "JenkinsLint");
        assertTrue(page.getWebResponse().getContentAsString().contains(EMPTY_TABLE));
        WebAssert.assertTextPresent(page, "JL-1");
    }
    @Test
    public void testJob() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);
        HtmlPage htmlPage = j.createWebClient().goTo(URL);
        WebAssert.assertTextPresent(htmlPage, "JenkinsLint");
        assertFalse(htmlPage.getWebResponse().getContentAsString().contains(EMPTY_TABLE));
        assertTrue(htmlPage.getWebResponse().getContentAsString().contains(j.getURL().toString() + project.getUrl()));
        WebAssert.assertTextPresent(htmlPage, "JL-1");
        Page page = j.createWebClient().goTo("jenkinslint/api/xml", "application/xml");
        assertThat(page.getWebResponse().getContentAsString(), containsString(jobName));
    }
    @Test
    public void testAPI() throws Exception {
        Page page = j.createWebClient().goTo("jenkinslint/api/xml", "application/xml");
        assertThat(page.getWebResponse().getContentAsString(), containsString("jenkinsLintAction"));

        XmlPage p = j.createWebClient().goToXml("jenkinslint/api/xml");
        for (DomNode element: p.getFirstChild().getChildNodes()){
            assertNotEquals(0, element.getChildNodes());
        }
        assertEquals("checkSet", p.getFirstChild().getChildNodes().get(0).getNodeName());
        assertEquals("jobSet", p.getFirstChild().getChildNodes().get(1).getNodeName());
        assertEquals("slaveCheckSet", p.getFirstChild().getChildNodes().get(2).getNodeName());
        assertEquals("slaveSet", p.getFirstChild().getChildNodes().get(3).getNodeName());
    }
    @Test
    public void testUITable() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);
        HtmlPage page = j.createWebClient().goTo(URL);
        assertFalse(page.getWebResponse().getContentAsString().contains(EMPTY_TABLE));
        String content = page.getWebResponse().getContentAsString();
        assertTrue(content.contains(j.getURL().toString() + project.getUrl()));
        assertTrue(content.contains(htmlLint("JobNameChecker", "JL-1")));
        assertTrue(content.contains(htmlLint("JobDescriptionChecker", "JL-2")));
        assertTrue(content.contains(htmlLint("JobAssignedLabelChecker", "JL-3")));
        assertTrue(content.contains(htmlLint("MasterLabelChecker", "JL-4")));
        assertTrue(content.contains(htmlLint("JobLogRotatorChecker", "JL-5")));
        assertTrue(content.contains(htmlLint("MavenJobTypeChecker", "JL-6")));
        assertTrue(content.contains(htmlLint("CleanupWorkspaceChecker", "JL-7")));
        assertTrue(content.contains(htmlLint("JavadocChecker", "JL-8")));
        assertTrue(content.contains(htmlLint("ArtifactChecker", "JL-9")));
        assertTrue(content.contains(htmlLint("NullSCMChecker", "JL-10")));
        assertTrue(content.contains(htmlLint("PollingSCMTriggerChecker", "JL-11")));
        assertTrue(content.contains(htmlLint("GitShallowChecker", "JL-12")));
        assertTrue(content.contains(htmlLint("MultibranchJobTypeChecker", "JL-13")));
        assertTrue(content.contains(htmlLint("HardcodedScriptChecker", "JL-14")));
        assertTrue(content.contains(htmlLint("GradleWrapperChecker", "JL-15")));
        assertTrue(content.contains(htmlLint("TimeoutChecker", "JL-16")));
        assertTrue(content.contains(htmlLint("GroovySystemExitChecker", "JL-17")));
        assertTrue(content.contains(htmlLint("GitRefChecker", "JL-18")));
        assertTrue(content.contains(htmlLint("TimerTriggerChecker", "JL-19")));
        assertTrue(content.contains(htmlLint("GitRefSubmoduleChecker", "JL-20")));
        assertTrue(content.contains(htmlLint("BFAChecker", "JL-21")));
        assertTrue(content.contains(htmlLint("GroovySandboxChecker", "JL-22")));
    }

    @Issue("JENKINS-46176")
    @Test
    public void testMatrixJobWithConfigurations() throws Exception {
        String jobName = "JOB";
        MatrixProject project = j.createMatrixProject(jobName);
        List<Axis> axes = new ArrayList<Axis>();
        for (int i = 1; i <= 2; i++) {
            List<String> vals = new ArrayList<String>();
            for (int j = 1; j <= i; j++) {
                vals.add(Integer.toString(j));
            }
            axes.add(new TextAxis("axis" + i, vals));
        }
        project.setAxes(new AxisList(axes));
        HtmlPage page = j.createWebClient().goTo(URL);
        String content = page.getWebResponse().getContentAsString();
        assertFalse(content.contains("axis"));
    }

    @Test
    public void testDisabledJobs() throws Exception {
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setLintDisabledJobEnabled(false);
        config.save();
        FreeStyleProject project = j.createFreeStyleProject();
        project.disable();
        for (Action action: j.getInstance().getActions()) {
            if (action instanceof JenkinsLintAction) {
                ((JenkinsLintAction) action).getData();
                assertEquals(((JenkinsLintAction) action).getJobSet().size(), 0);
                j.createFreeStyleProject();
                j.createFreeStyleProject();
                ((JenkinsLintAction) action).getData();
                assertEquals(((JenkinsLintAction) action).getJobSet().size(), 2);
                j.createFreeStyleProject().disable();
                ((JenkinsLintAction) action).getData();
                assertEquals(((JenkinsLintAction) action).getJobSet().size(), 2);
                config.setLintDisabledJobEnabled(true);
                config.save();
                ((JenkinsLintAction) action).getData();
                assertEquals(((JenkinsLintAction) action).getJobSet().size(), 4);
            }
        }
    }

    private String htmlLint (String name, String id) {
        return "<th tooltip=\"" +  name + "\" class=\"pane-header\">" + id + "</th>";
    }
}
