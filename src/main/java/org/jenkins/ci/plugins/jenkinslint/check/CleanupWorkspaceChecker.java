package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class CleanupWorkspaceChecker extends AbstractCheck{

    public CleanupWorkspaceChecker() {
        super();
        this.setDescription(Messages.CleanupCheckerDesc());
        this.setSeverity(Messages.CleanupCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (Jenkins.getInstance().pluginManager.getPlugin("ws-cleanup")!=null) {
            return item instanceof Project && ((Project) item).getPublisher(Descriptor.find("hudson.plugins.ws_cleanup.WsCleanup")) == null;
        } else {
            return true;
        }
    }
}
