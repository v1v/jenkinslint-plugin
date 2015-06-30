package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobDescriptionChecker extends AbstractCheck{

    public JobDescriptionChecker() {
        super();
        this.setDescription("In order to comply with the style guide, Jenkins project description might help you to know what it does and further details..");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof hudson.model.Project) {
            return (((Project) item).getDescription() == null
                    ||  ((Project) item).getDescription().length() == 0);
        }
        return false;
    }
}
