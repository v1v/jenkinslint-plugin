package org.jenkins.ci.plugins.jenkinslint.model;

import java.util.logging.Logger;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Exported;

/**
 * AbstractCheck class.
 * @author Victor Martinez
 */
 @ExportedBean
public abstract class AbstractCheck implements Comparable<AbstractCheck>, InterfaceCheck {
    private String name;
    private String description;
    private String id = "JL-";
    private String severity;
    private boolean enabled = true;
    protected static final Logger LOG = Logger.getLogger(AbstractCheck.class.getName());

    public AbstractCheck(boolean enabled) {
        super();
        this.setName(this.getClass().getSimpleName());
        this.setEnabled(enabled);
    }

    public AbstractCheck() {
        super();
        this.setName(this.getClass().getSimpleName());
    }

    @Exported
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Exported
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Exported
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int compareTo(final AbstractCheck other) {
        if (this == other) {
            return 0;
        }
        return getName().compareTo(other.getName());
    }

    public boolean isIgnored(String jobDescription) {
        return jobDescription != null && jobDescription.contains("lint:ignore:" + this.getName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AbstractCheck)) {
            return false;
        }

        return getName().equals(((AbstractCheck) obj).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Check: " + getName() + ", " + getDescription() ;
    }
}
