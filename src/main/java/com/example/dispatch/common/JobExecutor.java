package com.example.dispatch.common;


/**
 * 任务执行器类型枚举
 *
 */
public enum JobExecutor {

    SQL("SQL"),
    JAVA("JAVA"),
    PYTHON("PYTHON"),
    GO("GO"),
    C("C"),
    CSHARP("C#"),
    JAVASCRIPT("JavaScript"),
    PHP("PHP"),
    RUBY("Ruby"),
    RUST("Rust"),
    SWIFT("Swift"),
    TYPESCRIPT("TypeScript"),
    VUE("Vue");


    private final String executor;

    JobExecutor(String executor) {
        this.executor = executor;
    }

    public String getExecutor() {
        return executor;
    }

}
