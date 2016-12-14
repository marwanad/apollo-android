package com.apollostack.android.gradle

import com.android.build.gradle.api.BaseVariant
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.NodePlugin
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

class ApolloPlugin implements Plugin<Project> {
  private static final String NODE_VERSION = "6.7.0"
  public static final String TASK_GROUP = "apollo"

  @Override
  void apply(Project project) {
    setupNode(project)
    if (project.plugins.hasPlugin("com.android.application")) {
      configureAndroid(project,
          (DomainObjectCollection<BaseVariant>) project.android.applicationVariants)
    } else if (project.plugins.hasPlugin("com.android.library")) {
      configureAndroid(project,
          (DomainObjectCollection<BaseVariant>) project.android.libraryVariants)
    }
  }

  private static void configureAndroid(Project project, DomainObjectCollection<BaseVariant> variants) {
    project.tasks.create(ApolloCodegenInstallTask.NAME, ApolloCodegenInstallTask.class)
  }

  private static void setupNode(Project project) {
    project.plugins.apply NodePlugin
    NodeExtension nodeConfig = project.extensions.findByName("node") as NodeExtension
    nodeConfig.download = true
    nodeConfig.version = NODE_VERSION
  }
}

