package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Api;
import org.jenkins.ci.plugins.jenkinslint.Messages;
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
import org.jenkins.ci.plugins.jenkinslint.check.MasterLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MavenJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MultibranchJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.NullSCMChecker;
import org.jenkins.ci.plugins.jenkinslint.check.PollingSCMTriggerChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveDescriptionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.SlaveVersionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.TimeoutChecker;
import org.jenkins.ci.plugins.jenkinslint.check.TimerTriggerChecker;
import org.jenkins.ci.plugins.jenkinslint.check.WindowsSlaveLaunchChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AbstractAction class.
 * @author Victor Martinez
 */
public abstract class AbstractAction {
    protected static final Logger LOG = Logger.getLogger(AbstractAction.class.getName());
    private ArrayList<InterfaceCheck> checkList = new ArrayList<InterfaceCheck>();
    private ArrayList<InterfaceSlaveCheck> slaveCheckList = new ArrayList<InterfaceSlaveCheck>();

    public AbstractAction() {
        super();
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

    public Api getApi() {
        try {
            this.getData();
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Failing when getting the JenkinsLint data through the API");
        }
        return new Api(this);
    }

    public abstract void getData() throws IOException;

    public ArrayList<InterfaceCheck> getCheckList() {
        return checkList;
    }

    public ArrayList<InterfaceSlaveCheck> getSlaveCheckList() {
        return slaveCheckList;
    }

    protected void reloadCheckList() {
        checkList.clear();

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
    }

    protected void reloadSlaveCheckList() {
        slaveCheckList.clear();

        slaveCheckList.add(new SlaveDescriptionChecker());
        slaveCheckList.add(new SlaveVersionChecker());
        slaveCheckList.add(new SlaveLabelChecker());
        slaveCheckList.add(new WindowsSlaveLaunchChecker());
    }



}
