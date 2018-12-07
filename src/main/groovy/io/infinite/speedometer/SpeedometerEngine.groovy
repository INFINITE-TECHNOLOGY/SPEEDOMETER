package io.infinite.speedometer

import groovy.sql.Sql
import io.infinite.carburetor.CarburetorEngine
import io.infinite.carburetor.ast.MetaDataASTNode
import io.infinite.carburetor.ast.MetaDataExpression
import io.infinite.carburetor.ast.MetaDataMethodNode
import io.infinite.carburetor.ast.MetaDataStatement

class SpeedometerEngine extends CarburetorEngine {

    ExecutionASTNode executionASTNode

    ExecutionASTNode executionMethodASTNode

    List<ExecutionASTNode> executionASTNodes = []

    static Object printLock = new Object()

    static Sql sql = Sql.newInstance("jdbc:h2:mem:SPEEDOMETER;DB_CLOSE_ON_EXIT=FALSE", "sa", "", "org.h2.Driver")

    static getInstance() {
        return SpeedometerFactory.getInstance()
    }

    static {
        sql.getConnection().setAutoCommit(false)
        sql.execute("""create table EXECUTIONASTNODES (threadName varchar(128), standaloneKeyHash varchar(16), cumulativeKey varchar(4000), elapsedTime integer, standaloneKey varchar(4000))""")
    }

    void saveStats() {
        executionASTNodes.each {
            sql.execute("insert into EXECUTIONASTNODES values (?, ?, ?, ?, ?)", [Thread.currentThread().getName(), it.standaloneKeyHash, it.cumulativeKey, it.getElapsedTime(), it.standaloneKey])
        }
        sql.commit()
    }

    void printStats() {
        println("Stats for thread: " + Thread.currentThread().getName())
        sql.eachRow("select count(*) cc, sum(elapsedTime) t, standaloneKeyHash, cumulativeKey, standaloneKey from EXECUTIONASTNODES where threadName='${Thread.currentThread().getName()}' group by standaloneKeyHash, cumulativeKey, standaloneKey order by 2 desc limit 10") {
            println(it)
        }
    }

    SpeedometerEngine() {
        addShutdownHook {
            while (executionASTNode != null) {
                executionClose()
            }
            saveStats()
            synchronized (printLock) {
                printStats()
            }
        }
    }

    void addExecutionASTNode(MetaDataASTNode metaDataASTNode) {
        if (metaDataASTNode instanceof MetaDataMethodNode) {
            executionMethodASTNode = new ExecutionASTNode(this.executionASTNode, metaDataASTNode, executionMethodASTNode)
        }
        ExecutionASTNode newExecutionASTNode = new ExecutionASTNode(this.executionASTNode, metaDataASTNode, executionMethodASTNode)
        executionASTNode = newExecutionASTNode
        executionASTNodes.add(executionASTNode)
    }

    @Override
    void expressionExecutionOpen(MetaDataExpression metaDataExpression) {
        addExecutionASTNode(metaDataExpression)
    }

    @Override
    Object handleExpressionEvaluationResult(Object expressionEvaluationResult) {
        return expressionEvaluationResult
    }

    @Override
    void statementExecutionOpen(MetaDataStatement metaDataStatement) {
        addExecutionASTNode(metaDataStatement)
    }

    @Override
    void methodExecutionOpen(MetaDataMethodNode metaDataMethodNode, Map<String, Object> methodArgumentMap) {
        addExecutionASTNode(metaDataMethodNode)
    }

    @Override
    void executionClose() {
        executionASTNode.stopTiming()
        executionASTNode = executionASTNode.getParentExecutionASTNode()
        if (executionASTNode?.metaDataASTNode instanceof MetaDataMethodNode) {
            executionMethodASTNode = executionASTNode
        }
    }

    @Override
    void handleControlStatement(String controlStatementClassName) {
        switch (controlStatementClassName) {
            case "ReturnStatement":
                while (!(executionASTNode.metaDataASTNode instanceof MetaDataMethodNode || (executionASTNode.metaDataASTNode instanceof MetaDataExpression && executionASTNode.metaDataASTNode.getExpressionClassName() == "ClosureExpression"))) {
                    executionClose()
                }
                break
            case "BreakStatement":
                while (!(executionASTNode.metaDataASTNode instanceof MetaDataStatement && ["DoWhileStatement", "ForStatement", "WhileStatement", "SwitchStatement"].contains(executionASTNode.metaDataASTNode.getStatementClassName()))) {
                    executionClose()
                }
                break
            case "ContinueStatement":
                while (!(executionASTNode.metaDataASTNode instanceof MetaDataStatement && ["DoWhileStatement", "ForStatement", "WhileStatement"].contains(executionASTNode.metaDataASTNode.getStatementClassName()))) {
                    executionClose()
                }
                break
            case "ThrowStatement":
                while (!(executionASTNode.metaDataASTNode instanceof MetaDataMethodNode || (executionASTNode.metaDataASTNode instanceof MetaDataStatement && executionASTNode.metaDataASTNode.getStatementClassName() == "TryCatchStatement"))) {
                    executionClose()
                }
                break
        }
    }

    @Override
    void handleMethodResult(Object methodResult) {
        //ignore
    }

    @Override
    void exception(Exception e) {
        //ignore
    }
}
