package com.example.cambox.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.cambox.databinding.ItemOrderBinding;
import com.example.cambox.interfaces.OnClickListenerOrder;
import com.example.cambox.model.Order;
import com.example.cambox.util.FormatUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRvAdapter extends RecyclerView.Adapter<OrderRvAdapter.OrderProcessingVH> {
    private List<Order> list;
    private OnClickListenerOrder listener;

    public OrderRvAdapter(List<Order> list) {
        this.list = list;
    }

    public void setListener(OnClickListenerOrder listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderProcessingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = ItemOrderBinding.inflate(inflater, parent, false);
        return new OrderProcessingVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProcessingVH holder, int position) {
        Order order = list.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return list!=null? list.size():0;
    }

    public class OrderProcessingVH extends RecyclerView.ViewHolder {
        private ItemOrderBinding binding;
        public OrderProcessingVH(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Order order){
            binding.setOrder(order);
            binding.setOnclick(listener);
            binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
            binding.executePendingBindings();
        }
    }
}
