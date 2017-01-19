package com.apollostack.compiler.ir

import com.apollostack.compiler.convertToPOJO
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class FragmentTypeSpecBuilder(
    val fragment: Fragment,
    val generateClasses: Boolean
) : CodeGenerator {
  override fun toTypeSpec(fragmentsPkgName: String, typesPkgName: String): TypeSpec =
      fragment.toTypeSpec(fragmentsPkgName, typesPkgName).let {
    if (generateClasses) it.convertToPOJO(Modifier.PUBLIC) else it
  }
}
