package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.PluginWrapper;
import hudson.model.*;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.List;
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
        boolean found = false;
        if (item instanceof Job) {
            // Pipeline support
            if (item.getClass().getSimpleName().equals("WorkflowJob")) {
                try {
                    Object getDefinition = item.getClass().getMethod("getDefinition", null).invoke(item);
                    if (getDefinition.getClass().getSimpleName().equals("CpsFlowDefinition")) {
                        found = !isPipelineSandbox(getDefinition);
                    }
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Exception " + e.getMessage(), e.getCause());
                }
            }
        }
        PluginWrapper plugin = Jenkins.getInstance().pluginManager.getPlugin("groovy");
        if (plugin!=null && plugin.getVersionNumber().isNewerThan(new hudson.util.VersionNumber("1.30"))) {
            if (item.getClass().getSimpleName().equals("MavenModuleSet")) {
                try {
                    Object getPrebuilders = item.getClass().getMethod("getPrebuilders", null).invoke(item);
                    if (!isSystemSandbox((List) getPrebuilders)) {
                        found = true;
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                }
            }
            if (item instanceof Project && !isSystemSandbox(((Project) item).getBuilders())) {
                found = true;
            }
            if (item.getClass().getSimpleName().equals("MatrixProject")) {
                try {
                    Object getBuilders = item.getClass().getMethod("getBuilders", null).invoke(item);
                    if (!isSystemSandbox((List) getBuilders)) {
                        found = true;
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                }
            }
        }

        if (item instanceof AbstractProject) {
            if (!isSandboxInPublisher(((AbstractProject) item).getPublishersList())) {
                found = true;
            }

            if (((AbstractProject) item).getProperty(ParametersDefinitionProperty.class) != null) {
                if (!isSandboxParameters(((ParametersDefinitionProperty) ((AbstractProject) item).getProperty(ParametersDefinitionProperty.class)).getParameterDefinitions())) {
                    found = true;
                }
            }
        }

        return found;
    }

    private boolean isPipelineSandbox(Object object) {
        boolean status = true;
        if (object != null) {
            try {
                return isSandbox(object.getClass().getMethod("isSandbox", null).invoke(object));
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
            }
        }
        return status;
    }

    private boolean isSystemSandbox(List<Builder> builders) {
        boolean status = true;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder.getClass().getName().endsWith("SystemGroovy")) {
                    try {
                        Object source = builder.getClass().getMethod("getSource",null).invoke(builder);
                        if (source.getClass().getName().endsWith("StringSystemScriptSource")) {
                            Object scriptSource = source.getClass().getMethod("getScript",null).invoke(source);
                            if (scriptSource.getClass().getName().endsWith("SecureGroovyScript")) {
                                if (!isSandbox(scriptSource.getClass().getMethod("isSandbox",null).invoke(scriptSource))) {
                                    status = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    }
                }
            }
        }
        return status;
    }

    private boolean isSandboxInPublisher(DescribableList<Publisher, Descriptor<Publisher>> publishersList) {
        boolean status = true;
        if (publishersList != null) {
            for (Publisher publisher : publishersList) {
                if (publisher.getClass().getName().endsWith("GroovyPostbuildRecorder")) {
                    LOG.log(Level.FINEST, "GroovyPostbuildRecorder " + publisher);
                    try {
                        Object scriptSource = publisher.getClass().getMethod("getScript", null).invoke(publisher);
                        if (scriptSource.getClass().getName().endsWith("SecureGroovyScript")) {
                            if (!isSandbox(scriptSource.getClass().getMethod("isSandbox", null).invoke(scriptSource))) {
                                status = false;
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    }
                }
            }
        }
        return status;
    }

    private boolean isSandboxParameters(List<ParameterDefinition> properties) {
        boolean status = true;
        if (properties != null) {
            for (ParameterDefinition property : properties) {
                if (property.getClass().getName().endsWith("ChoiceParameter") ||
                        property.getClass().getName().endsWith("CascadeChoiceParameter") ||
                        property.getClass().getName().endsWith("DynamicReferenceParameter")) {
                    LOG.log(Level.FINEST, "unochoice " + property);
                    try {
                        Object scriptSource = property.getClass().getMethod("getScript", null).invoke(property);
                        if (scriptSource.getClass().getName().endsWith("GroovyScript")) {
                            Object script = scriptSource.getClass().getMethod("getScript", null).invoke(scriptSource);
                            if (script != null && script.getClass().getName().endsWith("SecureGroovyScript")) {
                                if (!isSandbox(script.getClass().getMethod("isSandbox", null).invoke(script))) {
                                    status = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    }
                }
            }
        }
        return status;
    }

    private boolean isSandbox(Object command) {
        boolean status = false;
        if (command != null && command instanceof Boolean) {
            status = (Boolean) command;
        }
        return status;
    }
}
