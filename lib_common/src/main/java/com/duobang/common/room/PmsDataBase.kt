package com.duobang.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.duobang.common.data.bean.DailyComment
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.data.bean.User
import com.duobang.common.room.convert.DailyCommentListConvert
import com.duobang.common.room.convert.DailyTaskListConvert
import com.duobang.common.room.convert.DateConvert
import com.duobang.common.room.dao.DailyTaskWrapperDao
import com.duobang.common.room.dao.UserDao

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/25 16:55
 * @描述　: 创建database类继承自RoomDatabase类，必须声明成abstract entities,
 * 指定需要创建的数据表的类，必须有Entity注解,version ,指定数据库版本
 */
@Database(
    entities = [User::class, DailyTaskWrapper::class, DailyComment::class, DailyTask::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DailyTaskListConvert::class, DailyCommentListConvert::class, DateConvert::class
)
abstract class PmsDataBase : RoomDatabase() {
    abstract fun dailyTaskWrapperDao(): DailyTaskWrapperDao?
    abstract fun userDao(): UserDao?

    companion object {

        @Volatile
        private var INSTANCE: PmsDataBase? = null
        fun getInstance(context: Context): PmsDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PmsDataBase::class.java,
                    "pms_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}