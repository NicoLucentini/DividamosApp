package com.example.dividamos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

data class Participante(
    val nombre: String,
    var isSelected: Boolean = false
)
class ParticipantesGastosAdapter(
    private val participants: List<Participante>,
    private val onToggleChanged: (Participante) -> Unit
) : RecyclerView.Adapter<ParticipantesGastosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewNombre: TextView = view.findViewById(R.id.textViewName)
        val toggleButton: ToggleButton = view.findViewById(R.id.textNameToggle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = participants[position]
        holder.textViewNombre.text = participant.nombre
        holder.toggleButton.isChecked = participant.isSelected

        holder.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            participant.isSelected = isChecked
            onToggleChanged(participant)
        }
    }

    override fun getItemCount() = participants.size
}
