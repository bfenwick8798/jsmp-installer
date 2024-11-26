package io.github.amturtle.tppinstaller

interface ProgressHandler {
    fun prepareNewTaskSet(prepareMessage: String)

    fun newTaskSet(numTasks: Int)

    fun newTask(title: String)

    fun done()
}