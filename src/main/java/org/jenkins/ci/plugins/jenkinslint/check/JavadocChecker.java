package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
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
        if (item instanceof Project) {
            DescribableList<Publisher, Descriptor<Publisher>> publishersList = ((Project) item).getPublishersList();
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
