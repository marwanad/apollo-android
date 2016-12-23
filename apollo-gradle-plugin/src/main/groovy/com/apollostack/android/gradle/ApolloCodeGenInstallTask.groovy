package com.apollostack.android.gradle

import com.moowork.gradle.node.npm.NpmTask
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class ApolloCodeGenInstallTask extends NpmTask {
  static final String NAME = "installApolloCodegen"
  static final String INSTALL_DIR = "node_modules/apollo-codegen"
  static final String APOLLOCODEGEN_VERSION = "0.9.6"

  public ApolloCodeGenInstallTask() {
    group = ApolloPlugin.TASK_GROUP
    description = "Runs npm install for apollo-codegen"

    File apolloPackageDir = this.project.file("package.json")
    if (!apolloPackageDir.isFile()) {
      apolloPackageDir.write(buildApolloAndroidPackage())
    }

    setArgs(["install"])

    File installDir = this.project.file(INSTALL_DIR)
    if (!installDir.exists()) {
      installDir.mkdirs()
    } else {
      if (!apolloVersion()?.equals(APOLLOCODEGEN_VERSION)) {
        installDir.deleteDir()
      }
    }
    getOutputs().dir(installDir)
  }

  String apolloVersion() {
    String version = null
    File packageFile = project.file("${INSTALL_DIR}/package.json")
    if (packageFile.isFile()) {
      def input = new JsonSlurper().parseText(packageFile.text)
      version = input.version
    }
    return version
  }

  private String buildApolloAndroidPackage() {
    def builder = new JsonBuilder()

    builder {
      delegate.name 'apollo-android'
      version '0.0.1'
      delegate.description 'Generates Java code based on a GraphQL schema and query documents. Uses apollo-codegen' +
          ' under the hood.'
      delegate.dependencies(
          'apollo-codegen': APOLLOCODEGEN_VERSION
      )
      repository(
          type: 'git',
          url: 'git+https://github.com/apollostack/apollo-android.git'
      )
      author 'Apollo'
      license 'MIT'
    }
    return builder.toPrettyString()
  }
}
