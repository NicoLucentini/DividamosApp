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
import com.example.dividamos.apiservice.callRetrofit
import retrofit2.Call

class GastoPopupFragment : DialogFragment() {
    lateinit var activity : GrupoActivity
    var participantes : MutableList<String> = mutableListOf()
    var participants : MutableList<Participante> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var emailParticipant : EditText
    var idGrupo : Int = 0
    var gasto : Gasto? = null
    lateinit var btnCerrar : Button
    lateinit var btnCrear : Button

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
        val btnMe = view.findViewById<Button>(R.id.buttonMe)
        btnCerrar = view.findViewById<Button>(R.id.btnCerrarPopup)
        btnCrear = view.findViewById<Button>(R.id.btnAgregar)


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
        btnMe.setOnClickListener{

            val email = HomeActivity.user_data!!.email
            editTextNombrePagador.setText( email)
            participants.forEach {
                if (it.nombre.equals(email))
                    it.isSelected = false
            }
            adapter.notifyDataSetChanged()
        }
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

        btnCrear.isEnabled = false
        participantes = participants.filter { it.isSelected }.map { it.nombre }.toMutableList()
        val gasto = Gasto(nombre, nombrePagador,participantes, monto,-1)


        callRetrofit(RetrofitClient.apiService.crearGasto(idGrupo, gasto))
    }
    private fun sendEditarGastoRequest(idGasto : Int,
                                      nombre: String,
                                      nombrePagador: String,
                                      monto : Float,
                                      context: Context) {
        btnCrear.isEnabled = false
        participantes = participants.filter { it.isSelected }.map { it.nombre }.toMutableList()
        val gasto = Gasto(nombre, nombrePagador,participantes, monto, idGasto)

        callRetrofit(RetrofitClient.apiService.editarGasto(idGasto, gasto))

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
    }
    fun <T> callRetrofit(call : Call<T>){
        callRetrofit(
            call,
            onSuccess = { onCreateSuccesfull() },
            onError = { errorMessage -> onCreateError(errorMessage) },
            onFailure = { throwable ->  onCreateFailure(throwable)}
        )
    }
    fun onCreateSuccesfull(){
        btnCrear.isEnabled = true
        Toast.makeText(context, "Gasto editado correctamente", Toast.LENGTH_SHORT).show()
        activity.fetchGastos()
        dismiss()
    }
    fun onCreateError(errorMessage :String){
        btnCrear.isEnabled = true
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    fun onCreateFailure(throwable :Throwable){
        btnCrear.isEnabled = true
        Toast.makeText(context, "Network error: ${throwable.message}", Toast.LENGTH_SHORT).show()
    }
}