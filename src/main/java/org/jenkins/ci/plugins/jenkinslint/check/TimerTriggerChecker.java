package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import hudson.triggers.TimerTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import org.jenkins.ci.plugins.jenkinslint.utils.StringUtils;

import java.util.Map;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class TimerTriggerChecker extends AbstractCheck{

    public TimerTriggerChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.TimerTriggerCheckerDesc());
        this.setSeverity(Messages.TimerTriggerCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof AbstractProject && ((AbstractProject) item).getTrigger(TimerTrigger.class) != null ) {
            found = isNotHash(((AbstractProject) item).getTrigger(TimerTrigger.class).getSpec().toLowerCase());
        }
        if (item.getClass().getSimpleName().equals("WorkflowJob")) {
            try {
                Object getTriggers = item.getClass().getMethod("getTriggers", null).invoke(item);
                if (getTriggers instanceof Map) {
                    for (Object value : ((Map) getTriggers).values()) {
                        if (value instanceof TimerTrigger && isNotHash(((TimerTrigger) value).getSpec())) {
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

    private boolean isNotHash (String spec) {
        boolean found = false;
        String[] myData = spec.split("\n");
        for (String line: myData) {
            if (!StringUtils.isH(line) && !StringUtils.isShellComment(line) &&
                    !StringUtils.isAt(line) && !StringUtils.isEmptyOrBlank(line)) {
                found = true;
            }
        }
        return found;
    }
}
