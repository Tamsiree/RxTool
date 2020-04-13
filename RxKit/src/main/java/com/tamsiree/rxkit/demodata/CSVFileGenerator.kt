package com.tamsiree.rxkit.demodata

import com.google.common.base.Joiner
import com.google.common.collect.Lists
import com.google.common.io.Files
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

object CSVFileGenerator {
    private val LINE_SEPERATOR = System
            .getProperty("line.separator")
    private val charset = StandardCharsets.UTF_8
    fun generate(data: List<HashMap<String?, Any?>>,
                 columns: Array<String?>,
                 fileName: String?) {
        val file = File(fileName)
        if (file.exists()) {
            file.delete()
        }
        for (objects in data) {
            val result: MutableList<String?> = Lists.newArrayList()
            for (column in columns) {
                if (objects[column] != null) {
                    result.add(objects[column].toString())
                } else {
                    result.add("")
                }
            }
            val lineData = Joiner.on(",").skipNulls().join(result)
            try {
                Files.append(lineData + LINE_SEPERATOR, file, charset)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}