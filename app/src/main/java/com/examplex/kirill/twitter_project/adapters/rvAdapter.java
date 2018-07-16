package com.examplex.kirill.twitter_project.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examplex.kirill.twitter_project.R;
import com.examplex.kirill.twitter_project.models.Messages;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.RvViewholder>{

    public RealmList<Messages> list;
    private SimpleDateFormat dFormat;

    public rvAdapter(RealmList<Messages> list) {
        this.list = list;

        dFormat= new SimpleDateFormat("dd.mm.yyyy 'at' HH:mm");
    }

    @NonNull
    @Override
    public RvViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new RvViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewholder holder, int position) {
        Messages msg = list.get(position);
        holder.msgText.setText(msg.getMsgText());
        holder.msgDate.setText("" + dFormat.format(msg.getMsgDate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void addItem(Messages msg) {
            list.add(msg);
            notifyItemInserted(getItemCount());
    }

    public int updatePosition(long position) {
        int cnt = 0;
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getMsgId() == position)
            {
                notifyItemChanged(i);
                cnt++;
            }

        }
        return cnt;

    }

    public long get(int position) {

        return list.get(position).getMsgId();
    }


    public class RvViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView msgText;
        TextView msgDate;



        public RvViewholder(View itemView) {
            super(itemView);

            msgText = itemView.findViewById(R.id.cv_msg);
            msgDate = itemView.findViewById(R.id.cv_date);
        }


        @Override
        public void onClick(View v) {

        }
    }


}
