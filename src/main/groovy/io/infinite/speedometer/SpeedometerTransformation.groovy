package io.infinite.speedometer

import groovy.transform.ToString
import groovy.util.logging.Slf4j
import io.infinite.carburetor.CarburetorTransformation
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.transform.GroovyASTTransformation

@ToString(includeNames = true, includeFields = true, includePackage = false)
@GroovyASTTransformation(
        phase = CompilePhase.SEMANTIC_ANALYSIS
)
@Slf4j
class SpeedometerTransformation extends CarburetorTransformation {

    @Override
    String getEngineVarName() {
        return "speedometerEngine"
    }

    @Override
    Statement createEngineDeclaration() {
        return GeneralUtils.declS(
                GeneralUtils.varX(getEngineVarName(), ClassHelper.make(SpeedometerEngine.class)),
                GeneralUtils.callX(ClassHelper.make(SpeedometerEngine.class), "getInstance")
        )
    }
}
