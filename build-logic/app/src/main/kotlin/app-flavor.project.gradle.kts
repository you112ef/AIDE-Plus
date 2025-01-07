
afterEvaluate {
    var taskDefaultName: String?

    tasks.configureEach {
        if (name.startsWith("install")) {

        }

        if (name.contains("mergeDex")) {
            taskDefaultName = name.removePrefix("mergeDex")
            println("开始添加Dex")
            println(taskDefaultName)
        }
    }
}