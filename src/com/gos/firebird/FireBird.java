package com.gos.firebird;

import com.gos.firebird.config.Config;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * com.gos.firebird.FireBird
 * <p>
 * Created by suika on 15-12-13.
 */
public class FireBird implements ApplicationComponent {

    private Config.State state = Config.getInstance().state;

    public FireBird() {}

    @Override
    public void initComponent() {
        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
            @Override
            public void projectOpened(Project project) {
                if (state.IS_ENABLE)
                    FireBirdManager.getInstance().init(project);
                super.projectOpened(project);
            }

            @Override
            public void projectClosed(Project project) {
                FireBirdManager.getInstance().destroy(project, true);
                super.projectClosed(project);
            }

            @Override
            public void projectClosing(Project project) {
                super.projectClosing(project);
            }

            @Override
            public boolean canCloseProject(Project project) {
                return super.canCloseProject(project);
            }
        });
    }

    @Override
    public void disposeComponent() {
        FireBirdManager.getInstance().destroyAll();
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "com.gos.firebird.FireBird";
    }
}
