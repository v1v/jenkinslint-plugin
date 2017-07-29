package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Job;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class BFAChecker extends AbstractCheck {

    public BFAChecker() {
        super();
        this.setDescription(Messages.BFACheckerDesc());
        this.setSeverity(Messages.BFACheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);
        boolean found = false;
        if (Jenkins.getInstance().pluginManager.getPlugin("build-failure-analyzer") != null) {
            found = isDoNotScan(((Job) item).getProperty("com.sonyericsson.jenkins.plugins.bfa.model.ScannerJobProperty"));
        }
        return found;
    }

    private boolean isDoNotScan (Object property) {
        boolean status = false;
        if (property != null) {
            try {
                Object isDoNotScan = property.getClass().getMethod("isDoNotScan", null).invoke(property);
                if (isDoNotScan instanceof Boolean) {
                    status = ((Boolean) isDoNotScan).booleanValue();
                    LOG.log(Level.FINE, "isDoNotScan " + status);
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                status = false;
            }
        } else {
            status = true;
        }
        return status;
    }

}
