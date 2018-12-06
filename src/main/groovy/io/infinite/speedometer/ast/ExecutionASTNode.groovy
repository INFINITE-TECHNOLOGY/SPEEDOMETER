package io.infinite.speedometer.ast

import io.infinite.carburetor.ast.MetaDataASTNode
import io.infinite.carburetor.ast.MetaDataExpression
import io.infinite.carburetor.ast.MetaDataMethodNode
import io.infinite.carburetor.ast.MetaDataStatement

class ExecutionASTNode {

    ExecutionASTNode parentExecutionASTNode

    MetaDataASTNode metaDataASTNode

    Long startTimeMillis = System.currentTimeMillis()

    Long endTimeMillis

    void stopTiming() {
        if (endTimeMillis == null) {
            endTimeMillis = System.currentTimeMillis()
        }
    }

    Long getElapsedTime() {
        if (startTimeMillis == null) {
            return null as Long
        }
        stopTiming()
        return endTimeMillis - startTimeMillis
    }

    String getCumulativeKey() {
        ExecutionASTNode lookupParentNode = parentExecutionASTNode
        String key = ""
        while (lookupParentNode != null) {
            key += "->"
            key += lookupParentNode.getStandaloneKey()
            lookupParentNode = lookupParentNode.getParentExecutionASTNode()
        }
        key += getStandaloneKey()
        return key
    }

    String getStandaloneKey() {
        switch (metaDataASTNode) {
            case MetaDataMethodNode:
                return metaDataASTNode.className + "." + metaDataASTNode.methodName + "(...):" + metaDataASTNode.lineNumber
                break
            case MetaDataExpression:
                return metaDataASTNode.expressionClassName + ":" + metaDataASTNode.lineNumber
                break
            case MetaDataStatement:
                return metaDataASTNode.statementClassName + ":" + metaDataASTNode.lineNumber
                break
            default:
                return ""
        }
    }

    ExecutionASTNode(ExecutionASTNode parentExecutionASTNode, MetaDataASTNode metaDataASTNode) {
        this.parentExecutionASTNode = parentExecutionASTNode
        this.metaDataASTNode = metaDataASTNode
    }
}
