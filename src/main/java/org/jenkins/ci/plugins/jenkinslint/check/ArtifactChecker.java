package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.AbstractProject;
import hudson.tasks.ArtifactArchiver;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class ArtifactChecker extends AbstractCheck {

    public ArtifactChecker() {
        super();
        this.setDescription(Messages.ArtifactCheckerDesc());
        this.setSeverity(Messages.ArtifactCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);

        if (item instanceof AbstractProject) {
            DescribableList<Publisher, Descriptor<Publisher>> publishersList = ((AbstractProject)item).getPublishersList();
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
