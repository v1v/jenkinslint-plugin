package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import hudson.model.Slave;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractSlaveCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class WindowsSlaveLaunchChecker extends AbstractSlaveCheck {

    public WindowsSlaveLaunchChecker() {
        super();
        this.setDescription(Messages.WindowsSlaveLaunchCheckerDesc());
        this.setSeverity(Messages.WindowsSlaveLaunchCheckerSeverity());
    }

    public boolean executeCheck(Node item) {
        try {
            LOG.log(Level.FINER, "slave " + item.getDisplayName() + " service " +  ( (Slave) item).getComputer().getLauncher().toString());
            return ((Slave) item).getComputer().getLauncher().toString().contains("ManagedWindowsServiceLauncher");
        } catch (NullPointerException npe) {
            return false;
        } catch (java.lang.ClassCastException cce) {
            return false;
        }
    }
}
