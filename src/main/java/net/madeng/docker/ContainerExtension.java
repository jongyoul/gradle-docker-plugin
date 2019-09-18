package net.madeng.docker;

import java.util.Map;

public class ContainerExtension {
  private String image;
  private String name;
  private Map<String, String> envs;
  private Map<String, String> ports;
  private Map<String, String> volumes;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getEnvs() {
    return envs;
  }

  public void setEnvs(Map<String, String> envs) {
    this.envs = envs;
  }

  public Map<String, String> getPorts() {
    return ports;
  }

  public void setPorts(Map<String, String> ports) {
    this.ports = ports;
  }

  public Map<String, String> getVolumes() {
    return volumes;
  }

  public void setVolumes(Map<String, String> volumes) {
    this.volumes = volumes;
  }

  @Override
  public String toString() {
    return "ContainerExtension{"
        + "image='"
        + image
        + '\''
        + ", name='"
        + name
        + '\''
        + ", envs="
        + envs
        + ", ports="
        + ports
        + ", volumes="
        + volumes
        + '}';
  }
}
