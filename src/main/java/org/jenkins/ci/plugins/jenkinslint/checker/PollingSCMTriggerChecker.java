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

    public PollingSCMTriggerChecker(final String name, final String description, final String severity, final boolean found, final boolean ignored) {
        super(name, description, severity, found, ignored);
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getTrigger(SCMTrigger.class) != null);
        }
        return false;
    }
}
