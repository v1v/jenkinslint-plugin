package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Api;
import org.jenkins.ci.plugins.jenkinslint.Messages;
import org.jenkins.ci.plugins.jenkinslint.check.*;

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
        checkList.add(new GitRefSubmoduleChecker());
    }

    protected void reloadSlaveCheckList() {
        slaveCheckList.clear();

        slaveCheckList.add(new SlaveDescriptionChecker());
        slaveCheckList.add(new SlaveVersionChecker());
        slaveCheckList.add(new SlaveLabelChecker());
        slaveCheckList.add(new WindowsSlaveLaunchChecker());
    }



}
