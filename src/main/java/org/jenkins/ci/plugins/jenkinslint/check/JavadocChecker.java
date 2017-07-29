package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class JavadocChecker extends AbstractCheck{

    public JavadocChecker() {
        super();
        this.setDescription(Messages.JavadocCheckerDesc());
        this.setSeverity(Messages.JavadocCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof AbstractProject) {
            DescribableList<Publisher, Descriptor<Publisher>> publishersList = ((AbstractProject) item).getPublishersList();
            for (Publisher publisher : publishersList) {
                if (publisher.getClass().getSimpleName().equals("JavadocArchiver")) {
                    try {
                        Object getJavadocDir = publisher.getClass().getMethod("getJavadocDir", null).invoke(publisher);
                        if (getJavadocDir instanceof String) {
                            if (getJavadocDir == null) {
                                found = true;
                            } else {
                                found = ((String) getJavadocDir).isEmpty();
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    }
                }
            }
        }
        return found;
    }
}
