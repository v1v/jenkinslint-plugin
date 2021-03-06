package org.jenkins.ci.plugins.jenkinslint.model;

/**
 * Check class.
 * @author Victor Martinez
 */
public final class Lint implements Comparable<Lint> {
    private String name;
    private boolean found = false;
    private boolean ignored = false;

    public Lint(final String name, final boolean found, final boolean ignored) {
        super();
        this.setName(name);
        this.setFound(found);
        this.setIgnored(ignored);
    }

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
        return "Lint: " + getName() + ", " + isFound() + ", " + isIgnored();
    }
}
