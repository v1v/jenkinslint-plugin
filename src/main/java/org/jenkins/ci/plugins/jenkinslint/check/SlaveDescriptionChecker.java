package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractSlaveCheck;

/**
 * @author Victor Martinez
 */
public class SlaveDescriptionChecker extends AbstractSlaveCheck {

    public SlaveDescriptionChecker() {
        super();
        this.setDescription("Jenkins slave description might help you to know what it does and further details.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Node item) {
       return item.getNodeDescription() == null || item.getNodeDescription().length() == 0;
    }
}
