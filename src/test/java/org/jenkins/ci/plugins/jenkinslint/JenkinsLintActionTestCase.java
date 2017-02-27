package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.*;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * @author victor.martinez.
 */
public class JenkinsLintActionTestCase {

    private static final String EMPTY_TABLE = "<tbody id=\"jenkinsLintTableBody\"></tbody></table>";
    private static final String URL = "jenkinslint";

    @Rule
    public JenkinsRule j = new JenkinsRule();
    @Test
    public void testEmptyJob() throws Exception {
        HtmlPage page = j.createWebClient().goTo(URL);
        WebAssert.assertTextPresent(page, "JenkinsLint");
        assertTrue(page.getWebResponse().getContentAsString().contains(EMPTY_TABLE));
        WebAssert.assertTextPresent(page, "JL-1");
    }
    //Issue("JENKINS-29418")
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
}
