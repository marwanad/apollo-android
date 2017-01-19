package com.apollostack.compiler.ir

import com.apollostack.compiler.SchemaTypeSpecBuilder
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

data class InlineFragment(
    val typeCondition: String,
    val fields: List<Field>,
    val fragmentSpreads: List<String>?
) : CodeGenerator {
  override fun toTypeSpec(fragmentsPkgName: String, typesPkgName: String): TypeSpec =
      TypeSpec.interfaceBuilder(interfaceName())
          .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
          .addMethods(fields.map { it.toMethodSpec() })
          .addTypes(fields.filter(Field::isNonScalar).map { field ->
            SchemaTypeSpecBuilder(field.normalizedName(), field.fields ?: emptyList(), fragmentSpreads ?: emptyList(),
                field.inlineFragments ?: emptyList(), fragmentsPkgName, typesPkgName)
                .build()
          })
          .build()

  fun interfaceName() = "$INTERFACE_PREFIX${typeCondition.capitalize()}"

  companion object {
    private val INTERFACE_PREFIX = "As"
  }
}
