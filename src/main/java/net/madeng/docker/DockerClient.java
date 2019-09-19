package net.madeng.docker;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DockerClient {
  private static final Logger logger = Logging.getLogger(DockerClient.class);
  private static final Map<DockerClientConfig, DockerClient> dockerClientMap =
      Collections.synchronizedMap(new HashMap<>());

  private final com.github.dockerjava.api.DockerClient dockerClient;

  DockerClient(com.github.dockerjava.api.DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  /* `synchronized` ........... */
  public static synchronized DockerClient getDockerClient(DockerClientConfig dockerClientConfig) {
    return dockerClientMap.getOrDefault(
        dockerClientConfig,
        new DockerClient(DockerClientBuilder.getInstance(dockerClientConfig).build()));
  }

  @VisibleForTesting
  Container getContainer(String name) {
    Objects.requireNonNull(name, "'name' should set");
    return dockerClient.listContainersCmd().exec().stream()
        .filter(c -> 0 >= Arrays.binarySearch(c.getNames(), name))
        .findFirst()
        .orElse(null);
  }

  Info getInfo() {
    return dockerClient.infoCmd().exec();
  }

  public void startContainer(
      String image,
      String name,
      Map<String, String> envs,
      Map<String, String> ports,
      Map<String, String> volumes)
      throws InterruptedException {

    List<PortBinding> portBindings =
        Optional.ofNullable(ports).orElse(new HashMap<>()).entrySet().stream()
            .map(
                e ->
                    new PortBinding(
                        Ports.Binding.bindPort(Integer.parseInt(e.getKey())),
                        ExposedPort.tcp(Integer.parseInt(e.getValue()))))
            .collect(Collectors.toList());
    List<Bind> volumeBinds =
        Optional.ofNullable(volumes).orElse(new HashMap<>()).entrySet().stream()
            .map(e -> new Bind(e.getKey(), new Volume(e.getValue())))
            .collect(Collectors.toList());

    PullImageResultCallback callback = new PullImageResultCallback();
    dockerClient.pullImageCmd(image).exec(callback);
    callback.awaitCompletion();
    CreateContainerResponse createContainerResponse =
        dockerClient
            .createContainerCmd(image)
            .withName(name)
            .withEnv(
                Optional.ofNullable(envs).orElse(new HashMap<>()).entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.toList()))
            .withExposedPorts(
                portBindings.stream().map(PortBinding::getExposedPort).collect(Collectors.toList()))
            .withPortBindings(portBindings)
            .withVolumes(volumeBinds.stream().map(Bind::getVolume).collect(Collectors.toList()))
            .withBinds(volumeBinds)
            .exec();
    String containerId = createContainerResponse.getId();
    dockerClient.startContainerCmd(containerId).exec();
  }

  public void stopContainer(String name) {
    Container container = getContainer(name);
    if (null != container) {
      String containerId = container.getId();
      dockerClient.stopContainerCmd(containerId).exec();
      dockerClient.removeContainerCmd(containerId).exec();
    } else {
      logger.info("No container name of [{}] is running", name);
    }
  }

  @VisibleForTesting
  com.github.dockerjava.api.DockerClient getDockerClient() {
    return dockerClient;
  }
}
