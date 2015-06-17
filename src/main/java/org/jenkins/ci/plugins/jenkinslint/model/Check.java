package org.jenkins.ci.plugins.jenkinslint.model;

/**
 * Check class.
 * @author Victor Martinez
 */
public final class Check implements Comparable<Check> {
    private String name;
    private boolean found = false;
    private boolean ignored = false;

    public Check(final String name, final boolean found, final boolean ignored) {
        super();
        this.name = name;
        this.found = found;
        this.ignored = ignored;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int compareTo(final Check other) {
        if (this == other) {
            return 0;
        }
        return name.compareTo(other.getName());
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

        if (!(obj instanceof Check)) {
            return false;
        }

        return name.equals(((Check) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Check: ").append(name).
                append(", ").append(found).
                append(", ").append(ignored).toString();
    }
}
