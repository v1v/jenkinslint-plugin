package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.*;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExportedBean
@Extension
public final class JenkinsLintAction extends AbstractAction implements RootAction {

    private static final Logger LOG = Logger.getLogger(JenkinsLintAction.class.getName());
    private Hashtable<String, Job> jobSet = new Hashtable<String, Job>();
    private Hashtable<String, Slave> slaveSet = new Hashtable<String, Slave>();

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
        jobSet.clear();
        slaveSet.clear();

        this.reloadCheckList();
        this.reloadSlaveCheckList();

        for (hudson.model.Job item : Jenkins.getInstance().getAllItems(hudson.model.Job.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getName());
            Job newJob = new Job(item.getName(), item.getUrl());
            for (InterfaceCheck checker : this.getCheckList()) {
                LOG.log(Level.FINER, checker.getName() + " " + item.getName() + " " + checker.executeCheck(item));
                // Lint is disabled when is ignored or globally disabled
                newJob.addLint(new Lint(checker.getName(), checker.executeCheck(item), checker.isIgnored(item.getDescription()), checker.isEnabled()));
            }
            jobSet.put(item.getName(),newJob);
            LOG.log(Level.FINER, newJob.toString());
        }


        for (Node node : Jenkins.getInstance().getNodes()) {
            LOG.log(Level.FINER, "querySlaveCheck " + node.getDisplayName());
            Slave newSlave = new Slave(node.getNodeName(), node.getSearchUrl());
            for (InterfaceSlaveCheck checker : this.getSlaveCheckList()) {
                boolean status = checker.executeCheck(node);
                LOG.log(Level.FINER, checker.getName() + " " + node.getDisplayName() + " " + status);
                newSlave.addLint(new Lint(checker.getName(), status, checker.isIgnored(node.getNodeDescription()), checker.isEnabled()));
            }
            slaveSet.put(newSlave.getName(), newSlave);
            LOG.log(Level.FINER, newSlave.toString());
        }
    }

    @Exported
    public Hashtable<String, Job> getJobSet() {
        return jobSet;
    }

    @Exported
    public Hashtable<String, InterfaceCheck> getCheckSet() {
        Hashtable<String, InterfaceCheck> temp = new Hashtable<String, InterfaceCheck>();
        for (InterfaceCheck check : this.getCheckList()) {
          temp.put(check.getName(), check);
        }
        return temp;
    }

    @Exported
    public Hashtable<String, Slave> getSlaveSet() {
        return slaveSet;
    }

    @Exported
    public Hashtable<String, InterfaceSlaveCheck> getSlaveCheckSet() {
        Hashtable<String, InterfaceSlaveCheck> temp = new Hashtable<String, InterfaceSlaveCheck>();
        for (InterfaceSlaveCheck check : this.getSlaveCheckList()) {
          temp.put(check.getName(), check);
        }
        return temp;
    }
}
