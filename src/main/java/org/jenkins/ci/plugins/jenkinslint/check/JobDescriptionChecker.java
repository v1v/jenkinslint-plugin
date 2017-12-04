package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractItem;
import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class JobDescriptionChecker extends AbstractCheck{

    public JobDescriptionChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.JobDescriptionCheckerDesc());
        this.setSeverity(Messages.JobDescriptionCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof AbstractItem) {
            return isDescription(((AbstractItem) item).getDescription());
        }
        if (item.getClass().getSimpleName().equals("WorkflowJob")) {
            try {
                Object getDescription = item.getClass().getMethod("getDescription", null).invoke(item);
                if (getDescription instanceof String) {
                    return isDescription((String) getDescription);
                }
            } catch (Exception e) {
                LOG.log(Level.FINE, "Exception " + e.getMessage(), e.getCause());
            }
        }
        return false;
    }

    private boolean isDescription(String description) {
        return (description == null || description.length() == 0);
    }
}
