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
        this.setDescription("Polling a repository from Jenkins is inefficient; it adds delay on the order of minutes " +
                            "before a build starts after a commit is pushed, and it adds additional loads.<br/>" +
                            "It is much better instead to do push-notification from the repository.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getTrigger(SCMTrigger.class) != null);
        }
        return false;
    }
}
