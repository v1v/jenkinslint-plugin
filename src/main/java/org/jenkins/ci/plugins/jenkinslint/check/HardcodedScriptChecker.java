package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.CommandInterpreter;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class HardcodedScriptChecker extends AbstractCheck {

    public final static int THRESHOLD = 2;

    public HardcodedScriptChecker() {
        super();
        this.setDescription("When setting Jenkins Jobs with Shell/Batch builds you shouldn't hardcoded the script "+
                            "it's recommended to track them in your SCM tool instead.<br/>" +
                            "Otherwise you won't be able to reproduce your CI environment easily.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);
        boolean found = false;
        if (item instanceof hudson.maven.MavenModuleSet) {
            found = false;
        } else {
            Project project = (Project) item;
            if (project.getBuilders() != null && project.getBuilders().size() > 0 ) {
                for (CommandInterpreter builder : (List<CommandInterpreter>) project.getBuilders()) {
                    if (builder instanceof hudson.tasks.Shell || builder instanceof hudson.tasks.BatchFile) {
                        if (isHarcoded (builder.getCommand(), THRESHOLD)) {
                            found = true;
                        }
                    }
                }
            } else {
                found = false;
            }
        }
        return found;
    }

    private boolean isHarcoded (String content, int threshold) {
        if (content != null) {
            return content.split("\r\n|\r|\n").length > threshold;
        } else {
            return false;
        }
    }
}
