package com.livestreaming.tv.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemPlanBinding
import com.livestreaming.tv.ui.fragments.subscriptions.Plan

class PlansAdapter(private var plans: List<Plan>) :
    RecyclerView.Adapter<PlansAdapter.PlanViewHolder>() {

    fun updatePlans(newPlans: List<Plan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

    inner class PlanViewHolder(private val binding: ItemPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan) {
            with(binding) {
                tvTitle.text = plan.planName
                tvPrice.text = plan.price
                tvDesc.text = plan.description

                btnTrial.setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        btnTrial.apply {
                            strokeWidth = 2
                            btnChoose.strokeColor = ColorStateList.valueOf(Color.WHITE)
                        }
                    } else {
                        btnTrial.strokeWidth = 1
                        btnChoose.strokeColor = ColorStateList.valueOf(Color.WHITE)
                    }
                }

                btnChoose.setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        btnChoose.strokeWidth = 2
                        btnChoose.strokeColor = ColorStateList.valueOf(Color.WHITE)
                    } else {
                        btnChoose.strokeWidth = 0
                    }
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val currentPlan = plans[position]
        holder.bind(currentPlan)
    }

    override fun getItemCount(): Int {
        return plans.size
    }
}

