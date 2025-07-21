package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.twokingssolutions.diabeeto.db.entity.Departments
import com.twokingssolutions.diabeeto.util.Constants.DEPARTMENTS_TABLE

@Dao
interface DepartmentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: Departments)

    @Query("SELECT * FROM $DEPARTMENTS_TABLE WHERE department_id = :departmentId")
    suspend fun getDepartmentById(departmentId: String): Departments?

    @Query("SELECT * FROM Departments WHERE department_name = :name")
    suspend fun getDepartmentByName(name: String): Departments?

    @Query("SELECT * FROM $DEPARTMENTS_TABLE ORDER BY department_name ASC")
    suspend fun getAllDepartments(): List<Departments>

    @Query("SELECT * FROM Departments WHERE department_name LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchDepartments(query: String, limit: Int = 5): List<Departments>
}