package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Node;
import hudson.model.RootAction;
import hudson.util.ChartUtil;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.graph.JenkinsLintGraph;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractAction;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceSlaveCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.jenkins.ci.plugins.jenkinslint.model.Slave;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
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
        this.getJobSet().clear();
        this.getSlaveSet().clear();

        this.reloadCheckList();
        this.reloadSlaveCheckList();

        for (hudson.model.Job item : Jenkins.getInstance().getAllItems(hudson.model.Job.class)) {
            // Fixing MatrixJobs @JENKINS-46176
            if (!item.getParent().getClass().getSimpleName().equals("MatrixProject")) {
                LOG.log(Level.FINER, "queryChecks " + item.getName());
                Job newJob = new Job(item.getName(), item.getUrl());
                for (InterfaceCheck checker : this.getCheckList()) {
                    LOG.log(Level.FINER, checker.getName() + " " + item.getName() + " " + checker.executeCheck(item));
                    // Lint is disabled when is ignored or globally disabled
                    newJob.addLint(new Lint(checker.getName(), checker.executeCheck(item), checker.isIgnored(item.getDescription()), checker.isEnabled()));
                }
                this.getJobSet().put(item.getName(), newJob);
                LOG.log(Level.FINER, newJob.toString());
            } else {
                LOG.log(Level.FINER, "Excluded MatrixConfiguration " + item.getName());
            }
        }

        for (Node node : Jenkins.getInstance().getNodes()) {
            LOG.log(Level.FINER, "querySlaveCheck " + node.getDisplayName());
            Slave newSlave = new Slave(node.getNodeName(), node.getSearchUrl());
            for (InterfaceSlaveCheck checker : this.getSlaveCheckList()) {
                boolean status = checker.executeCheck(node);
                LOG.log(Level.FINER, checker.getName() + " " + node.getDisplayName() + " " + status);
                newSlave.addLint(new Lint(checker.getName(), status, checker.isIgnored(node.getNodeDescription()), checker.isEnabled()));
            }
            this.getSlaveSet().put(newSlave.getName(), newSlave);
            LOG.log(Level.FINER, newSlave.toString());
        }
    }

    @Exported
    public synchronized Hashtable<String, Job> getJobSet() {
        return jobSet;
    }

    @Exported
    public synchronized Hashtable<String, InterfaceCheck> getCheckSet() {
        Hashtable<String, InterfaceCheck> temp = new Hashtable<String, InterfaceCheck>();
        for (InterfaceCheck check : this.getCheckList()) {
          temp.put(check.getName(), check);
        }
        return temp;
    }

    @Exported
    public synchronized Hashtable<String, Slave> getSlaveSet() {
        return slaveSet;
    }

    @Exported
    public synchronized Hashtable<String, InterfaceSlaveCheck> getSlaveCheckSet() {
        Hashtable<String, InterfaceSlaveCheck> temp = new Hashtable<String, InterfaceSlaveCheck>();
        for (InterfaceSlaveCheck check : this.getSlaveCheckList()) {
          temp.put(check.getName(), check);
        }
        return temp;
    }

    public void doGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath()+"/images/headless.png");
            return;
        }
        ChartUtil.generateGraph(req,rsp, JenkinsLintGraph.createChart(this.getJobSet().elements()),1024,768);
    }

    public void doPieGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath()+"/images/headless.png");
            return;
        }
        ChartUtil.generateGraph(req,rsp, JenkinsLintGraph.createPieChart(this.getJobSet().elements()),512,384);
    }


    public void doSeverityPieGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath()+"/images/headless.png");
            return;
        }
        ChartUtil.generateGraph(req,rsp, JenkinsLintGraph.createSeverityPieChart(this.getJobSet().elements(), this.getCheckSet()),512,384);
    }
}
