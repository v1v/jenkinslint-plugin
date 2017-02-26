package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.tasks.JavadocArchiver;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

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
        if (item instanceof AbstractProject) {
            DescribableList<Publisher, Descriptor<Publisher>> publishersList = ((AbstractProject) item).getPublishersList();
            for (Publisher publisher : publishersList) {
                if (publisher instanceof hudson.tasks.JavadocArchiver) {
                    return ( ((JavadocArchiver) publisher).getJavadocDir() == null ||
                             ( ((JavadocArchiver) publisher).getJavadocDir() != null &&
                               ((JavadocArchiver) publisher).getJavadocDir().length() == 0 ));
                }
            }
        }
        return false;
    }
}
