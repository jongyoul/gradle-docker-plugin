package net.madeng.docker

import com.github.dockerjava.core.command.ExecStartResultCallback
import org.gradle.internal.impldep.org.codehaus.plexus.util.StringOutputStream
import org.gradle.testkit.runner.GradleRunner

import java.nio.file.Files
import java.nio.file.Paths

class DockerPluginSingleTest extends DockerPluginTestSpecification {

  def 'All tasks should be listed in "gradle tasks"'() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments('tasks')
        .forwardOutput()
        .build()

    then:
    DockerPlugin.ALL_TASK_NAMES.every { it -> result.output.contains(it) }
  }

  def 'Start docker container'() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image = 'nginx:latest'
          name = 'nginx-test'
          ports (
            '80': '10080',
            '443': '10443'
          )
        }
    """

    when:
    def before = null == dockerClient.getContainer('nginx-test')
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.START_DOCKER_CONTAINER_TASK_NAME)
        .build()
    def after = null != dockerClient.getContainer('nginx-test')

    then:
    before && after
  }

  def 'Stop docker container'() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image = 'nginx:latest'
          name = 'nginx-test'
          ports (
            '80': '10080',
            '443': '10443'
          )
        }
    """

    when:
    dockerClient.startContainer('nginx:latest', 'nginx-test', null, null, null);
    def before = null != dockerClient.getContainer('nginx-test')
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.STOP_DOCKER_CONTAINER_TASK_NAME)
        .forwardOutput()
        .build()
    def after = null == dockerClient.getContainer('nginx-test')

    then:
    before && after
  }

  def 'Map envs'() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image = 'nginx:latest'
          name = 'nginx-test'
          envs (
            'TEST_ENV': 'AAAA',
          )
        }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.START_DOCKER_CONTAINER_TASK_NAME)
        .build()
    def containerId = dockerClient.getContainer('nginx-test').getId()
    def execCreateCmdResponse = dockerClient.getDockerClient().execCreateCmd(containerId).withCmd('/bin/bash', '-c', 'echo TEST_ENV=$TEST_ENV').withAttachStdout(true).exec()
    def stringOutputStream = new StringOutputStream()
    def execStartResultCallback = new ExecStartResultCallback(stringOutputStream, stringOutputStream)
    dockerClient.getDockerClient().execStartCmd(execCreateCmdResponse.getId()).exec(execStartResultCallback).awaitCompletion()

    then:
    stringOutputStream.toString().contains('TEST_ENV=AAAA')
  }

  def 'Map ports'() {
    given:
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image = 'nginx:latest'
          name = 'nginx-test'
          ports (
            '80': '10080',
            '443': '10443'
          )
        }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.START_DOCKER_CONTAINER_TASK_NAME)
        .build()
    def container = dockerClient.getContainer('nginx-test')

    then:
    container.ports.collect { it.privatePort.toString() }.contains('80')
    container.ports.collect { it.privatePort.toString() }.contains('443')
  }

  def 'Map Volumes'() {
    given:
    def tempDir = System.getProperty('java.io.tmpdir')
    if (tempDir.startsWith('/var') && System.getProperty('os.name').toLowerCase().contains('mac')) {
      tempDir = '/private' + tempDir
    }
    settingsFile << baseSettingScript
    buildFile << baseBuildScript
    buildFile << """
        container {
          image = 'nginx:latest'
          name = 'nginx-test'
          volumes (
            '/test-volume': '${tempDir}'
          )
        }
    """

    when:
    def tempFile = Paths.get(tempDir, 'foo')
    if (Files.exists(tempFile)) {
      Files.delete(tempFile)
    }
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath(pluginClasspath)
        .withArguments(DockerPlugin.START_DOCKER_CONTAINER_TASK_NAME)
        .build()
    def container = dockerClient.getContainer('nginx-test')
    def containerId = container.getId()
    def execCreateCmdResponse = dockerClient.getDockerClient().execCreateCmd(containerId).withCmd('/bin/touch', '/test-volume/foo').exec()
    def stringOutputStream = new StringOutputStream()
    def execStartResultCallback = new ExecStartResultCallback(stringOutputStream, stringOutputStream)
    dockerClient.getDockerClient().execStartCmd(execCreateCmdResponse.getId()).exec(execStartResultCallback).awaitCompletion()

    then:
    Files.exists(Paths.get(tempDir, 'foo'))
  }

}