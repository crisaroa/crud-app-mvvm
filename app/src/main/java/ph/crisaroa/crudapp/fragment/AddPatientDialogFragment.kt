package ph.crisaroa.crudapp.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import ph.crisaroa.crudapp.R

class AddPatientDialogFragment : DialogFragment() {
    private lateinit var listener: AddPatientDialogListener

    interface AddPatientDialogListener {
        fun onAPDSaveClick(patientData: JSONObject)
        fun onAPDDeleteClick(patientData: JSONObject)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.add_update_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnProcessPatient: MaterialButton = view.findViewById(R.id.btn_process_patient)
        val btnDeletePatient: MaterialButton = view.findViewById(R.id.btn_delete_patient)
        val ibCloseAddPatient: ImageButton = view.findViewById(R.id.ib_close_add_patient)
        val tieFirstName: TextInputEditText = view.findViewById(R.id.tie_add_first_name)
        val tieLastName: TextInputEditText = view.findViewById(R.id.tie_add_last_name)
        val tiePhone: TextInputEditText = view.findViewById(R.id.tie_add_phone)
        val tieCity: TextInputEditText = view.findViewById(R.id.tie_add_city)
        var firstName: String
        var lastName: String
        var phoneNumber: String
        var city: String

        val process = requireArguments().getString("process")
        if (process.equals("update")) {
            tieFirstName.setText(requireArguments().getString("fn"))
            tieLastName.setText(requireArguments().getString("ln"))
            tiePhone.setText(requireArguments().getString("p"))
            tieCity.setText(requireArguments().getString("c"))
            btnDeletePatient.visibility = View.VISIBLE
            btnProcessPatient.text = getString(R.string.update)
        } else {
            tieFirstName.setText("")
            tieLastName.setText("")
            tiePhone.setText("")
            tieCity.setText("")
            btnDeletePatient.visibility = View.GONE
            btnProcessPatient.text = getString(R.string.save)
        }

        ibCloseAddPatient.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            manager.popBackStack()
        }
        btnProcessPatient.setOnClickListener {
            firstName = tieFirstName.text.toString()
            lastName = tieLastName.text.toString()
            phoneNumber = tiePhone.text.toString()
            city = tieCity.text.toString()

            if (validateFields(firstName, lastName, phoneNumber, city)) {
                val patientJSON = JSONObject()
                patientJSON.put("fn", firstName)
                patientJSON.put("ln", lastName)
                patientJSON.put("p", phoneNumber)
                patientJSON.put("c", city)
                patientJSON.put("process", btnProcessPatient.text.toString().lowercase())
                listener.onAPDSaveClick(patientJSON)
                val manager = requireActivity().supportFragmentManager
                manager.popBackStack()
            }
        }
        btnDeletePatient.setOnClickListener {
            firstName = tieFirstName.text.toString()
            lastName = tieLastName.text.toString()
            phoneNumber = tiePhone.text.toString()
            city = tieCity.text.toString()
            val patientJSON = JSONObject()

            patientJSON.put("fn", firstName)
            patientJSON.put("ln", lastName)
            patientJSON.put("p", phoneNumber)
            patientJSON.put("c", city)
            listener.onAPDDeleteClick(patientJSON)
            val manager = requireActivity().supportFragmentManager
            manager.popBackStack()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddPatientDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement AddPatientDialogListener")
            )
        }
    }

    private fun validateFields(fn: String, ln: String, p: String, c: String): Boolean {
        return if (fn.isEmpty() || ln.isEmpty() || p.isEmpty() || c.isEmpty() || p.length != 10) {
            Toast.makeText(context, "Please fill out all empty fields", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    companion object {
        const val TAG = "AddPatientDialog"
    }
}
