package ph.crisaroa.crudapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.json.JSONObject
import ph.crisaroa.crudapp.adapter.PatientClickInterface
import ph.crisaroa.crudapp.adapter.PatientsAdapter
import ph.crisaroa.crudapp.db.Patient
import ph.crisaroa.crudapp.fragment.AddPatientDialogFragment
import ph.crisaroa.crudapp.viewmodel.PatientViewModel

class MainActivity : AppCompatActivity(), AddPatientDialogFragment.AddPatientDialogListener,
    PatientClickInterface {
    //AppBarLayout declaration
    private lateinit var topAppBar: MaterialToolbar

    //FloatingActionButton declaration
    private lateinit var fabAddPatient: ExtendedFloatingActionButton

    //RecyclerView declaration
    private lateinit var rvPatients: RecyclerView
    private lateinit var rvPatientsAdapter: PatientsAdapter
    private lateinit var rvPatientsLayoutManager: LinearLayoutManager

    //Database declarations
    private lateinit var vm: PatientViewModel

    //Boolean declarations
    private var isLargeLayout: Boolean = true

    //String declarations
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phoneNumber: String
    private lateinit var city: String

    //Integer declarations
    private var currentId: Int = 0

    //Long declarations
    private var currentCreatedAt: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeVariables()
        initializeListeners()
    }

    private fun initializeViews() {
        //AppBarLayout initialization
        topAppBar = findViewById(R.id.top_app_bar)

        //FloatingActionBar initialization
        fabAddPatient = findViewById(R.id.fab_add_patient)

        //RecyclerView initializations
        rvPatients = findViewById(R.id.rv_patients)
        rvPatientsAdapter = PatientsAdapter(this)
        rvPatientsLayoutManager = LinearLayoutManager(this)
        rvPatients.layoutManager = rvPatientsLayoutManager
        rvPatients.adapter = rvPatientsAdapter

        vm = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[PatientViewModel::class.java]

        vm.allPatients.observe(this) { list ->
            list?.let {
                rvPatientsAdapter.updateList(it)
            }
        }
    }

    private fun initializeVariables() {
        //Boolean initializations
        isLargeLayout = resources.getBoolean(R.bool.large_layout)
    }

    private fun initializeListeners() {
        fabAddPatient.setOnClickListener {
            showAddPatientDialog()
        }
    }

    private fun showAddPatientDialog() {
        val fragmentManager = supportFragmentManager
        val nf = AddPatientDialogFragment()

        if (isLargeLayout) {
            nf.show(fragmentManager, AddPatientDialogFragment.TAG)
        } else {
            val data = Bundle()

            data.putString("process", "save")
            nf.arguments = data
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, nf)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onAPDSaveClick(patientData: JSONObject) {
        firstName = patientData.getString("fn")
        lastName = patientData.getString("ln")
        phoneNumber = patientData.getString("p")
        city = patientData.getString("c")
        val process = patientData.getString("process")

        if (process.equals("save")) {
            val createdAt = System.currentTimeMillis()
            val updatedAt = System.currentTimeMillis()
            vm.addPatient(Patient(0, firstName, lastName, phoneNumber, city, createdAt, updatedAt))
            Toast.makeText(this, "Patient successfully added!", Toast.LENGTH_SHORT).show()
        } else {
            val updatedAt = System.currentTimeMillis()
            val updatedPatient = Patient(
                currentId,
                firstName,
                lastName,
                phoneNumber,
                city,
                currentCreatedAt,
                updatedAt
            )
            vm.updatePatient(updatedPatient)
            Toast.makeText(this, "Patient successfully updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPDDeleteClick(patientData: JSONObject) {
        firstName = patientData.getString("fn")
        lastName = patientData.getString("ln")
        phoneNumber = patientData.getString("p")
        city = patientData.getString("c")

        val updatedAt = System.currentTimeMillis()
        val selectedPatient =
            Patient(currentId, firstName, lastName, phoneNumber, city, currentCreatedAt, updatedAt)
        vm.deletePatient(selectedPatient)
        Toast.makeText(this, "Patient successfully deleted!", Toast.LENGTH_SHORT).show()
    }

    override fun onPatientClick(patient: Patient) {
        val fragmentManager = supportFragmentManager
        val nf = AddPatientDialogFragment()
        val data = Bundle()
        val transaction = fragmentManager.beginTransaction()
        currentId = patient.patientId
        currentCreatedAt = patient.createdAt

        data.putString("process", "update")
        data.putString("title", "Update patient data")
        data.putString("fn", patient.firstName)
        data.putString("ln", patient.lastName)
        data.putString("p", patient.phoneNumber)
        data.putString("c", patient.city)
        data.putLong("ca", patient.createdAt)
        data.putInt("id", patient.patientId)
        nf.arguments = data
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction
            .add(android.R.id.content, nf)
            .addToBackStack(null)
            .commit()
    }

}