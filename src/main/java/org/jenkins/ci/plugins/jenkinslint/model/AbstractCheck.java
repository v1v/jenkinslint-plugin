package org.jenkins.ci.plugins.jenkinslint.model;

import java.util.logging.Logger;

/**
 * AbstractCheck class.
 * @author Victor Martinez
 */
public abstract class AbstractCheck implements Comparable<AbstractCheck>, InterfaceCheck {
    private String name;
    private String description;
    private String id = "JL-";
    private String severity;
    protected static final Logger LOG = Logger.getLogger(AbstractCheck.class.getName());

    public AbstractCheck() {
        super();
        this.setName(this.getClass().getSimpleName());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

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
