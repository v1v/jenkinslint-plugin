package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobNameChecker extends AbstractCheck{

    public JobNameChecker() {
        super();
        this.setDescription(Messages.JobNameCheckerDesc());
        this.setSeverity(Messages.JobNameCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        return item.getName().contains(" ");
    }
}
