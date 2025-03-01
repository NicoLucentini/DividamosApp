package com.example.dividamos

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dividamos.apiservice.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GastoPopupFragment : DialogFragment() {
    lateinit var activity : GrupoActivity
    var participantes : MutableList<String> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var emailParticipant : EditText
    var idGrupo : Int = 0

    public fun onStart(idGrupo: Int, activity: GrupoActivity){
        this.activity = activity
        this.idGrupo = idGrupo
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_gasto, container, false)

        val editTextNombreGasto = view.findViewById<EditText>(R.id.editTextNombreGasto)
        val editTextNombrePagador = view.findViewById<EditText>(R.id.editTextPagador)
        val editTextMonto = view.findViewById<EditText>(R.id.editTextMonto)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerParticipantes)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarPopup)
        val btnCrear = view.findViewById<Button>(R.id.btnAgregar)
        val btnAddParticipant = view.findViewById<Button>(R.id.buttonAddParticipant)

        emailParticipant = view.findViewById<EditText>(R.id.editTextParticipantEmail)

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = ParticipantesAdapter(participantes) // Datos de prueba

        btnCerrar.setOnClickListener {
            dismiss()
        }
        btnCrear.setOnClickListener{
            sendCrearGastoRequest(idGrupo,
                editTextNombreGasto.text.toString(),
                editTextNombrePagador.text.toString(),
                editTextMonto.text.toString().toFloat(),
                this.requireContext()
            )
        }
        btnAddParticipant.setOnClickListener{
            sendAddParticipantRequest(emailParticipant.text.toString(), this.requireContext())
        }
        return view
    }

    private fun sendAddParticipantRequest(participant: String, context: Context) {
        RetrofitClient.apiService.agregarParticipante(participant).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                println(response.body().toString() + " " + response.message() )
                if (response.isSuccessful) {
                    participantes.add(participant)
                    recyclerView.adapter?.notifyDataSetChanged()
                    emailParticipant.text.clear()
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendCrearGastoRequest(idGrupo : Int,
                              nombre: String,
                              nombrePagador: String,
                              monto : Float,
                                      context: android.content.Context) {

        val gasto = Gasto(nombre, nombrePagador,participantes, monto)

        RetrofitClient.apiService.crearGasto(idGrupo, gasto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onCreateSuccesfull()
                } else {
                    Toast.makeText(context, "Login asdasd!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
    }
    fun onCreateSuccesfull(){
        Toast.makeText(context, "Grupo creado", Toast.LENGTH_SHORT).show()
        activity.fetchGastos()
        dismiss()
    }
}