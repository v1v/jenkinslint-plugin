package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.HealthReport;
import org.jenkins.ci.plugins.jenkinslint.Messages;

import java.util.ArrayList;

/**
 * Slave class.
 * @author Victor Martinez
 */
public final class Slave implements Comparable<Slave> {
    private String name;
    private String url;
    private final ArrayList<Lint> lintList = new ArrayList<Lint>();

    public Slave(final String name, final String url) {
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

    public ArrayList<Lint> getLintList() {
        return lintList;
    }

    public void addLint(Lint lint) {
        lintList.add(lint);
    }

    public int compareTo(final Slave other) {
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

        if (!(obj instanceof Slave)) {
            return false;
        }

        return name.equals(((Slave) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Slave: ").append(name).
                    append(", ").append(url).
                    append(", ").append(lintList).toString();
    }

    public HealthReport getLintHealthReport() {
        if (lintList != null && lintList.size() > 0) {
            int ok = 0;
            for (Lint lint : lintList) {
                if (!lint.isIgnored()) {
                    if (! lint.isFound()) { ok++; }
                } else {
                    ok++;
                }
            }
            int score = (int) (100.0 * ok / lintList.size());
            return new HealthReport(score, Messages._Job_LintStability(score + "%"));
        }
        return null;
    }
}
