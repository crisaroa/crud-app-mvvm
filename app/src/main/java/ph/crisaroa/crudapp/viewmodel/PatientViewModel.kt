package ph.crisaroa.crudapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ph.crisaroa.crudapp.db.Patient
import ph.crisaroa.crudapp.db.PatientDatabase
import ph.crisaroa.crudapp.db.PatientRepository

class PatientViewModel(application: Application) : AndroidViewModel(application) {
    val allPatients: LiveData<List<Patient>>
    private val repository: PatientRepository

    init {
        val dao = PatientDatabase.getDatabase(application).getPatientDAO()
        repository = PatientRepository(dao)
        allPatients = repository.patients()
    }

    fun deletePatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(patient)
    }

    fun updatePatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(patient)
    }

    fun addPatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(patient)
    }
}