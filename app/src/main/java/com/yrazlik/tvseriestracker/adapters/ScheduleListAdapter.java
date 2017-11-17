package com.yrazlik.tvseriestracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowSchedule;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;

import java.util.List;

/**
 * Created by yrazlik on 17.11.2017.
 */

public class ScheduleListAdapter extends ArrayAdapter<ShowSchedule> {

    private Context mContext;
    private List<ShowSchedule> schedule;

    public ScheduleListAdapter(Context context, int resource, List<ShowSchedule> schedule) {
        super(context, resource, schedule);
        this.mContext = context;
        this.schedule = schedule;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_trending_shows, parent, false);

            holder = new ViewHolder();
            holder.showImage = convertView.findViewById(R.id.showImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShowSchedule schedule = getItem(position);
        PicassoImageLoader.getInstance(mContext).loadImage(schedule.getShow() != null && schedule.getShow().getImage() != null ? schedule.getShow().getImage().getMedium() : "", holder.showImage);

        return convertView;
    }

    static class ViewHolder {
        public ImageView showImage;
    }
}