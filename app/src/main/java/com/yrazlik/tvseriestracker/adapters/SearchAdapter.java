package com.yrazlik.tvseriestracker.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.ImageDto;
import com.yrazlik.tvseriestracker.data.NetworkDto;
import com.yrazlik.tvseriestracker.data.RatingDto;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.data.WebChannelDto;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;

import java.util.List;

/**
 * Created by yrazlik on 14.11.2017.
 */

public class SearchAdapter extends ArrayAdapter<SearchResultDto> {

    private List<SearchResultDto> items;
    private Context mContext;
    private int layoutResourceId;

    public SearchAdapter(Context context, int resource, List<SearchResultDto> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.items = objects;
        this.layoutResourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = ((AppCompatActivity)mContext).getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.image = convertView.findViewById(R.id.image);
            holder.channel = convertView.findViewById(R.id.channel);
            holder.rating = convertView.findViewById(R.id.rating);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SearchResultDto item = getItem(position);

        if(item != null){
            ShowDto show = item.getShow();
            if(show != null){
                RatingDto showRating = show.getRating();
                String name = show.getName();
                String medium = null;
                ImageDto image = show.getImage();
                NetworkDto network = show.getNetwork();
                if(image != null) {
                    medium = show.getImage().getMedium();
                }

                if(name != null) {
                    holder.title.setText(name);
                }

                PicassoImageLoader.getInstance(mContext).loadImage(medium, holder.image);

                if(network != null){
                    String channelName = network.getName();
                    if(channelName != null && channelName.length() > 0){
                        holder.channel.setVisibility(View.VISIBLE);
                        holder.channel.setText(channelName);
                    }else{
                        holder.channel.setVisibility(View.GONE);
                    }
                }else{
                    WebChannelDto webChannel = show.getWebChannel();
                    if(webChannel != null){
                        String channelName = webChannel.getName();
                        if(channelName != null && channelName.length() > 0){
                            holder.channel.setVisibility(View.VISIBLE);
                            holder.channel.setText(channelName);
                        }else{
                            holder.channel.setVisibility(View.GONE);
                        }
                    }else {
                        holder.channel.setVisibility(View.GONE);
                    }
                }

                if(showRating != null){
                    Double avgRating = showRating.getAverage();
                    if(avgRating != null){
                        String averageRating = avgRating + "";
                        if(averageRating != null && averageRating.length() > 0){
                            holder.rating.setVisibility(View.VISIBLE);
                            holder.rating.setText(averageRating);
                        }else{
                            holder.rating.setVisibility(View.GONE);
                        }
                    }else {
                        holder.rating.setVisibility(View.GONE);
                    }

                }else{
                    holder.rating.setVisibility(View.GONE);
                }


            }
        }



        return convertView;
    }

    static class ViewHolder{
        public ImageView image;
        public TextView title;
        public TextView channel;
        public TextView rating;
    }
}