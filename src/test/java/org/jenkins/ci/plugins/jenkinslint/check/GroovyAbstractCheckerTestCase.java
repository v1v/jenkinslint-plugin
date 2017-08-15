package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.ParametersDefinitionProperty;
import org.biouno.unochoice.CascadeChoiceParameter;
import org.biouno.unochoice.ChoiceParameter;
import org.biouno.unochoice.DynamicReferenceParameter;
import org.biouno.unochoice.model.GroovyScript;
import org.jenkins.ci.plugins.jenkinslint.AbstractTestCase;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;

/**
 * Created by vicmar02 on 07/08/2017.
 */
public class GroovyAbstractCheckerTestCase extends AbstractTestCase {

    protected ParametersDefinitionProperty createChoiceParameter(String content) {
      GroovyScript script = new GroovyScript(createScript(content),createScript(content));
      ChoiceParameter cp = new ChoiceParameter("param", "desc", script, "", false);
      return new ParametersDefinitionProperty(cp);
    }

    protected ParametersDefinitionProperty createCascadeChoiceParameter(String content) {
      GroovyScript script = new GroovyScript(createScript(content),createScript(content));
      CascadeChoiceParameter ccp = new CascadeChoiceParameter("param", "desc", script, "", "", false);
      return new ParametersDefinitionProperty(ccp);
    }

    protected ParametersDefinitionProperty createDynamicReferenceParameter(String content) {
      GroovyScript script = new GroovyScript(createScript(content),createScript(content));
      DynamicReferenceParameter drp = new DynamicReferenceParameter("param", "desc", script, "", "", false);
      return new ParametersDefinitionProperty(drp);
    }

    private SecureGroovyScript createScript (String content) {
      return new SecureGroovyScript(content,false,null);
    }

    protected ParametersDefinitionProperty createChoiceParameter(String content, Boolean sandbox) {
        GroovyScript script = new GroovyScript(createScript(content, sandbox),createScript(content, sandbox));
        ChoiceParameter cp = new ChoiceParameter("param", "desc", script, "", false);
        return new ParametersDefinitionProperty(cp);
    }

    protected ParametersDefinitionProperty createCascadeChoiceParameter(String content, Boolean sandbox) {
        GroovyScript script = new GroovyScript(createScript(content, sandbox),createScript(content, sandbox));
        CascadeChoiceParameter ccp = new CascadeChoiceParameter("param", "desc", script, "", "", false);
        return new ParametersDefinitionProperty(ccp);
    }

    protected ParametersDefinitionProperty createDynamicReferenceParameter(String content, Boolean sandbox) {
        GroovyScript script = new GroovyScript(createScript(content, sandbox),createScript(content, sandbox));
        DynamicReferenceParameter drp = new DynamicReferenceParameter("param", "desc", script, "", "", false);
        return new ParametersDefinitionProperty(drp);
    }

    private SecureGroovyScript createScript (String content, Boolean sandbox) {
        return new SecureGroovyScript(content,sandbox,null);
    }
}
