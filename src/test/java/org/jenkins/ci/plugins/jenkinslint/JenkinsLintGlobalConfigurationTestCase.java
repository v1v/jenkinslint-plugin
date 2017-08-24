package org.jenkins.ci.plugins.jenkinslint;

import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
