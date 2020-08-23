package com.yrazlik.tvseriestracker.adapters;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.activities.MainActivity;
import com.yrazlik.tvseriestracker.data.ImageDto;
import com.yrazlik.tvseriestracker.data.NetworkDto;
import com.yrazlik.tvseriestracker.data.RatingDto;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.data.WebChannelDto;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.util.Utils;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 14.11.2017.
 */

public class SearchAdapter extends ArrayAdapter<SearchResultDto> {

    private List<SearchResultDto> items;
    private Context mContext;
    private int layoutResourceId;
    private MainActivity.OnFavoritesChangedListener favoritesChangedListener;

    public SearchAdapter(Context context, int resource, List<SearchResultDto> objects, MainActivity.OnFavoritesChangedListener favoritesChangedListener) {
        super(context, resource, objects);
        this.mContext = context;
        this.items = objects;
        this.layoutResourceId = resource;
        this.favoritesChangedListener = favoritesChangedListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = ((AppCompatActivity)mContext).getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.image = convertView.findViewById(R.id.image);
            holder.channel = convertView.findViewById(R.id.channel);
            holder.favoriteCB = convertView.findViewById(R.id.favoriteCB);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SearchResultDto item = getItem(position);

        if(item != null){
            final ShowDto show = item.getShow();
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

                PicassoImageLoader.getInstance(mContext).loadCircleImage(medium, holder.image);

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

                holder.favoriteCB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        final boolean isChecked = holder.favoriteCB.isChecked();
                        if(isChecked) {
                            Utils.saveToFavoritesList(mContext, show);
                        } else {
                            Utils.removeFromFavoritesList(mContext, show);
                        }
                        if(favoritesChangedListener != null) {
                            favoritesChangedListener.onFavoritesChanged();
                        }
                    }
                });

                if(Utils.isFavoriteShow(show.getId())) {
                    holder.favoriteCB.setChecked(true);
                } else {
                    holder.favoriteCB.setChecked(false);
                }


            }
        }



        return convertView;
    }

    static class ViewHolder{
        public ImageView image;
        public RobotoTextView title;
        public RobotoTextView channel;
        public CheckBox favoriteCB;
    }
}