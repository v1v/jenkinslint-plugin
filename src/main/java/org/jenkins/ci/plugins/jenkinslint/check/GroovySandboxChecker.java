package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class GroovySandboxChecker extends AbstractCheck {

    public GroovySandboxChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.GroovySandboxCheckerDesc());
        this.setSeverity(Messages.GroovySandboxCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);
        if (item instanceof Job) {
            // Pipeline support
            if (item.getClass().getSimpleName().equals("WorkflowJob")) {
                try {
                    Object getDefinition = item.getClass().getMethod("getDefinition", null).invoke(item);
                    if (getDefinition.getClass().getSimpleName().equals("CpsFlowDefinition")) {
                        return !isSandbox(getDefinition);
                    }
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Exception " + e.getMessage(), e.getCause());
                }
            }
        }
        return false;
    }

    private boolean isSandbox(Object object) {
        boolean status = true;
        if (object != null) {
            try {
                Object isSandbox = object.getClass().getMethod("isSandbox", null).invoke(object);
                return ((Boolean) isSandbox);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
            }
        }
        return status;
    }
}
