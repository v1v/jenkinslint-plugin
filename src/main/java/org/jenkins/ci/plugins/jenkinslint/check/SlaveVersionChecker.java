package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Node;
import hudson.remoting.Callable;
import hudson.remoting.Launcher;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractSlaveCheck;
import org.jenkinsci.remoting.RoleChecker;

import java.io.IOException;

/**
 * Based on: https://github.com/jenkinsci/versioncolumn-plugin
 * @author Victor Martinez
 */
public class SlaveVersionChecker extends AbstractSlaveCheck {

    private static final String masterVersion = Launcher.VERSION;

    public SlaveVersionChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.SlaveVersionCheckerDesc());
        this.setSeverity(Messages.SlaveVersionCheckerSeverity());
    }

    public boolean executeCheck(Node item) {
        try {
            String version = item.getChannel().call(new SlaveVersion());
            if (version == null || !version.equals(masterVersion)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    private static final class SlaveVersion implements Callable<String, IOException> {

        private static final long serialVersionUID = 1L;

        public String call() throws IOException {
            try {
                return Launcher.VERSION;
            } catch (Throwable ex) {
                // Older slave.jar won't have VERSION
                return "< 1.335";
            }
        }

        @Override
        public void checkRoles(RoleChecker roleChecker) throws SecurityException {

        }
    }
}
