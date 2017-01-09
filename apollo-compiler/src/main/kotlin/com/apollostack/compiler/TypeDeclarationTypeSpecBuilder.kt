package com.apollostack.compiler

import com.apollostack.compiler.ir.TypeDeclaration
import com.squareup.javapoet.TypeSpec

class TypeDeclarationTypeSpecBuilder(
    val type: TypeDeclaration,
    val typesPkgName: String,
    val irPkgName: String
) : TypeSpecBuilder {
  override fun packageName(): String {
    return typesPkgName
  }

  override fun toTypeSpec(): TypeSpec = type.toTypeSpec(irPkgName)
}
