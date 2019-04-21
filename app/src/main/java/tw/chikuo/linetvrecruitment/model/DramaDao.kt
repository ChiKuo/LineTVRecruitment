package tw.chikuo.linetvrecruitment.model

import android.arch.persistence.room.*

@Dao
interface DramaDao {

    @get:Query("SELECT * FROM drama")
    val all: MutableList<Drama>

    @Query("SELECT * FROM drama WHERE drama_id LIKE :id LIMIT 1")
    fun findById(id: String): Drama

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg drama: Drama)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dramaList: MutableList<Drama>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg drama: Drama)

    @Delete
    fun delete(drama: Drama)

    @Query("DELETE FROM drama")
    fun deleteAll()

    @Query("SELECT * FROM drama WHERE name LIKE :keyword")
    fun getDramaByKeyword(keyword: String): MutableList<Drama>

}