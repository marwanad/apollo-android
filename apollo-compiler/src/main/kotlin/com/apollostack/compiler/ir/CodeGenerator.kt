package com.apollostack.compiler.ir

import com.squareup.javapoet.TypeSpec

interface CodeGenerator {
  fun toTypeSpec(fragmentsPkgName: String = "", typesPkgName: String = ""): TypeSpec
}
