package com.apollostack.compiler.ir

import com.apollostack.compiler.TypeSpecBuilder
import com.apollostack.compiler.convertToPOJO
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class FragmentTypeSpecBuilder(
    val fragment: Fragment,
    val generateClasses: Boolean,
    val fragmentPkgName: String,
    val irPkgName: String
) : TypeSpecBuilder {
  override fun packageName(): String {
    return fragmentPkgName
  }

  override fun toTypeSpec(): TypeSpec = fragment.toTypeSpec(irPkgName).let {
    if (generateClasses) it.convertToPOJO(Modifier.PUBLIC) else it
  }
}
