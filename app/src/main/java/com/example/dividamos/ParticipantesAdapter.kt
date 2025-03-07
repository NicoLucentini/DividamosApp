package com.example.dividamos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipantesAdapter(private val participantes: List<String>) : RecyclerView.Adapter<ParticipantesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val textViewParticipante: TextView = view.findViewById(android.R.id.text1)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  participantes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewParticipante.text = participantes[position]
    }
}

