package com.example.ausias.ingotohelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ausias.ingotohelper.DAOS.Requests;

import java.util.List;

/**
 * Created by ausias on 15/05/17.
 */

public class ListViewRequestAdapter extends BaseAdapter {
    Context context;
    List<Requests> listRequest;

    public ListViewRequestAdapter(Context ctx, List<Requests> results){
        this.context = ctx;
        this.listRequest = results;
    }

    @Override
    public int getCount() {
        return listRequest.size();
    }

    @Override
    public Object getItem(int position) {
        return listRequest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listRequest.indexOf(position);
    }

    private class ViewHolder{
        ImageView ivFriend;
        TextView tvNameSolicita;
        ImageButton ibAccept;
        ImageButton ibDenny;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.request, null);
            holder = new ViewHolder();

            holder.tvNameSolicita = (TextView) convertView.findViewById(R.id.tvNameSolicita);
            holder.ivFriend = (ImageView) convertView.findViewById(R.id.ivFriend);
            holder.ibAccept = (ImageButton) convertView.findViewById(R.id.ibAccept);
            holder.ibDenny = (ImageButton) convertView.findViewById(R.id.ibDenny);

            Requests requests_pos = listRequest.get(position);

            if(requests_pos.getPhoto()!=null)
                holder.ivFriend.setImageBitmap(Bitmap.createBitmap(requests_pos.getPhoto()));
            else {
                Drawable defaultProfile = context.getDrawable(R.drawable.default_profile);
                holder.ivFriend.setImageDrawable(defaultProfile);
            }
            holder.tvNameSolicita.setText(requests_pos.getNom());
            holder.ibAccept.setImageResource(R.drawable.tick);
            holder.ibDenny.setImageResource(R.drawable.cross);

            holder.ibAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TICK","TICK");
                    new BackgroundWorker(context).execute("accepta",MainActivity.id, requests_pos.getIdSolicitant());
                }
            });
            holder.ibDenny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
