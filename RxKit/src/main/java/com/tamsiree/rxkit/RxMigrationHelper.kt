package com.tamsiree.rxkit

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.database.StandardDatabase
import org.greenrobot.greendao.internal.DaoConfig
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created by Android Studio
 * user : Tamsiree
 * date : 2017/5/11 ${time}
 * desc :
 */
object RxMigrationHelper {
    var DEBUG = false
    private const val TAG = "RxMigrationHelper"

    @JvmStatic
    fun migrate(sqliteDatabase: SQLiteDatabase, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        val db = StandardDatabase(sqliteDatabase)
        printLog("【The Old Database Version】" + sqliteDatabase.version)
        printLog("【Generate temp table】start")
        migrate(db, *daoClasses)
    }

    @JvmStatic
    fun migrate(db: StandardDatabase, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        generateNewTablesIfNotExists(db, *daoClasses)
        printLog("【Generate temp table】complete")
        generateTempTables(db, *daoClasses)
        dropAllTables(db, true, *daoClasses)
        createAllTables(db, false, *daoClasses)
        printLog("【Restore data】start")
        restoreData(db, *daoClasses)
        printLog("【Restore data】complete")
    }

    private fun generateNewTablesIfNotExists(db: StandardDatabase, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        reflectMethod(db, "createTable", true, *daoClasses)
    }

    private fun generateTempTables(db: StandardDatabase, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        for (i in 0 until daoClasses.size) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"
            val insertTableStringBuilder = StringBuilder()
            insertTableStringBuilder.append("CREATE TEMP TABLE ").append(tempTableName)
            insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";")
            db.execSQL(insertTableStringBuilder.toString())
        }
    }

    private fun dropAllTables(db: StandardDatabase, ifExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        reflectMethod(db, "dropTable", ifExists, *daoClasses)
    }

    private fun createAllTables(db: StandardDatabase, ifNotExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        reflectMethod(db, "createTable", ifNotExists, *daoClasses)
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private fun reflectMethod(db: StandardDatabase, methodName: String, isExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        if (daoClasses.size < 1) {
            return
        }
        try {
            for (cls in daoClasses) {
                val method = cls.getDeclaredMethod(methodName, Database::class.java, Boolean::class.javaPrimitiveType)
                method.invoke(null, db, isExists)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private fun restoreData(db: StandardDatabase, vararg daoClasses: Class<out AbstractDao<*, *>?>) {
        for (i in 0 until daoClasses.size) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"
            // get all columns from tempTable, take careful to use the columns list
            val columns = getColumns(db, tempTableName)
            val properties = ArrayList<String?>(columns.size)
            for (j in daoConfig.properties.indices) {
                val columnName = daoConfig.properties[j].columnName
                if (columns.contains(columnName)) {
                    properties.add(columnName)
                }
            }
            if (properties.size > 0) {
                val columnSQL = TextUtils.join(",", properties)
                val insertTableStringBuilder = StringBuilder()
                insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (")
                insertTableStringBuilder.append(columnSQL)
                insertTableStringBuilder.append(") SELECT ")
                insertTableStringBuilder.append(columnSQL)
                insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";")
                db.execSQL(insertTableStringBuilder.toString())
            }
            val dropTableStringBuilder = StringBuilder()
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName)
            db.execSQL(dropTableStringBuilder.toString())
        }
    }

    private fun getColumns(db: StandardDatabase, tableName: String): List<String> {
        var columns: List<String>? = null
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName limit 0", null)
            if (null != cursor && cursor.columnCount > 0) {
                columns = Arrays.asList(*cursor.columnNames)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            if (null == columns) {
                columns = ArrayList()
            }
        }
        return columns
    }

    //----------------------------------------------------------------------------------------------
    private fun printLog(info: String) {
        if (DEBUG) {
            Log.d(TAG, info)
        }
    }
}