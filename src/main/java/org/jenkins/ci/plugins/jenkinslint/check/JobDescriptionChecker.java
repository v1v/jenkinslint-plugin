package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractItem;
import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

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
            return (((AbstractItem) item).getDescription() == null
                    ||  ((AbstractItem) item).getDescription().length() == 0);
        }
        return false;
    }
}
