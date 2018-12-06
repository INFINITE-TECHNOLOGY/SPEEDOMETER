package io.infinite.speedometer

import io.infinite.carburetor.CarburetorEngine
import io.infinite.carburetor.ast.MetaDataASTNode
import io.infinite.carburetor.ast.MetaDataExpression
import io.infinite.carburetor.ast.MetaDataMethodNode
import io.infinite.carburetor.ast.MetaDataStatement
import io.infinite.speedometer.ast.ExecutionASTNode
import io.infinite.speedometer.stat.Stat

class SpeedometerEngine extends CarburetorEngine {

    ExecutionASTNode executionASTNode

    Map<String, Stat> cumulativeStatMap = [:]
    Map<String, Stat> standaloneStatMap = [:]

    static Object statWritingLock = new Object()

    static ThreadLocal engineThreadLocal = new ThreadLocal()

    static void printStats(Map<String, Stat> statMap) {
        for (key in statMap.keySet()) {
            System.out.println(key + statMap.get(key).count + ";" + statMap.get(key).duration)
        }
    }

    static CarburetorEngine getInstance() {
        SpeedometerEngine speedometerEngine = engineThreadLocal.get() as SpeedometerEngine
        if (speedometerEngine == null) {
            speedometerEngine = new SpeedometerEngine()
            engineThreadLocal.set(speedometerEngine)
        }
        return speedometerEngine
    }

    void printStats() {
        System.out.println("Stats for thread " + Thread.currentThread().getName() + ":")
        System.out.println("Cumulative stats:")
        printStats(cumulativeStatMap)
        System.out.println("Standalone stats:")
        printStats(standaloneStatMap)
    }

    @Override
    Closure getShutdownHook() {
        return {
            synchronized (statWritingLock) {
                printStats()
            }
        }
    }

    static void updateStatsCount(Map<String, Stat> statMap, String key) {
        if (statMap.containsKey(key)) {
            statMap.get(key).count = statMap.get(key).count + 1
        } else {
            statMap.put(key, new Stat())
        }
    }

    static void updateStatsTime(Map<String, Stat> statMap, String key, Long elapsedTime) {
        if (statMap.containsKey(key)) {
            statMap.get(key).duration = elapsedTime
        } else {
            statMap.put(key, new Stat())
        }
    }

    void addExecutionASTNode(MetaDataASTNode metaDataASTNode) {
        ExecutionASTNode newExecutionASTNode = new ExecutionASTNode(this.executionASTNode, metaDataASTNode)
        executionASTNode = newExecutionASTNode
        updateStatsCount(cumulativeStatMap, newExecutionASTNode.getCumulativeKey())
        updateStatsCount(standaloneStatMap, newExecutionASTNode.getStandaloneKey())
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
        updateStatsTime(cumulativeStatMap, executionASTNode.getCumulativeKey(), executionASTNode.getElapsedTime())
        updateStatsTime(standaloneStatMap, executionASTNode.getStandaloneKey(), executionASTNode.getElapsedTime())
        executionASTNode = executionASTNode.getParentExecutionASTNode()
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
