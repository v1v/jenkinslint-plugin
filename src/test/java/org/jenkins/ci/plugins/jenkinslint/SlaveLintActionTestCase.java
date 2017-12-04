package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.model.FreeStyleProject;
import hudson.model.Node;
import hudson.model.Slave;
import hudson.slaves.DumbSlave;
import hudson.slaves.JNLPLauncher;
import hudson.slaves.NodeProperty;
import hudson.slaves.RetentionStrategy;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * @author victor.martinez.
 */
public class SlaveLintActionTestCase extends AbstractTestCase {

    @Test
    public void testSlave() throws Exception {
        Slave slave = j.createSlave();
        slave.getComputer().getAction(SlaveLintAction.class).getComputer().getName().equals(slave.getNodeName());
        HtmlPage htmlPage = j.createWebClient().goTo("computer/" + slave.getNodeName());
        // TODO: floatingBox is not supported yet in SlaveActions https://issues.jenkins-ci.org/browse/JENKINS-24783
        // WebAssert.assertTextPresent(htmlPage, "stability");
        WebAssert.assertTextPresent(htmlPage, "Jenkins Lint");
    }
    @Test
    public void testAPI() throws Exception {
        Slave slave = j.createSlave();
        Page page = j.createWebClient().goTo("computer/" + slave.getNodeName() + "/jenkinslint/api/xml", "application/xml");
        assertThat(page.getWebResponse().getContentAsString(), containsString("slaveLintAction"));

        XmlPage p = j.createWebClient().goToXml("computer/" + slave.getNodeName() + "/jenkinslint/api/xml?depth=2");
        for (DomNode element: p.getFirstChild().getChildNodes()){
            assertNotEquals(0, element.getChildNodes());
        }
        assertEquals("slave", p.getFirstChild().getChildNodes().get(0).getNodeName());
        assertEquals("lintHealthReport", p.getFirstChild().getChildNodes().get(0).getChildNodes().get(0).getNodeName());
        assertEquals("lintSet", p.getFirstChild().getChildNodes().get(0).getChildNodes().get(1).getNodeName());
    }
}
