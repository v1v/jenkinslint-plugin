package org.jenkins.ci.plugins.jenkinslint.model;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Job class.
 * @author Victor Martinez
 */
public final class Job implements Comparable<Job> {
    private String name;
    private String url;
    private final SortedSet<Lint> lintSet = new TreeSet<Lint>();

    public Job(final String name, final String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public SortedSet<Lint> getLintSet() {
        return lintSet;
    }

    public void addCheck(Lint lint) {
        lintSet.add(lint);
    }

    public int compareTo(final Job other) {
        if (this == other) {
            return 0;
        }
        return name.compareTo(other.getName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Job)) {
            return false;
        }

        return name.equals(((Job) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Job: ").append(name).
                    append(", ").append(url).
                    append(", ").append(lintSet).toString();
    }
}