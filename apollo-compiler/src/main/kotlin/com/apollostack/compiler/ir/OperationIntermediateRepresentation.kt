package com.apollostack.compiler.ir

import com.apollostack.compiler.ir.Fragment
import com.apollostack.compiler.ir.Operation
import com.apollostack.compiler.ir.TypeDeclaration

data class OperationIntermediateRepresentation(
    val operations: List<Operation>,
    val fragments: List<Fragment>,
    val typesUsed: List<TypeDeclaration>
)
