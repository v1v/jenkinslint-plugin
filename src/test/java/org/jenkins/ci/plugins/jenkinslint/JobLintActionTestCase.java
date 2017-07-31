package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
}
