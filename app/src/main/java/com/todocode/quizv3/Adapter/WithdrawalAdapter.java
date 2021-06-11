package com.todocode.quizv3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todocode.quizv3.Model.Withdrawal;
import com.todocode.quizv3.R;

import java.util.ArrayList;

public class WithdrawalAdapter extends RecyclerView.Adapter<WithdrawalAdapter.WithdrawalsHolder> {
    private Context context;
    private ArrayList<Withdrawal> withdrawals;

    public class WithdrawalsHolder extends RecyclerView.ViewHolder {
        private TextView name, status, account, method, amount, points, date;

        public WithdrawalsHolder(@NonNull View itemView) {
            super(itemView);
            status = (TextView) itemView.findViewById(R.id.payment_status);
            method = (TextView) itemView.findViewById(R.id.payment_method);
            amount = (TextView) itemView.findViewById(R.id.payment_amount);
            date = (TextView) itemView.findViewById(R.id.payment_date);
        }
        @SuppressLint("SetTextI18n")
        public void setDetails(Withdrawal withdrawal) {
            status.setText(withdrawal.getStatus());
            method.setText(withdrawal.getPayment_method());
            SharedPreferences currencyShared = context.getSharedPreferences("currencyShared", Context.MODE_PRIVATE);
            amount.setText(currencyShared.getString("currencyShared", "")+ " " + String.valueOf(withdrawal.getAmount()));
            date.setText(withdrawal.getDate());
        }
    }

    public WithdrawalAdapter(Context context, ArrayList<Withdrawal> withdrawals) {
        this.context = context;
        this.withdrawals = withdrawals;
    }

    @NonNull
    @Override
    public WithdrawalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_withdrawal, parent, false);
        return new WithdrawalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawalsHolder holder, int position) {
        Withdrawal withdrawal = withdrawals.get(position);
        holder.setDetails(withdrawal);
    }

    @Override
    public int getItemCount() {
        return withdrawals.size();
    }
}



