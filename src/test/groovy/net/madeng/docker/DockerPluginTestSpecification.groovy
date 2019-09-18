package net.madeng.docker

import com.github.dockerjava.core.DefaultDockerClientConfig
import org.gradle.api.logging.Logging
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DockerPluginTestSpecification extends Specification {
  DockerClient dockerClient

  @Rule
  TemporaryFolder testProjectDir = new TemporaryFolder()

  File settingsFile
  File buildFile

  String pluginId = System.getenv("PLUGIN_ID")
  String pluginArtifact = System.getenv("PLUGIN_ARTIFACT")
  List<File> pluginClasspath
  String baseSettingScript = "rootProject.name = '${pluginArtifact}-test'"
  String baseBuildScript = """
            plugins {
                id '${pluginId}'
            }
        """

  def setup() {
    dockerClient = DockerClient.getDockerClient(DefaultDockerClientConfig.createDefaultConfigBuilder().build())
    settingsFile = testProjectDir.newFile("settings.gradle")
    buildFile = testProjectDir.newFile("build.gradle")

    def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
    if (pluginClasspathResource == null) {
      throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
    }

    pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }

    dockerClient.stopContainer('nginx-test')
    dockerClient.stopContainer('httpd-test')
  }

  def cleanup() {
    dockerClient.stopContainer('nginx-test')
    dockerClient.stopContainer('httpd-test')
  }
}
