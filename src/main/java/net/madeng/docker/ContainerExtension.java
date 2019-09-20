package net.madeng.docker;

import static net.madeng.docker.TypeUtil.makeString;
import static net.madeng.docker.TypeUtil.makeStringMap;

import java.util.Map;

public class ContainerExtension {
  private Object image;
  private Object name;
  private Map<Object, Object> envs;
  private Map<Object, Object> ports;
  private Map<Object, Object> volumes;
  private boolean restart = true;

  public String getImage() {
    return makeString(image);
  }

  public void setImage(Object image) {
    this.image = image;
  }

  public String getName() {
    return makeString(name);
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getEnvs() {
    return makeStringMap(envs);
  }

  public void setEnvs(Map<Object, Object> envs) {
    this.envs = envs;
  }

  public Map<String, String> getPorts() {
    return makeStringMap(ports);
  }

  public void setPorts(Map<Object, Object> ports) {
    this.ports = ports;
  }

  public Map<String, String> getVolumes() {
    return makeStringMap(volumes);
  }

  public void setVolumes(Map<Object, Object> volumes) {
    this.volumes = volumes;
  }

  public boolean isRestart() {
    return restart;
  }

  public void setRestart(boolean restart) {
    this.restart = restart;
  }

  @Override
  public String toString() {
    return "ContainerExtension{"
        + "image="
        + image
        + ", name="
        + name
        + ", envs="
        + envs
        + ", ports="
        + ports
        + ", volumes="
        + volumes
        + ", restart="
        + restart
        + '}';
  }
}
