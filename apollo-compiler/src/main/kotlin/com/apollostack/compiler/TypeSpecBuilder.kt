package com.apollostack.compiler

import com.squareup.javapoet.TypeSpec

interface TypeSpecBuilder {
  fun packageName(): String
  fun toTypeSpec(): TypeSpec
}
