package ph.crisaroa.crudapp.db

class PatientRepository(private val dao: PatientDAO) {
    fun patients() = dao.loadAllPatients()

    suspend fun insert(patient: Patient) {
        dao.insertPatient(patient)
    }

    suspend fun update(patient: Patient) {
        dao.updatePatient(patient)
    }

    suspend fun delete(patient: Patient) {
        dao.deletePatient(patient)
    }
}