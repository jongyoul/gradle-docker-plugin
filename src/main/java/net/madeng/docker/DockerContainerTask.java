package net.madeng.docker;

import com.github.dockerjava.core.DockerClientConfig;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

public class DockerContainerTask extends DefaultTask {
  @Input @Optional DockerExtension docker;

  @Input @Optional ContainerExtension container;

  @Internal
  DockerClientConfig getDockerClientConfig() {
    if (null != docker) {
      getLogger().info("Overridden value will be used: [{}]", docker);
      return docker.toDockerClientConfig();
    } else {
      DockerExtension dockerExtension =
          getProject().getExtensions().getByType(DockerExtension.class);
      getLogger().info("Extension value will be used: [{}]", dockerExtension);
      return dockerExtension.toDockerClientConfig();
    }
  }

  @Internal
  ContainerExtension getContainerExtension() {
    if (null != container) {
      getLogger().info("Overridden information will be used: [{}]", container);
      return container;
    } else {
      ContainerExtension containerExtension =
          getProject().getExtensions().getByType(ContainerExtension.class);
      getLogger().info("Extension value will be used: [{}]", containerExtension);
      return containerExtension;
    }
  }

  @Internal
  DockerClient getDockerClient() {
    return DockerClient.getDockerClient(getDockerClientConfig());
  }

  public DockerExtension getDocker() {
    return docker;
  }

  public void setDocker(DockerExtension docker) {
    this.docker = docker;
  }

  public ContainerExtension getContainer() {
    return container;
  }

  public void setContainer(ContainerExtension container) {
    this.container = container;
  }

  public static class StartDockerContainerTask extends DockerContainerTask {
    @TaskAction
    public void startDockerContainer() throws InterruptedException {
      getLogger().debug("Starting a docker container");
      getLogger().debug("{}", getDockerClient().getInfo());

      ContainerExtension containerExtension = getContainerExtension();
      getLogger().debug("{}", containerExtension);

      getDockerClient()
          .startContainer(
              containerExtension.getImage(),
              containerExtension.getName(),
              containerExtension.getEnvs(),
              containerExtension.getPorts(),
              containerExtension.getVolumes());
    }
  }

  public static class StopDockerContainerTask extends DockerContainerTask {
    @TaskAction
    public void stopDockerContainer() {
      getLogger().debug("Stopping a docker container");
      getLogger().debug("{}", getDockerClient().getInfo());

      ContainerExtension containerExtension = getContainerExtension();
      getLogger().debug("{}", containerExtension);

      getDockerClient().stopContainer(containerExtension.getName());
    }
  }
}
