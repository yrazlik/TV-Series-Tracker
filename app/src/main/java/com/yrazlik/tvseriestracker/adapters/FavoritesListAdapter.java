package com.yrazlik.tvseriestracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.util.Utils;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;

import okhttp3.internal.Util;

/**
 * Created by yrazlik on 12.11.2017.
 */

public class FavoritesListAdapter  extends ArrayAdapter<ShowDto> {

    public interface FavoritesEmptyListener {
        void onFavoritesEmpty();
    }

    private FavoritesEmptyListener favoritesEmptyListener;
    private Context mContext;
    private List<ShowDto> shows;

    public FavoritesListAdapter(Context context, int resource, List<ShowDto> shows, FavoritesEmptyListener favoritesEmptyListener) {
        super(context, resource, shows);
        this.mContext = context;
        this.shows = shows;
        this.favoritesEmptyListener = favoritesEmptyListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_trending_shows, parent, false);

            holder = new ViewHolder();
            holder.showtextContainer = convertView.findViewById(R.id.showtextContainer);
            holder.showImage = convertView.findViewById(R.id.showImage);
            holder.showTitle = convertView.findViewById(R.id.showTitle);
            holder.showGenres = convertView.findViewById(R.id.showGenres);
            holder.showRating = convertView.findViewById(R.id.showRating);
            holder.favoriteCB = convertView.findViewById(R.id.favoriteCB);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShowDto show = getItem(position);
        PicassoImageLoader.getInstance(mContext).loadImage(show.getImage() != null ? show.getImage().getMedium() : "", holder.showImage);
        holder.showTitle.setText((position + 1) + ". " + show.getName());
        holder.showGenres.setText(show.getGenresText());
        holder.showRating.setText((show.getRating() != null && show.getRating().getAverage() > 0) ? show.getRating().getAverage() + "" : "-");


        holder.favoriteCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = holder.favoriteCB.isChecked();
                if(isChecked) {
                    Utils.saveToFavoritesList(mContext, show);
                } else {
                    String areYouSureText = mContext.getResources().getString(R.string.are_you_sure_t_remove_from_favorites, show.getName());
                    new AlertDialog.Builder(mContext).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.removeFromFavoritesList(mContext, show);
                            shows.remove(show);
                            notifyDataSetChanged();
                            if(shows.size() == 0) {
                                favoritesEmptyListener.onFavoritesEmpty();
                            }
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            holder.favoriteCB.setChecked(true);
                        }
                    }).setMessage(areYouSureText).show();
                }
            }
        });

        if(Utils.isFavoriteShow(show.getId())) {
            holder.favoriteCB.setChecked(true);
        } else {
            holder.favoriteCB.setChecked(false);
        }

        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout showtextContainer;
        public ImageView showImage;
        public RobotoTextView showTitle;
        public RobotoTextView showGenres;
        public RobotoTextView showRating;
        public CheckBox favoriteCB;
    }
}