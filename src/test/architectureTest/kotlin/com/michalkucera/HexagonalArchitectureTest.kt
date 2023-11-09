package com.michalkucera

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import org.jmolecules.archunit.JMoleculesArchitectureRules
import org.jmolecules.archunit.JMoleculesDddRules

@AnalyzeClasses(packages = ["com.michalkucera"])
class HexagonalArchitectureTest {
    @ArchTest
    val domainDrivenDesignTest = JMoleculesDddRules.all()

    @ArchTest
    val hexagonalArchitectureTest = JMoleculesArchitectureRules.ensureHexagonal()
}
