package com.apollostack.android.gradle

import com.apollostack.compiler.GraphQLCompiler
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Lists
import com.google.common.collect.Multimap
import org.apache.tools.ant.types.PatternSet
import org.gradle.api.Project

class ApolloExtension {
    public static String NAME = "apollo"
    private final Project project
    private List<String> folders = Lists.newArrayList()
    private Multimap<String, String> cachedFiles

    public ApolloExtension(Project project, String sourceSet) {
        this.project = project
        folders.add("src/$sourceSet/graphql")
    }

    public Multimap<String, String> getFiles() {
        if (cachedFiles != null) {
            return cachedFiles
        }
        PatternSet patternSet = new PatternSet().include("**/*.${GraphQLCompiler.FILE_EXTENSION}")
                .include("**/schema.json")
        def files = ArrayListMultimap.create()
        for (String folder : folders) {
            project.files(folder).getAsFileTree().matching(patternSet).visit { element ->
                if (!element.directory) {
                    files.put(folder, element.relativePath.pathString)
                }
            }
        }
        cachedFiles = files
        return cachedFiles
    }
}
