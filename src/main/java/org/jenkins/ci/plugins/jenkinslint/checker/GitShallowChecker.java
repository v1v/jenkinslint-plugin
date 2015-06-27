package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import hudson.model.Project;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.extensions.GitSCMExtension;
import hudson.plugins.git.extensions.GitSCMExtensionDescriptor;
import hudson.plugins.git.extensions.impl.CloneOption;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class GitShallowChecker extends AbstractCheck {

    public GitShallowChecker(final boolean found, final boolean ignored) {
        super(found, ignored);
        this.setDescription("In order to comply with the style guide, Jenkins projects must avoid whitespace.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            if (((Project) item).getScm() instanceof hudson.plugins.git.GitSCM) {
                DescribableList<GitSCMExtension, GitSCMExtensionDescriptor> extensionsList = ((GitSCM) ((Project) item).getScm()).getExtensions();
                boolean status = true;
                for (GitSCMExtension extension : extensionsList) {
                    if (extension instanceof hudson.plugins.git.extensions.impl.CloneOption && ((CloneOption) extension).isShallow()) {
                        status = false;
                    }
                }
                return status;
            }
        }
        return false;
    }
}
