package fr.aresrpg.gradle.api;

import fr.aresrpg.gradle.api.task.ApiJarTask;
import fr.aresrpg.gradle.api.task.ApiTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.SourceSet;

import java.io.File;

public class ApiPlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		configureDependencies(project);
		ApiTask apiTask = configureApiTask(project);
		ApiJarTask jarTask = configureJarApiTask(project , apiTask);
		configureComponent(project , jarTask);
	}

	private void configureDependencies(Project project){
		project.getPluginManager().apply(JavaPlugin.class);
	}

	private ApiTask configureApiTask(Project project){
		ApiTask task = project.getTasks().create(ApiTask.NAME , ApiTask.class);
		task.setInputFolder(project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput().getClassesDir());
		task.setOutputFolder(new File(project.getBuildDir() , "api"));
		task.getDependsOn().add(project.getTasks().getByName(JavaPlugin.CLASSES_TASK_NAME));
		return task;
	}

	private ApiJarTask configureJarApiTask(Project project , ApiTask apiTask){
		ApiJarTask apiJarTask = project.getTasks().create(ApiJarTask.NAME , ApiJarTask.class);
		apiJarTask.getDependsOn().add(apiTask);
		apiJarTask.from(apiTask.getOutputFolder());
		apiJarTask.setOnlyIf(new ApiJarTaskSpec(apiTask));
		return apiJarTask;
	}

	private void configureComponent(Project project , ApiJarTask task){
		project.getComponents().add(new ApiComponent((SoftwareComponentInternal) project.getComponents().getByName("java"), new ArchivePublishArtifact(task)));
	}

	private static class ApiJarTaskSpec implements Spec<Task>{
		private ApiTask task;

		private ApiJarTaskSpec(ApiTask task) {
			this.task = task;
		}

		@Override
		public boolean isSatisfiedBy(Task task) {
			return this.task.getState().getExecuted();
		}
	}
}
