package io.infinite.speedometer

import io.infinite.carburetor.ast.MetaDataASTNode
import io.infinite.carburetor.ast.MetaDataExpression
import io.infinite.carburetor.ast.MetaDataMethodNode
import io.infinite.carburetor.ast.MetaDataStatement

class ExecutionASTNode {

    ExecutionASTNode parentExecutionASTNode

    ExecutionASTNode parentMethodASTNode

    MetaDataASTNode metaDataASTNode

    Long startTimeMillis = System.currentTimeMillis()

    Long endTimeMillis

    String stackKey

    String codeKey

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

    String computeCodeKey() {
        switch (metaDataASTNode) {
            case MetaDataMethodNode:
                return metaDataASTNode.classSimpleName + "." + metaDataASTNode.methodName + "(...):" + metaDataASTNode.lineNumber + ":" + metaDataASTNode.columnNumber
                break
            case MetaDataExpression:
                return parentMethodASTNode.metaDataASTNode.classSimpleName + "." + parentMethodASTNode.metaDataASTNode.methodName + "(...):" + metaDataASTNode.expressionClassName + ":" + metaDataASTNode.lineNumber + ":" + metaDataASTNode.columnNumber
                break
            case MetaDataStatement:
                return parentMethodASTNode.metaDataASTNode.classSimpleName + "." + parentMethodASTNode.metaDataASTNode.methodName + "(...):" + metaDataASTNode.statementClassName + ":" + metaDataASTNode.lineNumber + ":" + metaDataASTNode.columnNumber
                break
            default:
                return ""
        }
    }

    ExecutionASTNode(ExecutionASTNode parentExecutionASTNode, MetaDataASTNode metaDataASTNode, ExecutionASTNode parentMethodASTNode) {
        this.parentMethodASTNode = parentMethodASTNode
        this.parentExecutionASTNode = parentExecutionASTNode
        this.metaDataASTNode = metaDataASTNode
        this.codeKey = computeCodeKey()
        this.stackKey = (this.parentExecutionASTNode?.stackKey ?: "") + "->" + this.codeKey.hashCode().toString()
    }
}
