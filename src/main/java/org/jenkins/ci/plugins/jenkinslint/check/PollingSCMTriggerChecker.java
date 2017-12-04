package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.triggers.SCMTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.Map;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class PollingSCMTriggerChecker extends AbstractCheck {

    public PollingSCMTriggerChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.PollingSCMTriggerCheckerDesc());
        this.setSeverity(Messages.PollingSCMTriggerCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof AbstractProject) {
            found = (((AbstractProject) item).getTrigger(SCMTrigger.class) != null);
        }
        if (item.getClass().getSimpleName().equals("WorkflowJob")) {
            try {
                Object getTriggers = item.getClass().getMethod("getTriggers", null).invoke(item);
                if (getTriggers instanceof Map) {
                    for (Object value : ((Map) getTriggers).values()) {
                        if (value instanceof SCMTrigger) {
                            found = true;
                        }
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.FINE, "Exception " + e.getMessage(), e.getCause());
            }
        }
        return found;
    }
}
