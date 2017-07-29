package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.triggers.SCMTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class PollingSCMTriggerChecker extends AbstractCheck {

    public PollingSCMTriggerChecker() {
        super();
        this.setDescription(Messages.PollingSCMTriggerCheckerDesc());
        this.setSeverity(Messages.PollingSCMTriggerCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof AbstractProject) {
            return (((AbstractProject) item).getTrigger(SCMTrigger.class) != null);
        }
        return false;
    }
}
