package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import hudson.triggers.SCMTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class PollingSCMTriggerChecker extends AbstractCheck {

    public PollingSCMTriggerChecker() {
        super();
        this.setDescription("In order to comply with the style guide, Jenkins projects must avoid whitespace.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getTrigger(SCMTrigger.class) != null);
        }
        return false;
    }
}
