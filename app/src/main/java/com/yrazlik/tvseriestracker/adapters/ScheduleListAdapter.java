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
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
            convertView = inflater.inflate(R.layout.list_row_schedule, parent, false);

            holder = new ViewHolder();
            holder.showImage = convertView.findViewById(R.id.showImage);
            holder.showTitle = convertView.findViewById(R.id.showTitle);
            holder.episodeTV = convertView.findViewById(R.id.episodeTV);
            holder.episodeDateTV = convertView.findViewById(R.id.episodeDateTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShowSchedule schedule = getItem(position);
        PicassoImageLoader.getInstance(mContext).loadImage(schedule.getShow() != null && schedule.getShow().getImage() != null ? schedule.getShow().getImage().getMedium() : "", holder.showImage);

        holder.showTitle.setText(schedule.getShow().getName());
        holder.episodeTV.setText(mContext.getResources().getString(R.string.season) + " " + schedule.getEpisode().getSeason()
            + ", " + mContext.getResources().getString(R.string.episode) + schedule.getEpisode().getNumber());

        holder.episodeDateTV.setText(getDateText(schedule));
        return convertView;
    }

    private String getDateText(ShowSchedule schedule) {
        String airDate = schedule.getEpisode().getAirDate();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(airDate);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
            String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return  dayName + ", " + month + " " + day;

        } catch (Exception e) {
            return "";
        }
    }

    static class ViewHolder {
        public ImageView showImage;
        public RobotoTextView showTitle;
        public RobotoTextView episodeTV;
        private RobotoTextView episodeDateTV;
    }
}