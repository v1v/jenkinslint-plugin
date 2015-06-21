package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobDescriptionChecker extends AbstractCheck{

    public JobDescriptionChecker(final String name, final boolean found, final boolean ignored) {
        super(name, found, ignored);
    }

    public boolean executeCheck(Item item) {
        if (item instanceof hudson.model.Project) {
            return (((Project) item).getDescription() == null
                    ||  ((Project) item).getDescription().length() == 0);
        }
        return false;
    }
}
