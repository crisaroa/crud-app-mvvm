package ph.crisaroa.crudapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Patient::class], version = 1, exportSchema = false)
abstract class PatientDatabase : RoomDatabase() {
    abstract fun getPatientDAO(): PatientDAO

    companion object {
        @Volatile
        private var INSTANCE: PatientDatabase? = null

        fun getDatabase(context: Context): PatientDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatientDatabase::class.java,
                    "patient_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}