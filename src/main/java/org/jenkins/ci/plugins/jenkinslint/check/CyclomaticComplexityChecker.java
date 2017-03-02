package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class CyclomaticComplexityChecker extends AbstractCheck {

    private static final String SYS_PROPERTY_NAME  = CyclomaticComplexityChecker.class.getName() + ".threshold";
    private int threshold = 4;


    public CyclomaticComplexityChecker() {
        super();
        this.setDescription(Messages.CyclometicComplexityCheckerDesc());
        this.setSeverity(Messages.CyclometicComplexityCheckerSeverity());
        String limit = System.getProperty(SYS_PROPERTY_NAME);
        if (limit != null) {
            try {
                threshold = Integer.valueOf(limit);
            } catch (NumberFormatException e) {
                LOG.warning("Ignoring invalid " + SYS_PROPERTY_NAME + "=" + limit);
            }
        }
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (Jenkins.getInstance().pluginManager.getPlugin("conditional-buildstep") != null) {

            int cyclomatic = 0;
            if (item.getClass().getSimpleName().equals("MavenModuleSet")) {
                try {
                    Object getPrebuilders = item.getClass().getMethod("getPrebuilders", null).invoke(item);
                    if (getPrebuilders instanceof List) {
                        cyclomatic = totalCyclomatic(((List)getPrebuilders));
                        found = cyclomatic > threshold;                    }
                }catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                }
            }
            if (item instanceof Project) {
                cyclomatic = totalCyclomatic(((Project) item).getBuilders());
                found = cyclomatic > threshold;
            }
            if (item instanceof MatrixProject) {
                cyclomatic = totalCyclomatic(((MatrixProject) item).getBuilders());
                found = cyclomatic > threshold;
            }

            LOG.log(Level.INFO, "Cyclomatic complexity of " + item.getName() + " is " + cyclomatic);

        } else {
            LOG.log(Level.INFO, "Conditional BuildStep plugin is not installed");
        }
        return found;
    }

    private int totalCyclomatic(List<Builder> builders) {
        int cyclomatic = 0;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder.getClass().getName().endsWith("ConditionalBuilder")) {
                    cyclomatic += cyclomatic(builder, 0);
                }
            }
        }
        return cyclomatic;
    }

    private int conditionalCyclomatic(Object builder) {
        int cyclomatic = 0;
        try {
            Object getConditionalbuilders = builder.getClass().getMethod("getConditionalbuilders", null).invoke(builder);
            if (getConditionalbuilders instanceof List) {
                for (Object conditional : ((List) getConditionalbuilders)) {
                    cyclomatic += cyclomatic(conditional, 0);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
            cyclomatic = 0;
        }
        return cyclomatic;
    }

    private int cyclomatic(Object builder, int cyclomatic) {
        try {
            Object runCondition = builder.getClass().getMethod("getRunCondition", null).invoke(builder);
            switch (runCondition.getClass().getSimpleName()) {
                case "And":
                    cyclomatic += conditionalCyclomatic(builder);
                    break;
                case "Or":
                    cyclomatic += conditionalCyclomatic(builder);
                    break;
                case "Not":
                    cyclomatic += conditionalCyclomatic(builder);
                    break;
                default:
                    cyclomatic++;
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
        }
        return cyclomatic;
    }
}
