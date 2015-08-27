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
        this.setDescription("This launch method relies on DCOM and is often associated with <a href=https://wiki.jenkins-ci.org/display/JENKINS/Windows+slaves+fail+to+start+via+DCOM>subtle problems</a>. " +
                            "Consider using Launch slave agents using Java Web Start instead,<br/>which also permits " +
                            "installation as a Windows service but is generally considered more reliable.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Node item) {
        try {
            LOG.log(Level.INFO, "slave " + item.getDisplayName() + " service " +  ( (Slave) item).getComputer().getLauncher().toString());
            return ((Slave) item).getComputer().getLauncher().toString().contains("ManagedWindowsServiceLauncher");
        } catch (NullPointerException npe) {
            return false;
        }
    }
}
