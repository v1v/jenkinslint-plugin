package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractSlaveCheck;

/**
 * @author Victor Martinez
 */
public class SlaveDescriptionChecker extends AbstractSlaveCheck {

    public SlaveDescriptionChecker() {
        super();
        this.setDescription(Messages.SlaveDescriptionCheckerDesc());
        this.setSeverity(Messages.SlaveDescriptionCheckerSeverity());
    }

    public boolean executeCheck(Node item) {
       return item.getNodeDescription() == null || item.getNodeDescription().length() == 0;
    }
}
