package com.tamsiree.rxkit

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.tamsiree.rxkit.RxConstTool.KB
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxFileTool.Companion.closeIO
import com.tamsiree.rxkit.RxFileTool.Companion.getFileByPath
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.model.FileHeader
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * @author tamsiree
 * @date 2016/1/24
 * 压缩相关工具类
 */
object RxZipTool {
    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件集合
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件集合
     * @param zipFilePath 压缩文件路径
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    @JvmStatic
    fun zipFiles(resFiles: Collection<File>?, zipFilePath: String?, comment: String? = null): Boolean {
        return zipFiles(resFiles, getFileByPath(zipFilePath), comment)
    }
    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @param comment  压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    @JvmStatic
    fun zipFiles(resFiles: Collection<File>?, zipFile: File?, comment: String? = null): Boolean {
        if (resFiles == null || zipFile == null) {
            return false
        }
        var zos: ZipOutputStream? = null
        return try {
            zos = ZipOutputStream(FileOutputStream(zipFile))
            for (resFile in resFiles) {
                if (!zipFile(resFile, "", zos, comment)) {
                    return false
                }
            }
            true
        } finally {
            if (zos != null) {
                zos.finish()
                closeIO(zos)
            }
        }
    }
    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    @JvmStatic
    fun zipFile(resFilePath: String?, zipFilePath: String?, comment: String? = null): Boolean {
        return zipFile(getFileByPath(resFilePath), getFileByPath(zipFilePath), comment)
    }
    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @param comment 压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    @JvmStatic
    fun zipFile(resFile: File?, zipFile: File?, comment: String? = null): Boolean {
        if (resFile == null || zipFile == null) {
            return false
        }
        var zos: ZipOutputStream? = null
        return try {
            zos = ZipOutputStream(FileOutputStream(zipFile))
            zipFile(resFile, "", zos, comment)
        } finally {
            if (zos != null) {
                zos.finish()
                closeIO(zos)
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param resFile  待压缩文件
     * @param rootPath 相对于压缩文件的路径
     * @param zos      压缩文件输出流
     * @param comment  压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    private fun zipFile(resFile: File, rootPath: String, zos: ZipOutputStream, comment: String?): Boolean {
        var rootPath = rootPath
        rootPath = rootPath + (if (isNullString(rootPath)) "" else File.separator) + resFile.name
        if (resFile.isDirectory) {
            val fileList = resFile.listFiles()
            // 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
            if (fileList.size <= 0) {
                val entry = ZipEntry("$rootPath/")
                if (!isNullString(comment)) {
                    entry.comment = comment
                }
                zos.putNextEntry(entry)
                zos.closeEntry()
            } else {
                for (file in fileList) {
                    // 如果递归返回false则返回false
                    if (!zipFile(file, rootPath, zos, comment)) {
                        return false
                    }
                }
            }
        } else {
            var `is`: InputStream? = null
            try {
                `is` = BufferedInputStream(FileInputStream(resFile))
                val entry = ZipEntry(rootPath)
                if (!isNullString(comment)) {
                    entry.comment = comment
                }
                zos.putNextEntry(entry)
                val buffer = ByteArray(KB)
                var len: Int
                while (`is`.read(buffer, 0, KB).also { len = it } != -1) {
                    zos.write(buffer, 0, len)
                }
                zos.closeEntry()
            } finally {
                closeIO(`is`)
            }
        }
        return true
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles    压缩文件集合
     * @param destDirPath 目标目录路径
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @JvmStatic
    fun unzipFiles(zipFiles: Collection<File?>?, destDirPath: String?): Boolean {
        return unzipFiles(zipFiles, getFileByPath(destDirPath))
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles 压缩文件集合
     * @param destDir  目标目录
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @JvmStatic
    fun unzipFiles(zipFiles: Collection<File?>?, destDir: File?): Boolean {
        if (zipFiles == null || destDir == null) {
            return false
        }
        for (zipFile in zipFiles) {
            if (!unzipFile(zipFile, destDir)) {
                return false
            }
        }
        return true
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @JvmStatic
    fun unzipFile(zipFilePath: String?, destDirPath: String?): Boolean {
        return unzipFile(getFileByPath(zipFilePath), getFileByPath(destDirPath))
    }

    /**
     * 解压文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @JvmStatic
    fun unzipFile(zipFile: File?, destDir: File?): Boolean {
        return unzipFileByKeyword(zipFile, destDir, null) != null
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    @JvmStatic
    fun unzipFileByKeyword(zipFilePath: String?, destDirPath: String?, keyword: String?): List<File>? {
        return unzipFileByKeyword(getFileByPath(zipFilePath),
                getFileByPath(destDirPath), keyword)
    }

    /**
     * 根据所给密码解压zip压缩包到指定目录
     *
     *
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zipFile zip压缩包绝对路径
     * @param destDir 指定解压文件夹位置
     * @param passwd  密码(可为空)
     * @return 解压后的文件数组
     * @throws ZipException
     */
    @JvmStatic
    fun unzipFileByKeyword(zipFile: File?, destDir: File?, passwd: String?): List<File>? {
        return try {
            //1.判断指定目录是否存在
            if (zipFile == null) {
                throw ZipException("压缩文件不存在.")
            }
            if (destDir == null) {
                throw ZipException("解压缩路径不存在.")
            }
            if (destDir.isDirectory && !destDir.exists()) {
                destDir.mkdir()
            }

            //2.初始化zip工具
            val zFile = ZipFile(zipFile)
            zFile.setFileNameCharset("UTF-8")
            if (!zFile.isValidZipFile) {
                throw ZipException("压缩文件不合法,可能被损坏.")
            }
            //3.判断是否已加密
            if (zFile.isEncrypted) {
                zFile.setPassword(passwd!!.toCharArray())
            }
            //4.解压所有文件
            zFile.extractAll(destDir.absolutePath)
            val headerList: MutableList<FileHeader?> = zFile.fileHeaders as MutableList<FileHeader?>
            val extractedFileList: MutableList<File> = ArrayList()
            for (fileHeader in headerList) {
                if (!fileHeader!!.isDirectory) {
                    extractedFileList.add(File(destDir, fileHeader.fileName))
                }
            }
            extractedFileList
        } catch (e: ZipException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getFilesPath(zipFilePath: String?): List<String>? {
        return getFilesPath(getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getFilesPath(zipFile: File?): List<String>? {
        if (zipFile == null) {
            return null
        }
        val paths: MutableList<String> = ArrayList()
        val entries = getEntries(zipFile)
        while (entries!!.hasMoreElements()) {
            paths.add((entries.nextElement() as ZipEntry).name)
        }
        return paths
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getComments(zipFilePath: String?): List<String>? {
        return getComments(getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getComments(zipFile: File?): List<String>? {
        if (zipFile == null) {
            return null
        }
        val comments: MutableList<String> = ArrayList()
        val entries = getEntries(zipFile)
        while (entries!!.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            comments.add(entry.comment)
        }
        return comments
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getEntries(zipFilePath: String?): Enumeration<*>? {
        return getEntries(getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getEntries(zipFile: File?): Enumeration<*>? {
        return if (zipFile == null) {
            null
        } else java.util.zip.ZipFile(zipFile).entries()
    }
    //----------------------------------------加密压缩------------------------------------------------
    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    @JvmStatic
    fun fileToZip(sourceFilePath: String, zipFilePath: String, fileName: String): Boolean {
        var flag = false
        val sourceFile = File(sourceFilePath)
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var zos: ZipOutputStream? = null
        if (sourceFile.exists()) {
            try {
                val zipFile = File("$zipFilePath/$fileName.zip")
                if (zipFile.exists()) {
                    println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.")
                } else {
                    val sourceFiles = sourceFile.listFiles()
                    if (null == sourceFiles || sourceFiles.size < 1) {
                        println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.")
                    } else {
                        fos = FileOutputStream(zipFile)
                        zos = ZipOutputStream(BufferedOutputStream(fos))
                        val bufs = ByteArray(1024 * 10)
                        for (i in sourceFiles.indices) {
                            //创建ZIP实体，并添加进压缩包
                            val zipEntry = ZipEntry(sourceFiles[i].name)
                            //zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = FileInputStream(sourceFiles[i])
                            bis = BufferedInputStream(fis, 1024 * 10)
                            var read = 0
                            while (bis.read(bufs, 0, 1024 * 10).also { read = it } != -1) {
                                zos.write(bufs, 0, read)
                            }
                        }
                        flag = true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException(e)
            } finally {
                //关闭流
                try {
                    bis?.close()
                    zos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException(e)
                }
            }
        } else {
            println("待压缩的文件目录：" + sourceFilePath + "不存在.")
        }
        return flag
    }

    @JvmStatic
    fun zipEncrypt(src: String?, dest: String, isCreateDir: Boolean, passwd: String): String? {
        var dest = dest
        val srcFile = File(src)
        dest = buildDestinationZipFilePath(srcFile, dest)
        val parameters = ZipParameters()
        // 压缩方式
        parameters.compressionMethod = Zip4jConstants.COMP_DEFLATE
        // 压缩级别
        parameters.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_NORMAL
        if (!isNullString(passwd)) {
            parameters.isEncryptFiles = true
            // 加密方式
            parameters.encryptionMethod = Zip4jConstants.ENC_METHOD_STANDARD
            parameters.password = passwd.toCharArray()
        }
        try {
            val zipFile = ZipFile(dest)
            if (srcFile.isDirectory) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    val subFiles = srcFile.listFiles()
                    val temp = ArrayList<File>()
                    Collections.addAll(temp, *subFiles)
                    zipFile.addFiles(temp, parameters)
                    return dest
                }
                zipFile.addFolder(srcFile, parameters)
            } else {
                zipFile.addFile(srcFile, parameters)
            }
            return dest
        } catch (e: ZipException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 构建压缩文件存放路径,如果不存在将会创建
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile   源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private fun buildDestinationZipFilePath(srcFile: File, destParam: String): String {
        var destParam = destParam
        if (isNullString(destParam)) {
            destParam = if (srcFile.isDirectory) {
                srcFile.parent + File.separator + srcFile.name + ".zip"
            } else {
                val fileName = srcFile.name.substring(0, srcFile.name.lastIndexOf("."))
                srcFile.parent + File.separator + fileName + ".zip"
            }
        } else {
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam)
            if (destParam.endsWith(File.separator)) {
                var fileName = ""
                fileName = if (srcFile.isDirectory) {
                    srcFile.name
                } else {
                    srcFile.name.substring(0, srcFile.name.lastIndexOf("."))
                }
                destParam += "$fileName.zip"
            }
        }
        return destParam
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private fun createDestDirectoryIfNecessary(destParam: String) {
        var destDir: File? = null
        destDir = if (destParam.endsWith(File.separator)) {
            File(destParam)
        } else {
            File(destParam.substring(0, destParam.lastIndexOf(File.separator)))
        }
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
    }

    @JvmStatic
    fun zipEncryptRargo(src: String?, dest: String, isCreateDir: Boolean, passwd: String, unit: Int): String? {
        var dest = dest
        val srcFile = File(src)
        dest = buildDestinationZipFilePath(srcFile, dest)
        val parameters = ZipParameters()
        // 默认COMP_DEFLATE
        parameters.compressionMethod = Zip4jConstants.COMP_DEFLATE
        parameters.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_NORMAL
        if (!isNullString(passwd)) {
            parameters.isEncryptFiles = true
            parameters.encryptionMethod = 0
            parameters.password = passwd.toCharArray()
        }
        return try {
            val zipFile = ZipFile(dest)
            if (srcFile.isDirectory) {
                if (!isCreateDir) {
                    val subFiles = srcFile.listFiles()
                    val temp: ArrayList<File?> = ArrayList()
                    Collections.addAll(temp, *subFiles)
                    //                    zipFile.addFiles(temp, parameters);
                    zipFile.createZipFile(temp, parameters, true, unit * 1000.toLong())
                    return dest
                }
                zipFile.createZipFileFromFolder(srcFile, parameters, true, unit * 1000.toLong())
                //粗略的算一下分成多少份，获取的大小比实际的大点（一般是准确的）
                val partsize = zipInfo(dest).toInt() / unit //65536byte=64kb
                println("分割成功！总共分割成了" + (partsize + 1) + "个文件！")
            } else {
                zipFile.createZipFile(srcFile, parameters, true, unit * 1000.toLong())
            }
            dest
        } catch (var9: ZipException) {
            var9.printStackTrace()
            null
        }
    }

    // 预览压缩文件信息
    @Throws(ZipException::class)
    @JvmStatic
    fun zipInfo(zipFile: String?): Double {
        val zip = ZipFile(zipFile)
        zip.setFileNameCharset("GBK")
        val list: MutableList<FileHeader?> = zip.fileHeaders as MutableList<FileHeader?>
        var zipCompressedSize: Long = 0
        for (head in list) {
            zipCompressedSize += head!!.compressedSize
            //      System.out.println(zipFile+"文件相关信息如下：");
            //      System.out.println("Name: "+head.getFileName());
            //      System.out.println("Compressed Size:"+(head.getCompressedSize()/1.0/1024)+"kb");
            //      System.out.println("Uncompressed Size:"+(head.getUncompressedSize()/1.0/1024)+"kb");
            //      System.out.println("CRC32:"+head.getCrc32());
            //      System.out.println("*************************************");
        }
        return zipCompressedSize / 1.0 / 1024
    }

    /**
     * 删除ZIP文件内的文件夹
     *
     * @param file
     * @param removeDir
     */
    @JvmStatic
    fun removeDirFromZipArchive(file: String?, removeDir: String): Boolean {
        var removeDir = removeDir
        return try {
            // 创建ZipFile并设置编码
            val zipFile = ZipFile(file)
            zipFile.setFileNameCharset("GBK")

            // 给要删除的目录加上路径分隔符
            if (!removeDir.endsWith(File.separator)) {
                removeDir += File.separator
            }

            // 如果目录不存在, 直接返回
            val dirHeader = zipFile.getFileHeader(removeDir) ?: return false

            // 遍历压缩文件中所有的FileHeader, 将指定删除目录下的子文件名保存起来
            val headersList = zipFile.fileHeaders
            val removeHeaderNames: MutableList<String> = ArrayList()
            var i = 0
            val len = headersList.size
            while (i < len) {
                val subHeader = headersList[i] as FileHeader
                if (subHeader.fileName.startsWith(dirHeader.fileName)
                        && subHeader.fileName != dirHeader.fileName) {
                    removeHeaderNames.add(subHeader.fileName)
                }
                i++
            }
            // 遍历删除指定目录下的所有子文件, 最后删除指定目录(此时已为空目录)
            for (headerNameString in removeHeaderNames) {
                zipFile.removeFile(headerNameString)
            }
            zipFile.removeFile(dirHeader)
            true
        } catch (e: ZipException) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun Unzip(zipFile: File, dest: String?, passwd: String,
              charset: String?, handler: Handler?, isDeleteZipFile: Boolean) {
        var charset = charset
        try {
            val zFile = ZipFile(zipFile)
            if (TextUtils.isEmpty(charset)) {
                charset = "UTF-8"
            }
            zFile.setFileNameCharset(charset)
            if (!zFile.isValidZipFile) {
                throw ZipException(
                        "Compressed files are not illegal, may be damaged.")
            }
            val destDir = File(dest) // Unzip directory
            if (destDir.isDirectory && !destDir.exists()) {
                destDir.mkdir()
            }
            if (zFile.isEncrypted) {
                zFile.setPassword(passwd.toCharArray())
            }
            val progressMonitor = zFile.progressMonitor
            val progressThread = Thread(Runnable {
                var bundle: Bundle? = null
                var msg: Message? = null
                try {
                    var percentDone = 0
                    // long workCompleted=0;
                    // handler.sendEmptyMessage(ProgressMonitor.RESULT_SUCCESS)
                    if (handler == null) {
                        return@Runnable
                    }
                    handler.sendEmptyMessage(CompressStatus.START)
                    while (true) {
                        Thread.sleep(1000)
                        percentDone = progressMonitor.percentDone
                        bundle = Bundle()
                        bundle.putInt(CompressKeys.PERCENT, percentDone)
                        msg = Message()
                        msg.what = CompressStatus.HANDLING
                        msg.data = bundle
                        handler.sendMessage(msg)
                        if (percentDone >= 100) {
                            break
                        }
                    }
                    handler.sendEmptyMessage(CompressStatus.COMPLETED)
                } catch (e: InterruptedException) {
                    bundle = Bundle()
                    bundle.putString(CompressKeys.ERROR, e.message)
                    msg = Message()
                    msg.what = CompressStatus.ERROR
                    msg.data = bundle
                    handler!!.sendMessage(msg)
                    e.printStackTrace()
                } finally {
                    if (isDeleteZipFile) {
                        zipFile.deleteOnExit() //zipFile.delete();
                    }
                }
            })
            progressThread.start()
            zFile.isRunInThread = true
            zFile.extractAll(dest)
        } catch (e: ZipException) {
            e.printStackTrace()
        }
    }

    object CompressStatus {
        const val START = 0
        const val HANDLING = 1
        const val COMPLETED = 2
        const val ERROR = 3
    }

    object CompressKeys {
        const val PERCENT = "PERCENT"
        const val ERROR = "ERROR"
    }
}