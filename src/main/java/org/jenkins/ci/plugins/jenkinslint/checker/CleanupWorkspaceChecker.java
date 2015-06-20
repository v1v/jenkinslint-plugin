package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class CleanupWorkspaceChecker extends AbstractCheck{

    public CleanupWorkspaceChecker(final String name, final boolean found, final boolean ignored) {
        super(name, found, ignored);
    }

    public boolean executeCheck(Item item) {
        return item instanceof Project && ((Project) item).getPublisher(Descriptor.find(hudson.plugins.ws_cleanup.WsCleanup.DescriptorImpl.class.getName())) == null;
    }
}
