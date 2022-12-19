package ph.crisaroa.crudapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.crisaroa.crudapp.R
import ph.crisaroa.crudapp.db.Patient

class PatientsAdapter(
    private val patientClickInterface: PatientClickInterface
) :
    RecyclerView.Adapter<PatientsAdapter.ViewHolder>() {

    private val allPatients = ArrayList<Patient>()

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val fullName: TextView = v.findViewById(R.id.tv_full_name)
        val phoneNumber: TextView = v.findViewById(R.id.tv_phone_number)
        val city: TextView = v.findViewById(R.id.tv_city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_patient,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fn = allPatients[position].lastName + ", " + allPatients[position].firstName
        holder.fullName.text = fn
        holder.phoneNumber.text = "+63" + allPatients[position].phoneNumber
        holder.city.text = allPatients[position].city

        holder.itemView.setOnClickListener {
            patientClickInterface.onPatientClick(allPatients[position])
        }
    }

    override fun getItemCount(): Int {
        return allPatients.size
    }

    fun updateList(newList: List<Patient>) {
        allPatients.clear()
        allPatients.addAll(newList)
        notifyDataSetChanged()
    }
}

interface PatientClickInterface {
    fun onPatientClick(patient: Patient)
}
