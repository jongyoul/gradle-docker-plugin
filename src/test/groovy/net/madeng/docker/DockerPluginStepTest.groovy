package net.madeng.docker

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Stepwise

@Stepwise
class DockerPluginStepTest extends DockerPluginTestSpecification {

  def "clear all"() {
    when:
    {
    }

    then:
    null == dockerClient.getContainer('httpd-test')
    null == dockerClient.getContainer('hginx-test')
  }

  def "start"() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image 'nginx:latest'
          name 'nginx-test'
        }
        startDockerContainer {
          container {
            image 'httpd:latest'
            name 'httpd-test'
          }
        }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.START_DOCKER_CONTAINER_TASK_NAME)
        .forwardOutput()
        .build()

    then:
    null != dockerClient.getContainer('httpd-test')
  }

  def "stop"() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image 'nginx:latest'
          name 'nginx-test'
        }
        stopDockerContainer {
          container {
            image 'httpd:latest'
            name 'httpd-test'
          }
        }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.STOP_DOCKER_CONTAINER_TASK_NAME)
        .build()

    then:
    null == dockerClient.getContainer('httpd-test')
  }
}