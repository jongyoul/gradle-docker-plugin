package net.madeng.docker;

import net.madeng.docker.DockerContainerTask.StartDockerContainerTask;
import net.madeng.docker.DockerContainerTask.StopDockerContainerTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DockerPlugin implements Plugin<Project> {
  static final String EXTENSION_DOCKER_NAME = "docker";
  static final String EXTENSION_CONTAINER_NAME = "container";
  static final String START_DOCKER_CONTAINER_TASK_NAME = "startDockerContainer";
  static final String STOP_DOCKER_CONTAINER_TASK_NAME = "stopDockerContainer";

  static final String ALL_TASK_NAMES[] = {
    START_DOCKER_CONTAINER_TASK_NAME, STOP_DOCKER_CONTAINER_TASK_NAME
  };

  @Override
  public void apply(Project project) {
    project.getExtensions().create(EXTENSION_DOCKER_NAME, DockerExtension.class);
    project.getExtensions().create(EXTENSION_CONTAINER_NAME, ContainerExtension.class);

    project.getTasks().create(START_DOCKER_CONTAINER_TASK_NAME, StartDockerContainerTask.class, task -> {
      task.setGroup("docker");
      task.setDescription("Run a docker container");
    });
    project.getTasks().create(STOP_DOCKER_CONTAINER_TASK_NAME, StopDockerContainerTask.class, task -> {
      task.setGroup("docker");
      task.setDescription("Stop a docker container");
    });
  }
}
