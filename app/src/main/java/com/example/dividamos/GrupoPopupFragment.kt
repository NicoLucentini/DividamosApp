package com.example.dividamos

import android.app.Activity
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

class GrupoPopupFragment : DialogFragment() {

    lateinit var activity : HomeActivity
    public fun onStart(activity: HomeActivity){
        this.activity = activity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_group, container, false)

        val editTextNombreGrupo = view.findViewById<EditText>(R.id.editTextNombreGrupo)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerParticipantes)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarPopup)
        val btnCrear = view.findViewById<Button>(R.id.btnAgregar)
        // Configurar RecyclerView (Lista de Participantes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val participantes = listOf("Pepito", HomeActivity.user_data!!.nombre)

        recyclerView.adapter = ParticipantesAdapter(participantes) // Datos de prueba

        // Cerrar el Popup al hacer clic
        btnCerrar.setOnClickListener {
            dismiss()
        }
        btnCrear.setOnClickListener{
            sendCrearGrupoRequest(editTextNombreGrupo.text.toString(), participantes,
                this.requireContext()
            )
        }

        return view
    }
    fun sendCrearGrupoRequest(nombre: String, participantes : List<String>, context: android.content.Context) {

        val grupo = Grupo(nombre, participantes, -1)

        RetrofitClient.apiService.crearGrupo(grupo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onCreateSuccesfull()
                } else {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
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
        activity.fetchGrupos()
        dismiss()
    }
}