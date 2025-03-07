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
    var participants : MutableList<Participante> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var emailParticipant : EditText
    var idGrupo : Int = 0
    var gasto : Gasto? = null

    fun onStart(idGrupo: Int, activity: GrupoActivity, gasto: Gasto?){
        this.activity = activity
        this.idGrupo = idGrupo
        this.gasto = gasto
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_gasto, container, false)

        val editTextNombreGasto = view.findViewById<EditText>(R.id.editTextNombreGasto)
        val editTextNombrePagador = view.findViewById<EditText>(R.id.editTextPagador)
        val editTextMonto = view.findViewById<EditText>(R.id.editTextMonto)

        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarPopup)
        val btnCrear = view.findViewById<Button>(R.id.btnAgregar)


        if(gasto!=null){
            editTextNombreGasto.setText(gasto!!.detalle)
            editTextNombrePagador.setText(gasto!!.nombrePagador)
            editTextMonto.setText(gasto!!.monto.toString())

        }
        participantes = HomeActivity.grupo_data?.participantes!!.toMutableList()

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerParticipantes)
        recyclerView.layoutManager = LinearLayoutManager(context)

        participants = participantes.map { Participante(it, gasto == null || gasto!!.nombrePagador != it) }.toMutableList()

        val adapter = ParticipantesGastosAdapter(participants) { participant ->
            if (participant.isSelected) {
                participantes.add(participant.nombre) // Add to selected list
            } else {
                participantes.remove(participant.nombre) // Remove from selected list
            }
        }
        recyclerView.adapter = adapter

        btnCerrar.setOnClickListener {
            dismiss()
        }
        btnCrear.text = if(gasto==null) "Crear" else "Editar"

        btnCrear.setOnClickListener{
            if(gasto == null)
            {
                sendCrearGastoRequest(idGrupo,
                    editTextNombreGasto.text.toString(),
                    editTextNombrePagador.text.toString(),
                    editTextMonto.text.toString().toFloat(),
                    this.requireContext()
                )
            }
            else{
                sendEditarGastoRequest(gasto!!.id,
                    editTextNombreGasto.text.toString(),
                    editTextNombrePagador.text.toString(),
                    editTextMonto.text.toString().toFloat(),
                    this.requireContext())
            }

        }

        return view
    }
    private fun sendCrearGastoRequest(idGrupo : Int,
                              nombre: String,
                              nombrePagador: String,
                              monto : Float,
                                      context: Context) {

        participantes = participants.filter { it.isSelected }.map { it.nombre }.toMutableList()
        val gasto = Gasto(nombre, nombrePagador,participantes, monto,-1)

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
    private fun sendEditarGastoRequest(idGasto : Int,
                                      nombre: String,
                                      nombrePagador: String,
                                      monto : Float,
                                      context: Context) {

        participantes = participants.filter { it.isSelected }.map { it.nombre }.toMutableList()
        val gasto = Gasto(nombre, nombrePagador,participantes, monto, idGasto)

        RetrofitClient.apiService.editarGasto(idGasto, gasto).enqueue(object : Callback<Void> {
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
        Toast.makeText(context, "Gasto editado correctamente", Toast.LENGTH_SHORT).show()
        activity.fetchGastos()
        dismiss()
    }
}