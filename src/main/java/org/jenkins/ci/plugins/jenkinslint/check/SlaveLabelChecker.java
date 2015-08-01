package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractSlaveCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class SlaveLabelChecker extends AbstractSlaveCheck {

    public SlaveLabelChecker() {
        super();
        this.setDescription("When setting Jenkins Slaves you should set their labels in order to tie those Jobs.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Node item) {
        LOG.log(Level.INFO, "slave " + item.getDisplayName() + " labels " + item.getLabelString());
        return ( item.getLabelString() == null || item.getLabelString().equals("") );
    }
}
