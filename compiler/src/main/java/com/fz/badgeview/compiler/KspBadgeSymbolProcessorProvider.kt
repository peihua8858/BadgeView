package com.fz.badgeview.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KspBadgeSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.info("Found symbols, KspBadgeSymbolProcessorProvider: >>>>>>>>>>>>>>create")
        println("Found symbols, KspBadgeSymbolProcessorProvider: >>>>>>>>>>>>>>create")
        return KspBadgeProcessor(environment)
    }
}