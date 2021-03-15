package com.project.countplusplus.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.countplusplus.R
import com.project.countplusplus.models.PaymentModel
import com.project.countplusplus.payment.EditPaymentActivity
import com.project.countplusplus.payment.PaymentActivity
import kotlinx.android.synthetic.main.recycler_view.view.*


class PaymentRecyclerAdapter(val context: Context, val items: ArrayList<PaymentModel>):

    RecyclerView.Adapter<PaymentRecyclerAdapter.ViewHolder>()
    {
        var ctx: Context? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(context).inflate(
                            R.layout.recycler_view,
                            parent,
                            false
                    )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item = items.get(position)

            holder.senderNameTv.text = item.sender_name
            holder.amountTv.text = item.amount
            holder.dueDateTv.text = item.due_date
            if (item.recieved!="")
            {
                holder.statusTv.text = "Recieved"
            }
            else
            {
                holder.statusTv.text = "Not Recieved"
            }
//            for updating payment

            holder.itemContainer.setOnClickListener{
                val FLAG = 1
                val context=holder.itemView.context
                val intent = Intent( context, EditPaymentActivity::class.java)
                intent.putExtra("paymentId", item.id)
                intent.putExtra("sender_name", item.sender_name)
                intent.putExtra("sender_email", item.sender_email)
                intent.putExtra("reciver_id", item.reciver_id)
                intent.putExtra("amount", item.amount)
                intent.putExtra("due_date", item.due_date)
                intent.putExtra("recieved", item.recieved)
                intent.putExtra("recvied_date", item.recvied_date)
                (context as PaymentActivity).startActivityForResult(intent,FLAG)

            }
//
////            for deleting payment
//            holder.itemContainer.setOnLongClickListener{
//                Log.d("this name",holder.senderNameTv.text.toString())
//                true
//            }

        }

        override fun getItemCount(): Int {
            return items.size
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each item to
            val itemContainer = view.paymentRecycleCardView
            val senderNameTv = view.senderNameTv
            val amountTv = view.amountTv
            val dueDateTv = view.dueDateTv
            val statusTv = view.statusTv
//            val ivEdit = view.ivEdit
//            val ivDelete = view.ivDelete
        }

    }