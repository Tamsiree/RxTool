package com.tamsiree.rxkit

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 * Shell相关工具类
 */
object RxShellTool {
    const val COMMAND_SU = "su"
    const val COMMAND_SH = "sh"
    const val COMMAND_EXIT = "exit\n"
    const val COMMAND_LINE_END = "\n"

    /**
     * 判断设备是否root
     * @return `true`: root<br></br>`false`: 没root
     */
    val isRoot: Boolean
        get() = execCmd("echo root", true, false).result == 0

    /**
     * 是否是在root下执行命令
     *
     * @param command 命令
     * @param isRoot  是否root
     * @return CommandResult
     */
    @JvmStatic
    fun execCmd(command: String, isRoot: Boolean): CommandResult {
        return execCmd(arrayOf(command), isRoot, true)
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands 多条命令链表
     * @param isRoot   是否root
     * @return CommandResult
     */
    @JvmStatic
    fun execCmd(commands: List<String>?, isRoot: Boolean): CommandResult {
        return execCmd(commands?.toTypedArray(), isRoot, true)
    }

    /**
     * 是否是在root下执行命令
     *
     * @param command         命令
     * @param isRoot          是否root
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    @JvmStatic
    fun execCmd(command: String, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        return execCmd(arrayOf(command), isRoot, isNeedResultMsg)
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands        命令链表
     * @param isRoot          是否root
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    @JvmStatic
    fun execCmd(commands: List<String>?, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        return execCmd(commands?.toTypedArray(), isRoot, isNeedResultMsg)
    }
    /**
     * 是否是在root下执行命令
     *
     * @param commands        命令数组
     * @param isRoot          是否root
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    /**
     * 是否是在root下执行命令
     *
     * @param commands 多条命令数组
     * @param isRoot   是否root
     * @return CommandResult
     */
    @JvmOverloads
    @JvmStatic
    fun execCmd(commands: Array<String>?, isRoot: Boolean, isNeedResultMsg: Boolean = true): CommandResult {
        var result = -1
        if (commands == null || commands.size == 0) {
            return CommandResult(result, null, null)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(if (isRoot) COMMAND_SU else COMMAND_SH)
            os = DataOutputStream(process.outputStream)
            for (command in commands) {
                if (command == null) {
                    continue
                }
                os.write(command.toByteArray())
                os.writeBytes(COMMAND_LINE_END)
                os.flush()
            }
            os.writeBytes(COMMAND_EXIT)
            os.flush()
            result = process.waitFor()
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                errorMsg = StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String?
                while (successResult.readLine().also { s = it } != null) {
                    successMsg.append(s)
                }
                while (errorResult.readLine().also { s = it } != null) {
                    errorMsg.append(s)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return CommandResult(result, successMsg?.toString(), errorMsg?.toString())
    }

    /**
     * 返回的命令结果
     */
    class CommandResult {
        /**
         * 结果码
         */
        var result: Int

        /**
         * 成功的信息
         */
        var successMsg: String? = null

        /**
         * 错误信息
         */
        var errorMsg: String? = null

        constructor(result: Int) {
            this.result = result
        }

        constructor(result: Int, successMsg: String?, errorMsg: String?) {
            this.result = result
            this.successMsg = successMsg
            this.errorMsg = errorMsg
        }
    }
}