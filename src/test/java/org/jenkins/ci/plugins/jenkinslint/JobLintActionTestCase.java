package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.model.FreeStyleProject;
import jenkins.model.GlobalConfiguration;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author victor.martinez.
 */
public class JobLintActionTestCase extends AbstractTestCase {

    @Test
    public void testJob() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);
        project.getAction(JobLintAction.class).getProject().equals(jobName);

        HtmlPage htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextPresent(htmlPage, "stability");
        WebAssert.assertTextPresent(htmlPage, "Jenkins Lint");
    }

    @Test
    public void testAPI() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);

        // Default setup
        Page page = j.createWebClient().goTo("job/" + jobName + "/jenkinslint/api/xml", "application/xml");
        assertThat(page.getWebResponse().getContentAsString(), containsString("jobLintAction"));

        XmlPage p = j.createWebClient().goToXml("job/" + jobName + "/jenkinslint/api/xml?depth=2");
        for (DomNode element: p.getFirstChild().getChildNodes()){
            assertNotEquals(0, element.getChildNodes());
        }
        assertEquals("job", p.getFirstChild().getChildNodes().get(0).getNodeName());
        assertEquals("lintHealthReport", p.getFirstChild().getChildNodes().get(0).getChildNodes().get(0).getNodeName());
        assertEquals("lintSet", p.getFirstChild().getChildNodes().get(0).getChildNodes().get(1).getNodeName());
    }

    @Test
    public void testAPIWithLintDisabledJobEnabledProperty() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);

        // Disable lint analysis of disabled project
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setLintDisabledJobEnabled(false);
        config.save();
        project.disable();

        XmlPage p = j.createWebClient().goToXml("job/" + jobName + "/jenkinslint/api/xml?depth=2");
        assertEquals(0, p.getFirstChild().getChildNodes().size());

        // Enable lint analysis of disabled project
        config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setLintDisabledJobEnabled(true);
        config.save();
        project.enable();

        p = j.createWebClient().goToXml("job/" + jobName + "/jenkinslint/api/xml?depth=2");
        assertNotEquals(0, p.getFirstChild().getChildNodes().size());
    }

    @Test
    public void testDisabledJob() throws Exception {
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setLintDisabledJobEnabled(false);
        config.save();

        // Disable the job and see whether it is shown
        FreeStyleProject project = j.createFreeStyleProject();
        project.disable();
        JobLintAction action = project.getAction(JobLintAction.class);
        action.getData();
        assertNull(action.getJob());

        // Enable the job back and see whether it is shown
        config.setLintDisabledJobEnabled(true);
        config.save();
        action = project.getAction(JobLintAction.class);
        action.getData();
        assertNotEquals(action.getCheckList().size(), 0);
        assertNotNull(action.getJob());
    }

    @Test
    public void testDisabledFloatingBox() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);

        // Default setup
        HtmlPage htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextPresent(htmlPage, "stability");

        // Disable floating box
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setJobActionEnabled(false);
        config.save();
        htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextNotPresent(htmlPage, "stability");

        // Disable project
        project.disable();
        htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextNotPresent(htmlPage, "stability");

        // Enable floating box
        config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setJobActionEnabled(true);
        config.save();
        htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextPresent(htmlPage, "stability");

        // Disable lint analysis of disabled project
        config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        config.setLintDisabledJobEnabled(false);
        config.save();
        htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextNotPresent(htmlPage, "stability");

        // Enable project
        project.enable();
        htmlPage = j.createWebClient().goTo("job/" + jobName);
        WebAssert.assertTextPresent(htmlPage, "stability");
    }
}
