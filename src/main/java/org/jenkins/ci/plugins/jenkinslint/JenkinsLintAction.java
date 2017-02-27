package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.check.ArtifactChecker;
import org.jenkins.ci.plugins.jenkinslint.check.CleanupWorkspaceChecker;
import org.jenkins.ci.plugins.jenkinslint.check.GitRefChecker;
import org.jenkins.ci.plugins.jenkinslint.check.GitShallowChecker;
import org.jenkins.ci.plugins.jenkinslint.check.GradleWrapperChecker;
import org.jenkins.ci.plugins.jenkinslint.check.GroovySystemExitChecker;
import org.jenkins.ci.plugins.jenkinslint.check.HardcodedScriptChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JavadocChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobAssignedLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobDescriptionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobLogRotatorChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobNameChecker;
import org.jenkins.ci.plugins.jenkinslint.check.TimerTriggerChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MasterLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MavenJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MultibranchJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.NullSCMChecker;
import org.jenkins.ci.plugins.jenkinslint.check.PollingSCMTriggerChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveDescriptionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveVersionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.TimeoutChecker;
import org.jenkins.ci.plugins.jenkinslint.check.WindowsSlaveLaunchChecker;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceSlaveCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.jenkins.ci.plugins.jenkinslint.model.Slave;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.Api;

@ExportedBean
@Extension
public final class JenkinsLintAction implements RootAction {

    private static final Logger LOG = Logger.getLogger(JenkinsLintAction.class.getName());
    private Hashtable<String, Job> jobSet = new Hashtable<String, Job>();
    private ArrayList<InterfaceCheck> checkList = new ArrayList<InterfaceCheck>();
    private Hashtable<String, Slave> slaveSet = new Hashtable<String, Slave>();
    private ArrayList<InterfaceSlaveCheck> slaveCheckList = new ArrayList<InterfaceSlaveCheck>();

    public Api getApi() {
        try {
            this.getData();
        } catch (IOException ioe) {
            LOG.log(Level.INFO, "Failing when getting the JenkinsLint data through the API");
        }
        return new Api(this);
    }

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
        jobSet.clear();
        checkList.clear();
        slaveSet.clear();
        slaveCheckList.clear();

        checkList.add(new JobNameChecker());
        checkList.add(new JobDescriptionChecker());
        checkList.add(new JobAssignedLabelChecker());
        checkList.add(new MasterLabelChecker());
        checkList.add(new JobLogRotatorChecker());
        checkList.add(new MavenJobTypeChecker());
        checkList.add(new CleanupWorkspaceChecker());
        checkList.add(new JavadocChecker());
        checkList.add(new ArtifactChecker());
        checkList.add(new NullSCMChecker());
        checkList.add(new PollingSCMTriggerChecker( ));
        checkList.add(new GitShallowChecker());
        checkList.add(new MultibranchJobTypeChecker());
        checkList.add(new HardcodedScriptChecker());
        checkList.add(new GradleWrapperChecker());
        checkList.add(new TimeoutChecker());
        checkList.add(new GroovySystemExitChecker());
        checkList.add(new GitRefChecker());
        checkList.add(new TimerTriggerChecker());

        slaveCheckList.add(new SlaveDescriptionChecker());
        slaveCheckList.add(new SlaveVersionChecker());
        slaveCheckList.add(new SlaveLabelChecker());
        slaveCheckList.add(new WindowsSlaveLaunchChecker());

        for (AbstractProject item : Jenkins.getInstance().getAllItems(AbstractProject.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getDisplayName());
            Job newJob = new Job(item.getName(), item.getUrl());
            for (InterfaceCheck checker : checkList) {
                LOG.log(Level.FINER, checker.getName() + " " + item.getName() + " " + checker.executeCheck(item));
                newJob.addLint(new Lint(checker.getName(), checker.executeCheck(item), checker.isIgnored(item.getDescription())));
            }
            jobSet.put(item.getName(),newJob);
            LOG.log(Level.FINER, newJob.toString());
            //TODO: Update Job Data item.getActions().add(new GeneratedJobsAction());
        }


        for (Node node : Jenkins.getInstance().getNodes()) {
            LOG.log(Level.FINER, "querySlaveCheck " + node.getDisplayName());
            Slave newSlave = new Slave(node.getNodeName(), node.getSearchUrl());
            for (InterfaceSlaveCheck checker : slaveCheckList) {
                boolean status = checker.executeCheck(node);
                LOG.log(Level.FINER, checker.getName() + " " + node.getDisplayName() + " " + status);
                newSlave.addLint(new Lint(checker.getName(), status, checker.isIgnored(node.getNodeDescription())));
            }
            slaveSet.put(newSlave.getName(), newSlave);
            LOG.log(Level.FINER, newSlave.toString());
            //TODO: Update Job Data item.getActions().add(new GeneratedJobsAction());
        }
    }

    public String getDisplayName() {
        return Messages.DisplayName();
    }

    public String getIconFileName() {
        return Messages.IconFileName();
    }

    public String getUrlName() {
        return Messages.UrlName();
    }

    @Exported
    public Hashtable<String, Job> getJobSet() {
        return jobSet;
    }

    public ArrayList<InterfaceCheck> getCheckList() {
        return checkList;
    }

    @Exported
    public Hashtable<String, InterfaceCheck> getCheckSet() {
        Hashtable<String, InterfaceCheck> temp = new Hashtable<String, InterfaceCheck>();
        for (InterfaceCheck check : checkList) {
          temp.put(check.getName(), check);
        }
        return temp;
    }

    @Exported
    public Hashtable<String, Slave> getSlaveSet() {
        return slaveSet;
    }

    public ArrayList<InterfaceSlaveCheck> getSlaveCheckList() {
        return slaveCheckList;
    }

    @Exported
    public Hashtable<String, InterfaceSlaveCheck> getSlaveCheckSet() {
        Hashtable<String, InterfaceSlaveCheck> temp = new Hashtable<String, InterfaceSlaveCheck>();
        for (InterfaceSlaveCheck check : slaveCheckList) {
          temp.put(check.getName(), check);
        }
        return temp;
    }
}
