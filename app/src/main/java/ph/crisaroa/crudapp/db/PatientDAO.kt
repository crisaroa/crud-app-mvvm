package ph.crisaroa.crudapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PatientDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatient(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patients")
    fun loadAllPatients(): LiveData<List<Patient>>
}