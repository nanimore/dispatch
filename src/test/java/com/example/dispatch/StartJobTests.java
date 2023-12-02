package com.example.dispatch;

import com.example.dispatch.utils.SmallTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

//@SpringBootTest
public class StartJobTests {


    @Test
    void test() {
        ArrayList<ArrayList<String>> jobRels = new ArrayList<>();
        jobRels.add(new ArrayList<>(Arrays.asList("A", "B", "0")));
        jobRels.add(new ArrayList<>(Arrays.asList("A", "C", "0")));
        jobRels.add(new ArrayList<>(Arrays.asList("C", "D", "0")));
        jobRels.add(new ArrayList<>(Arrays.asList("D", "E", "0")));
        jobRels.add(new ArrayList<>(Arrays.asList("B", "E", "0")));
        jobRels.add(new ArrayList<>(Arrays.asList("E", "F", "0")));

        List<ArrayList<String>> safeLists = Collections.synchronizedList(jobRels);
        ArrayList<String> result = new ArrayList<>();

        startJob4("A", safeLists, result);

        System.out.println(result);


    }

    public void startJob4(String node, List<ArrayList<String>> jobRels, ArrayList<String> result) {


        // 找到当前节点的父亲
        for (ArrayList<String> jobRel : jobRels) {
            if (Objects.equals(jobRel.get(1), node)) {

                if (!result.contains(jobRel.get(0))) {
                    startJob4(jobRel.get(0), jobRels , result);
                }
            }
        }
        if (!result.contains(node)){
            result.add(node);
        }
        // 找到当前节点的孩子
        for (ArrayList<String> jobRel : jobRels) {
            if (Objects.equals(jobRel.get(0), node)) {

                if (!result.contains(jobRel.get(1))) {
                    startJob4(jobRel.get(1), jobRels , result);
                }
            }
        }

        return;
    }



    public ArrayList<String> startJob3(String node, List<ArrayList<String>> jobRels) {

        // 获取当前节点的父亲和孩子
        ArrayList<String> fatherNodes = new ArrayList<>();
        ArrayList<String> childrenNodes = new ArrayList<>();

        for (ArrayList<String> jobRel : jobRels) {
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(1), node)) {
                fatherNodes.add(jobRel.get(0));
                // 如果已经被执行过，就不用添加了
                for (ArrayList<String> rel : jobRels) {
                    if (rel.get(2) == "1"
                            &&
                            (Objects.equals(rel.get(0), jobRel.get(0)) || Objects.equals(rel.get(1), jobRel.get(0)))) {
                        fatherNodes.remove(jobRel.get(0));
                    }
                }
                jobRel.set(2, "1");
            }
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(0), node)) {
                // 如果已经被执行过，就不用添加了
                childrenNodes.add(jobRel.get(1));

                for (ArrayList<String> rel : jobRels) {
                    if (rel.get(2) == "1"
                            &&
                            (Objects.equals(rel.get(1), jobRel.get(1)) || Objects.equals(rel.get(0), jobRel.get(1)))) {
                        childrenNodes.remove(jobRel.get(1));
                    }
                }
                jobRel.set(2, "1");
            }
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        SmallTool.printTimeAndThread("当前递归线程");
        System.out.println("当前nodes：" + node);
        System.out.println("fatherNodes：" + fatherNodes);
        System.out.println("children：" + childrenNodes);
        System.out.println("jobRels: " + jobRels.toString());
        System.out.println("==========================================================================================");


        ArrayList<String> results = new ArrayList<>();

        for (String fatherNode : fatherNodes) {
            ArrayList<String> result = startJob3(fatherNode, jobRels);
        }

        SmallTool.printTimeAndThread("执行：" + node);
        results.add(node);

        for (String childrenNode : childrenNodes) {
            ArrayList<String> result = startJob3(childrenNode, jobRels);
        }

        return results;
    }


    public CopyOnWriteArrayList<String> startJob2(String node, List<ArrayList<String>> jobRels) {
        // 获取当前节点的父亲
        ArrayList<String> fatherNodes = new ArrayList<>();
        ArrayList<String> childrenNodes = new ArrayList<>();

        for (ArrayList<String> jobRel : jobRels) {
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(1), node)) {
                fatherNodes.add(jobRel.get(0));
                // 如果已经被执行过，就不用添加了
                for (ArrayList<String> rel : jobRels) {
                    if (rel.get(2) == "1"
                            &&
                            (Objects.equals(rel.get(0), jobRel.get(0)) || Objects.equals(rel.get(1), jobRel.get(0)))) {
                        fatherNodes.remove(jobRel.get(0));
                    }
                }
                jobRel.set(2, "1");
            }
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(0), node)) {
                // 如果已经被执行过，就不用添加了
                childrenNodes.add(jobRel.get(1));

                for (ArrayList<String> rel : jobRels) {
                    if (rel.get(2) == "1"
                            &&
                            (Objects.equals(rel.get(1), jobRel.get(1)) || Objects.equals(rel.get(0), jobRel.get(1)))) {
                        childrenNodes.remove(jobRel.get(1));
                    }
                }
                jobRel.set(2, "1");
            }
        }

        synchronized (this) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            SmallTool.printTimeAndThread("当前递归线程");
            System.out.println("当前nodes：" + node);
            System.out.println("fatherNodes：" + fatherNodes);
            System.out.println("children：" + childrenNodes);
            System.out.println("jobRels: " + jobRels.toString());
            System.out.println("==========================================================================================");

        }


        // 开启n个父节点
        CompletableFuture<CopyOnWriteArrayList<String>> cf = CompletableFuture.supplyAsync(() -> {
            // 开启n个父节点

            CopyOnWriteArrayList<String> results = new CopyOnWriteArrayList<>();

            for (String fatherNode : fatherNodes) {
                CopyOnWriteArrayList<String> result = startJob2(fatherNode, jobRels);
                results.addAll(result);
            }

            System.out.println("fatherNode results" + results);
            return results;
        }).thenCompose(results ->
                // 执行当前节点
                CompletableFuture.supplyAsync(() -> {
                    SmallTool.sleepMillis(1000);
                    SmallTool.printTimeAndThread(node);

                    results.add(node);

                    System.out.println("Node results" + results);
                    return results;
                })
        ).thenCompose(results -> CompletableFuture.supplyAsync(() -> {

            // 开启n个子节点
            for (String childrenNode : childrenNodes) {

                CopyOnWriteArrayList<String> result = startJob2(childrenNode, jobRels);
                results.addAll(result);
            }


            System.out.println("childrenNode results" + results);
            return results;
        }));

        return cf.join();
    }

















    public CompletableFuture<CopyOnWriteArrayList<String>> startJob(String node, List<ArrayList<String>> jobRels) {
        // 获取当前节点的父亲
        ArrayList<String> fatherNodes = new ArrayList<>();
        ArrayList<String> childrenNodes = new ArrayList<>();

        for (ArrayList<String> jobRel : jobRels) {
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(1), node)) {
                jobRel.set(2, "1");
                fatherNodes.add(jobRel.get(0));
            }
            if (jobRel.get(2) == "0" && Objects.equals(jobRel.get(0), node)) {
                // 如果已经是被添加为其他节点的孩子，就不用添加了
                childrenNodes.add(jobRel.get(1));

                for (ArrayList<String> rel : jobRels) {
                    if (rel.get(2) == "1" && Objects.equals(rel.get(1), jobRel.get(1))) {
                        childrenNodes.remove(jobRel.get(1));
                    }
                }
                jobRel.set(2, "1");
            }
        }

        // 对集合进行操作
//        for(Iterator<ArrayList<String>> it = jobRels.iterator(); it.hasNext();) {
//            ArrayList<String> jobRel = it.next();
//            if (Objects.equals(jobRel.get(1), node)) {
//                it.remove();
//                fatherNodes.add(jobRel.get(0));
//            }
//        }


        // 对集合进行操作
//        for (Iterator<ArrayList<String>> it = jobRels.iterator(); it.hasNext();) {
//            ArrayList<String> jobRel = it.next();
//            if (Objects.equals(jobRel.get(0), node)) {
//                // 对集合进行操作
//                it.remove();
//                childrenNodes.add(jobRel.get(1));
//            }
//        }
        synchronized (this) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            SmallTool.printTimeAndThread("当前递归线程");
            System.out.println("当前nodes：" + node);
            System.out.println("fatherNodes：" + fatherNodes);
            System.out.println("children：" + childrenNodes);
            System.out.println("jobRels: " + jobRels.toString());
            System.out.println("==========================================================================================");


        }


        // 连接任务
        CompletableFuture<CopyOnWriteArrayList<String>> cf = CompletableFuture.supplyAsync(() -> {
            // 开启n个父节点
            ArrayList<CompletableFuture<CopyOnWriteArrayList<String>>> tasks = new ArrayList<>();
//            ArrayList<String> results = new ArrayList<>();
            CopyOnWriteArrayList<String> results = new CopyOnWriteArrayList<>();

            for (String fatherNode : fatherNodes) {
                Boolean flag = true;
                for (ArrayList<String> jobRel : jobRels) {
                    if (jobRel.get(2) == "1" && Objects.equals(jobRel.get(1), fatherNode)) {
                        flag = false;
                    }
                }
                if (flag) {
                    CompletableFuture<CopyOnWriteArrayList<String>> task = startJob(fatherNode, jobRels);
                    tasks.add(task);
                }

            }

//            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
            SmallTool.sleepMillis(1000);
            for (CompletableFuture<CopyOnWriteArrayList<String>> task : tasks) {

                CopyOnWriteArrayList<String> result = task.join();
                results.addAll(result);
            }

            System.out.println("fatherNode results" + results);
            return results;
        }).thenCompose(results ->
                // 执行当前节点
                CompletableFuture.supplyAsync(() -> {
                    SmallTool.sleepMillis(1000);
                    SmallTool.printTimeAndThread(node);

                    results.add(node);

                    System.out.println("Node results" + results);
                    return results;
                })
        ).thenCompose(results -> CompletableFuture.supplyAsync(() -> {
            // 执行子节点
            ArrayList<CompletableFuture<CopyOnWriteArrayList<String>>> tasks = new ArrayList<>();
            // 开启n个子节点
            for (String childrenNode : childrenNodes) {

                CompletableFuture<CopyOnWriteArrayList<String>> task = startJob(childrenNode, jobRels);
                tasks.add(task);
            }

            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

            for (CompletableFuture<CopyOnWriteArrayList<String>> task : tasks) {

                CopyOnWriteArrayList<String> result = task.join();
                //System.out.println("childrenNode result" + result);
                results.addAll(result);
            }

            System.out.println("childrenNode results" + results);
            return results;
        }));

        return cf;
    }

    @Test
    void test2() {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("任务A");
            SmallTool.sleepMillis(100);
            return "任务A";
        });

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("任务B");
            SmallTool.sleepMillis(100);
            return "任务B";
        });

        CompletableFuture<String> taskC = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("任务C");
            SmallTool.sleepMillis(100);
            return "任务C";
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                taskA,
                taskB,
                taskC
        );

        // 等待所有任务完成
        allTasks.join();

        System.out.println(taskA.join());
        System.out.println(taskB.join());
        System.out.println(taskC.join());
    }


}
