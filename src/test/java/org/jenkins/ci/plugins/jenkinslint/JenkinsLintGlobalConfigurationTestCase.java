package org.jenkins.ci.plugins.jenkinslint;

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.*;

/**
 * @author victor.martinez.
 */
public class JenkinsLintGlobalConfigurationTestCase extends AbstractTestCase{

    @Test
    public void testDefaultonfigured() throws Exception {
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        assertTrue(config.isGlobalEnabled());
    }

    @Test
    public void testDoCheckHardcodedScriptThreshold() throws Exception {
        JenkinsLintGlobalConfiguration config = GlobalConfiguration.all().get(JenkinsLintGlobalConfiguration.class);
        FormValidation form = config.doCheckHardcodedScriptThreshold(0);
        assertEquals(FormValidation.Kind.ERROR, form.kind);
        form = config.doCheckHardcodedScriptThreshold(2);
        assertEquals(FormValidation.Kind.OK, form.kind);
    }
}
