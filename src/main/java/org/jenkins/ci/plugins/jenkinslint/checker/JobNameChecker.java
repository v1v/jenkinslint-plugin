package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobNameChecker extends AbstractCheck{

    public JobNameChecker(final String name, final String description, final String severity, final boolean found, final boolean ignored) {
        super(name, description, severity, found, ignored);
    }

    public boolean executeCheck(Item item) {
        return item.getName().contains(" ");
    }
}
