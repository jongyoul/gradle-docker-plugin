package net.madeng.docker;

import static net.madeng.docker.TypeUtil.makeString;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.SSLConfig;

public class DockerExtension {
  private Object dockerHost;
  private Object registryUsername;
  private Object registryPassword;
  private Object registryEmail;
  private Object registryUrl;
  private Object dockerConfigPath;
  private Object apiVersion;
  SSLConfig sslConfig;

  DockerClientConfig toDockerClientConfig() {
    final DefaultDockerClientConfig.Builder builder =
        DefaultDockerClientConfig.createDefaultConfigBuilder();

    if (null != dockerHost) {
      builder.withDockerHost(getDockerHost());
    }
    if (null != registryUsername) {
      builder.withRegistryUsername(getRegistryUsername());
    }
    if (null != registryPassword) {
      builder.withRegistryPassword(getRegistryPassword());
    }
    if (null != registryEmail) {
      builder.withRegistryEmail(getRegistryEmail());
    }
    if (null != registryUrl) {
      builder.withRegistryUrl(getRegistryUrl());
    }
    if (null != dockerConfigPath) {
      builder.withDockerConfig(getDockerConfigPath());
    }
    if (null != apiVersion) {
      builder.withApiVersion(getApiVersion());
    }
    if (null != sslConfig) {
      builder.withCustomSslConfig(sslConfig);
    }
    return builder.build();
  }

  public String getDockerHost() {
    return makeString(dockerHost);
  }

  public void setDockerHost(Object dockerHost) {
    this.dockerHost = dockerHost;
  }

  public String getRegistryUsername() {
    return makeString(registryUsername);
  }

  public void setRegistryUsername(Object registryUsername) {
    this.registryUsername = registryUsername;
  }

  public String getRegistryPassword() {
    return makeString(registryPassword);
  }

  public void setRegistryPassword(Object registryPassword) {
    this.registryPassword = registryPassword;
  }

  public String getRegistryEmail() {
    return makeString(registryEmail);
  }

  public void setRegistryEmail(Object registryEmail) {
    this.registryEmail = registryEmail;
  }

  public String getRegistryUrl() {
    return makeString(registryUrl);
  }

  public void setRegistryUrl(Object registryUrl) {
    this.registryUrl = registryUrl;
  }

  public String getDockerConfigPath() {
    return makeString(dockerConfigPath);
  }

  public void setDockerConfigPath(Object dockerConfigPath) {
    this.dockerConfigPath = dockerConfigPath;
  }

  public String getApiVersion() {
    return makeString(apiVersion);
  }

  public void setApiVersion(Object apiVersion) {
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
