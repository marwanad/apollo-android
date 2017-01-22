package com.apollostack.android.gradle

import com.apollostack.compiler.GraphQLCompiler
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

public class ApolloClassGenTask extends SourceTask {
  static final String NAME = "generate%sApolloClasses"
  @Internal List<GraphQLExtension> config
  @Internal String variant
  @Internal boolean generateClasses
  @OutputDirectory File outputDir

  public void init(String variantName, List<GraphQLExtension> extensionsConfig, boolean genClasses) {
    variant = variantName
    generateClasses = genPojo
    config = extensionsConfig
    group = ApolloPlugin.TASK_GROUP
    description = "Generate Android classes for ${variant.capitalize()} GraphQL queries"
    outputDir = new File("${project.buildDir}/${GraphQLCompiler.OUTPUT_DIRECTORY.join(File.separator)}")
    dependsOn(project.tasks.findByName(String.format(ApolloIRGenTask.NAME, variant.capitalize())))
  }

  @TaskAction void generateClasses(IncrementalTaskInputs inputs) {
    inputs.outOfDate { inputFileDetails ->
      new GraphQLCompiler().write(inputFileDetails.file, outputDir, generateClasses)
    }
  }
}
