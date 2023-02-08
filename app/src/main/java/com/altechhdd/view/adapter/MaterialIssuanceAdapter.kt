package com.altechhdd.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altechhdd.databinding.ItemMaterialIssaunceBinding
import com.altechhdd.interfaces.OnItemSelected
import com.altechhdd.model.GetCoilDetail.GetCoilResponseDetail
import com.altechhdd.model.GetMaterialInsurance.GetMaterialInsuranceModel
import com.altechhdd.viewModel.MaterialIssuanceViewModel

class MaterialIssuanceAdapter(
    private val list: MutableList<List<GetCoilResponseDetail>>,
    val viewModel: MaterialIssuanceViewModel,
) : RecyclerView.Adapter<MaterialIssuanceAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemMaterialIssaunceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMaterialIssaunceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position][position]
        holder.binding.txtColilNo.text = item.coilNo
        holder.binding.txtWidth.text = item.width
        holder.binding.txtWeightKg.text = item.weightKG
        holder.binding.txtSource.text = item.sourceLoc
        holder.binding.ivDelete.setOnClickListener {
            list.removeAt(position)// = t
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}