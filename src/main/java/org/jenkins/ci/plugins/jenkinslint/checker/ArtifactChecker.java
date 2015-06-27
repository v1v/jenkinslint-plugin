package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.ArtifactArchiver;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class ArtifactChecker extends AbstractCheck {

    public ArtifactChecker(final String name, final String description, final String severity, final boolean found, final boolean ignored) {
        super(name, description, severity, found, ignored);
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);

        if (item instanceof Project) {
            Project project = (Project) item;
            DescribableList<Publisher, Descriptor<Publisher>> publishersList = project.getPublishersList();
            for (Publisher publisher : publishersList) {
                if (publisher instanceof ArtifactArchiver) {
                    LOG.log(Level.FINEST, "ArtifactChecker " + publisher);
                    return (((ArtifactArchiver) publisher).getArtifacts() == null ||
                            (((ArtifactArchiver) publisher).getArtifacts() != null &&
                                    ((ArtifactArchiver) publisher).getArtifacts().length() == 0));
                }
            }
        }
        return false;
    }
}
