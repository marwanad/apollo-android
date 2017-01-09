package com.apollostack.compiler

import com.apollostack.compiler.ir.FragmentTypeSpecBuilder
import com.apollostack.compiler.ir.OperationIntermediateRepresentation
import com.squareup.javapoet.JavaFile
import com.squareup.moshi.Moshi
import java.io.File

open class GraphQLCompiler {
  private val moshi = Moshi.Builder().build()
  private val irAdapter = moshi.adapter(OperationIntermediateRepresentation::class.java)

  fun write(irFile: File, outputDir: File, generateClasses: Boolean = false) {
    val ir = irAdapter.fromJson(irFile.readText())
    val irPackageName = irFile.absolutePath.formatPackageName()
    val operationTypeBuilders = ir.operations.map {
      OperationTypeSpecBuilder(it, ir.fragments, generateClasses, it.path.formatPackageName(), irPackageName)
    }
    val fragmentTypeBuilders = ir.fragments.map {
      FragmentTypeSpecBuilder(it, generateClasses, "$irPackageName.$FRAGMENT_PACKAGE_PREFIX", irPackageName)
    }
    val typeDeclarationTypeBuilders = ir.typesUsed.map {
      TypeDeclarationTypeSpecBuilder(it, "$irPackageName.$TYPE_PACKAGE_PREFIX", irPackageName)
    }

    (operationTypeBuilders + fragmentTypeBuilders + typeDeclarationTypeBuilders).forEach {
      JavaFile.builder(it.packageName(), it.toTypeSpec()).build().writeTo(outputDir)
    }
  }

  companion object {
    const val FILE_EXTENSION = "graphql"
    val OUTPUT_DIRECTORY = listOf("generated", "source", "apollo")
    val FRAGMENT_PACKAGE_PREFIX = "fragment"
    val TYPE_PACKAGE_PREFIX = "type"
  }
}
