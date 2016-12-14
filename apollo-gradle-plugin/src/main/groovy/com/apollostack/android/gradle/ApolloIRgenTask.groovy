package com.apollostack.android.gradle

import com.moowork.gradle.node.task.NodeTask
import groovy.io.FileType
import org.gradle.api.GradleException
import com.google.common.collect.Sets
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles

public class ApolloIRgenTask extends NodeTask {
    static final String NAME = "generate%sApolloIR"

    @InputDirectory
    def sourcesDir
    Collection<ApolloExtension> config

    String variant

    private final static String APOLLO_CODEGEN = "node_modules/apollo-codegen/lib/cli.js"

    @InputFiles
    Collection<File> getInputFiles() {
        Set<File> inputFiles = Sets.newHashSet()
        println "Confugrations" + getConfigurations()
        for (ApolloExtension ext : getConfigurations()) {
            println "found apollo extension $ext"
            for (Map.Entry<String, Collection<String>> entry : config.getFiles().asMap().entrySet()) {
                for (String file : entry.value) {
                    inputFiles.add(new File(entry.key, file))
                }
            }
        }
        return inputFiles
    }

    @Override
    public void exec() {
        File apolloScript = this.project.file(APOLLO_CODEGEN)
        if (!apolloScript.isFile()) {
            throw new GradleException("Apollo-codegen was not found in node_modules. Please run 'gradle ${ApolloCodegenInstallTask.NAME}")
        }
        setScript(apolloScript)
        setWorkingDir(getSourcesDir().absolutePath)

        def queryFiles = getListOfQueryFiles(getSourcesDir().absolutePath)
        def apolloArgs = ["generate",
                          "--schema", "schema.json",
                          "--output", "${variant.capitalize()}API.json", "--target json"]
        apolloArgs.addAll(1, queryFiles)
        setArgs(apolloArgs)
        super.exec()
    }

    def getSourcesDir() {
        return project.file(sourcesDir)
    }
    /**
     * Recursively returns a list with the name of grapghql query files
     * @param path
     * @return - list containing the names of found query files
     */
    static def getListOfQueryFiles(String path) {
        def list = []

        def dir = new File(path)
        dir.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith(".graphql")) {
                list << file.name
            }
        }
        return list
    }
}
