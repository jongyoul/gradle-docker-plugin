package net.madeng.docker;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.SSLConfig;

public class DockerExtension {
  private String dockerHost;
  private String registryUsername;
  private String registryPassword;
  private String registryEmail;
  private String registryUrl;
  private String dockerConfigPath;
  private String apiVersion;
  SSLConfig sslConfig;

  DockerClientConfig toDockerClientConfig() {
    final DefaultDockerClientConfig.Builder builder =
        DefaultDockerClientConfig.createDefaultConfigBuilder();

    if (null != dockerHost) {
      builder.withDockerHost(dockerHost);
    }
    if (null != registryUsername) {
      builder.withRegistryUsername(registryUsername);
    }
    if (null != registryPassword) {
      builder.withRegistryPassword(registryPassword);
    }
    if (null != registryEmail) {
      builder.withRegistryEmail(registryEmail);
    }
    if (null != registryUrl) {
      builder.withRegistryUrl(registryUrl);
    }
    if (null != dockerConfigPath) {
      builder.withDockerConfig(dockerConfigPath);
    }
    if (null != apiVersion) {
      builder.withApiVersion(apiVersion);
    }
    if (null != sslConfig) {
      builder.withCustomSslConfig(sslConfig);
    }
    return builder.build();
  }

  public String getDockerHost() {
    return dockerHost;
  }

  public void setDockerHost(String dockerHost) {
    this.dockerHost = dockerHost;
  }

  public String getRegistryUsername() {
    return registryUsername;
  }

  public void setRegistryUsername(String registryUsername) {
    this.registryUsername = registryUsername;
  }

  public String getRegistryPassword() {
    return registryPassword;
  }

  public void setRegistryPassword(String registryPassword) {
    this.registryPassword = registryPassword;
  }

  public String getRegistryEmail() {
    return registryEmail;
  }

  public void setRegistryEmail(String registryEmail) {
    this.registryEmail = registryEmail;
  }

  public String getRegistryUrl() {
    return registryUrl;
  }

  public void setRegistryUrl(String registryUrl) {
    this.registryUrl = registryUrl;
  }

  public String getDockerConfigPath() {
    return dockerConfigPath;
  }

  public void setDockerConfigPath(String dockerConfigPath) {
    this.dockerConfigPath = dockerConfigPath;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public SSLConfig getSslConfig() {
    return sslConfig;
  }

  public void setSslConfig(SSLConfig sslConfig) {
    this.sslConfig = sslConfig;
  }

  @Override
  public String toString() {
    return "DockerExtension{" +
        "dockerHost='" + dockerHost + '\'' +
        ", registryUsername='" + registryUsername + '\'' +
        ", registryPassword='" + registryPassword + '\'' +
        ", registryEmail='" + registryEmail + '\'' +
        ", registryUrl='" + registryUrl + '\'' +
        ", dockerConfigPath='" + dockerConfigPath + '\'' +
        ", apiVersion='" + apiVersion + '\'' +
        ", sslConfig=" + sslConfig +
        '}';
  }
}
