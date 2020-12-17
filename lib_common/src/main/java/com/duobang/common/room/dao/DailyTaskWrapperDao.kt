package com.duobang.common.room.dao

import androidx.room.*
import com.duobang.common.data.bean.DailyTaskWrapper

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/1 15:15
 * @描述　: 日事日毕数据层Dao接口
 */
@Dao
interface DailyTaskWrapperDao {
    @Query("SELECT * FROM daily_task_wrapper where org_id =(:ordId)")
    fun getAll(ordId: String?): List<DailyTaskWrapper?>?

    @Query("SELECT * FROM daily_task_wrapper where id =(:id)")
    fun getDailyTaskFromId(id: String?): DailyTaskWrapper?

    @Query("SELECT * FROM daily_task_wrapper where year =(:year) and month =(:month) and day =(:day) and org_id =(:ordId)")
    fun getDailyTaskFromData(
        year: Int,
        month: Int,
        day: Int,
        ordId: String?
    ): List<DailyTaskWrapper?>?

    @Update
    fun update(dailyTaskWrapper: DailyTaskWrapper?): Int

    @Query("DELETE FROM daily_task_wrapper where id = (:id) ")
    fun delDailyTaskWrapperById(id: String?): Int

    @Query("DELETE FROM daily_task_wrapper")
    fun delAllDailyTaskWrapper(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneDailyTaskWrapper(dailyTaskWrapperEntity: DailyTaskWrapper?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDailyTaskWrapper(dailyTaskWrapperEntityList: List<DailyTaskWrapper?>?)
}