package org.jenkins.ci.plugins.jenkinslint.model;

import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Exported;

/**
 * Check class.
 * @author Victor Martinez
 */
@ExportedBean
public final class Lint implements Comparable<Lint> {
    private String name;
    private boolean found = false;
    private boolean ignored = false;
    private boolean enabled = false;

    public Lint(final String name, final boolean found, final boolean ignored, final boolean enabled) {
        super();
        this.setName(name);
        this.setFound(found);
        this.setIgnored(ignored);
        this.setEnabled(enabled);
    }

    @Exported
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int compareTo(final Lint other) {
        if (this == other) {
            return 0;
        }
        return getName().compareTo(other.getName());
    }

    @Exported
    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    @Exported
    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    @Exported
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isActive() {
        return isEnabled() && !isIgnored();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Lint)) {
            return false;
        }

        return getName().equals(((Lint) obj).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Lint: " + getName() + ", " + isFound() + ", " + isIgnored()+ ", " + isEnabled();
    }
}
