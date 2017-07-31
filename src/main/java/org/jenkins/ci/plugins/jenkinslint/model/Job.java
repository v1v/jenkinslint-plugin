package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.HealthReport;
import org.jenkins.ci.plugins.jenkinslint.Messages;
import java.util.ArrayList;
import java.util.Hashtable;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Exported;
/**
 * Job class.
 * @author Victor Martinez
 */
@ExportedBean
public final class Job implements Comparable<Job> {
    private String name;
    private String url;
    private final ArrayList<Lint> lintList = new ArrayList<Lint>();

    public Job(final String name, final String url) {
        super();
        this.name = name;
        this.url = url;
    }

    @Exported
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

    @Exported
    public Hashtable<String, Lint> getLintSet() {
        Hashtable<String, Lint> temp = new Hashtable<String, Lint>();
        for (Lint lint : lintList) {
          temp.put(lint.getName(), lint);
        }
        return temp;
    }

    public void addLint(Lint lint) {
        lintList.add(lint);
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
                    append(", ").append(lintList).toString();
    }

    @Exported
    public HealthReport getLintHealthReport() {
        if (lintList != null && lintList.size() > 0) {
            int ok = 0;
            for (Lint lint : lintList) {
                if (!lint.isIgnored() && lint.isEnabled()) {
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
