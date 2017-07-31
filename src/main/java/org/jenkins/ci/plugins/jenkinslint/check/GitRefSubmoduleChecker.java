package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.PluginWrapper;
import hudson.model.Item;
import hudson.model.Project;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class GitRefSubmoduleChecker extends AbstractCheck {

    public GitRefSubmoduleChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.GitRefSubmoduleCheckerDesc());
        this.setSeverity(Messages.GitRefSubmoduleCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            PluginWrapper plugin = Jenkins.getInstance().pluginManager.getPlugin("git");
            if (plugin!=null && plugin.getVersionNumber().isNewerThan(new hudson.util.VersionNumber("2.4.0"))) {
                if (Jenkins.getInstance().pluginManager.getPlugin("git") != null) {
                    if (((Project) item).getScm().getClass().getName().endsWith("GitSCM")) {
                        boolean status = true;
                        try {
                            Method method = ((Project) item).getScm().getClass().getMethod("getExtensions",null);
                            Object extensionsList = method.invoke(((Project) item).getScm());
                            if (extensionsList instanceof AbstractList) {
                                for (Object extension : ((AbstractList) extensionsList)) {
                                    if (extension.getClass().getName().endsWith("SubmoduleOption")) {
                                        Object reference = extension.getClass().getMethod("getReference",null).invoke(extension);
                                        if (reference instanceof String) {
                                            LOG.log(Level.FINEST, "Reference has been found: " + reference);
                                            status = ((String) reference).isEmpty();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                            status = false;
                        } finally {
                            return status;
                        }
                    }
                } else {
                    LOG.log(Level.FINE, "Plugin GIT hasn't been configured in this project");
                    return false;
                 }
            } else {
                LOG.log(Level.FINE, "Plugin GIT doesn't exist");
                return false;
            }

        }
        return false;
    }
}
