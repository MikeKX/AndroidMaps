package com.pathmazing.androidmaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pathmazing.androidmaps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterView extends ArrayAdapter<PlaceModel> {
    Context context;
    ViewHolder holder = null;
    int layoutResourceId;
    ArrayList<PlaceModel> data = new ArrayList<PlaceModel>();
    View row;
    SharedPreferences prefs;

    public AdapterView(Context context, int layoutResourceId, ArrayList<PlaceModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder.txTitle = (TextView) row.findViewById(R.id.txName);
            holder.imageView = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txAddress = (TextView) row.findViewById(R.id.txAddress);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        final PlaceModel model = data.get(position);
        holder.txTitle.setText(model.getName());
        Picasso.with(context)
                .load(model.getIcon())
                .placeholder(R.drawable.ic_launcher)
                .error(R.color.colorAccent)
                .into(holder.imageView);
        holder.txAddress.setText(model.getVicinity());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("key_lat", model.getLat());
                intent.putExtra("key_lng", model.getLng());
                context.startActivity(intent);
            }
        });

        return row;

    }

    static class ViewHolder {
        TextView txTitle;
        TextView txAddress;
        ImageView imageView;
    }
}
