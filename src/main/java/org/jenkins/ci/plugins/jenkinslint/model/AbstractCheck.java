package org.jenkins.ci.plugins.jenkinslint.model;

import java.util.logging.Logger;

/**
 * AbstractCheck class.
 * @author Victor Martinez
 */
public abstract class AbstractCheck implements Comparable<AbstractCheck>, CheckInterface {
    private String name;
    private String description;
    private boolean found = false;
    private boolean ignored = false;
    private String id = "JL-";
    private String severity;
    protected static final Logger LOG = Logger.getLogger(AbstractCheck.class.getName());

    public AbstractCheck(final boolean found, final boolean ignored) {
        super();
        this.setName(this.getClass().getSimpleName());
        this.setFound(found);
        this.setIgnored(ignored);
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

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
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
        return "Check: " + getName() + ", " + isFound() + ", " + isIgnored();
    }
}
