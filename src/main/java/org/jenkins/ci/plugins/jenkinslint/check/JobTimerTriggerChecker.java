package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import hudson.triggers.TimerTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobTimerTriggerChecker extends AbstractCheck{

    public JobTimerTriggerChecker() {
        super();
        this.setDescription("When setting Jenkins Jobs with some Timer trigger configuration use the symbol H (for “hash”) wherever possible \n" +
                            "to allow periodically scheduled tasks to produce even load on the system.");
        this.setSeverity("Low");
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof Project && ((Project) item).getTrigger(TimerTrigger.class) != null ) {
            String spec = ((Project) item).getTrigger(TimerTrigger.class).getSpec().toLowerCase();
            if (spec.contains("h")) {
                String[] myData = spec.split("/n");
                for (String line: myData) {
                    if (line.contains("#h") && !found) {
                        found = true;
                    }
                }
            } else {
                found = true;
            }
        } else {
            found = false;
        }
        return found;
    }
}
