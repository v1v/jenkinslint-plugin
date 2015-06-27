package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import hudson.model.Project;
import hudson.triggers.SCMTrigger;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class PollingSCMTriggerChecker extends AbstractCheck {

    public PollingSCMTriggerChecker(final boolean found, final boolean ignored) {
        super(found, ignored);
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
