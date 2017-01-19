package com.apollostack.compiler

import com.apollostack.compiler.ir.CodeGenerator
import com.apollostack.compiler.ir.TypeDeclaration
import com.squareup.javapoet.TypeSpec

class TypeDeclarationTypeSpecBuilder(
    val type: TypeDeclaration
) : CodeGenerator {
  override fun toTypeSpec(fragmentsPkgName: String, typesPkgName: String): TypeSpec = type.toTypeSpec()
}
