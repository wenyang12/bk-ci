package com.tencent.bk.codecc.task.service

interface OpenSourceTaskService {

    /**
     * 停用代码扫描任务
     */
    fun stopTask(taskId: Long, disableReason: String, userName: String)

    /**
     * 启用代码扫描任务
     */
    fun startTask(taskId : Long, userName: String)
}