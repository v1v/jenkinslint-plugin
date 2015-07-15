package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.*;
import org.jvnet.hudson.test.JenkinsRule;

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
    }
    //Issue("JENKINS-29418")
    @Test
    public void testJob() throws Exception {
        String jobName = "JOB";
        FreeStyleProject project = j.createFreeStyleProject(jobName);
        HtmlPage page = j.createWebClient().goTo(URL);
        //System.out.println(page.getWebResponse().getContentAsString());
        //System.out.println("URL " + j.getURL().toString() + project.getUrl());
        WebAssert.assertTextPresent(page, "JenkinsLint");
        assertFalse(page.getWebResponse().getContentAsString().contains(EMPTY_TABLE));
        assertTrue(page.getWebResponse().getContentAsString().contains(jobTable(j.getURL().toString() + project.getUrl(), jobName)));
    }

    private String jobTable (String url, String jobName){
        return "<tbody id=\"jenkinsLintTableBody\"><tr><td><a href=\""+ url +"\" target=\"_blank\">"+jobName+"</a></td>";
    }
}
