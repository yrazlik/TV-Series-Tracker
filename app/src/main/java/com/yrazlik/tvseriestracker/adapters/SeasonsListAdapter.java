package com.yrazlik.tvseriestracker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.util.Utils;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class SeasonsListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private List<Long> seasons;
    private Map<Long, List<EpisodeDto>> episodes;
    private long showId;

    public SeasonsListAdapter(Context context, List<Long> seasons,
                                 Map<Long, List<EpisodeDto>> episodes, long showId) {
        this.mContext = context;
        this.seasons = seasons;
        this.episodes = episodes;
        this.showId = showId;
    }

    @Override
    public int getGroupCount() {
        return this.seasons.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.episodes.get(this.seasons.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.seasons.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.episodes.get(this.seasons.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        HeaderHolder holder;
        String headerTitle = mContext.getResources().getString (R.string.season) + " " + getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_row_seasons_header, null);
            holder = new HeaderHolder();
            holder.seasonTV = convertView.findViewById(R.id.seasonTV);
            holder.arrowIV = convertView.findViewById(R.id.arrowIV);
            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }

        if (isExpanded) {
            holder.arrowIV.setImageResource(R.drawable.arrow_up_black_thin);
        } else {
            holder.arrowIV.setImageResource(R.drawable.arrow_down_black_thin);
        }

        holder.seasonTV.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {

        final EpisodeHolder holder;
        final EpisodeDto episodeDto = (EpisodeDto) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_row_episode, null);
            holder = new EpisodeHolder();
            holder.episodeTitleTV = convertView.findViewById(R.id.episodeTitleTV);
            holder.episodeCountTV = convertView.findViewById(R.id.episodeCountTV);
            holder.episodeDateTV = convertView.findViewById(R.id.episodeDateTV);
            holder.episodeWatchedCB = convertView.findViewById(R.id.episodeWatchedCB);
            convertView.setTag(holder);
        } else {
            holder = (EpisodeHolder) convertView.getTag();
        }

        holder.episodeTitleTV.setText(episodeDto.getName());
        holder.episodeCountTV.setText(Utils.getEpisodesText(episodeDto.getSeason(), episodeDto.getNumber()));

        holder.episodeDateTV.setText(Utils.getEpisodeDateText(episodeDto.getAirDate()));

        holder.episodeWatchedCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = holder.episodeWatchedCB.isChecked();
                if(isChecked) {
                    Utils.saveToWatchedList(mContext, showId, episodeDto);
                } else {
                    Utils.removeFromWatchedList(mContext, showId, episodeDto);
                }
            }
        });

        if(Utils.isEpisodeWatched(showId, episodeDto)) {
            holder.episodeWatchedCB.setChecked(true);
        } else {
            holder.episodeWatchedCB.setChecked(false);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class HeaderHolder {
        public RobotoTextView seasonTV;
        private ImageView arrowIV;
    }

    static class EpisodeHolder {
        public RobotoTextView episodeTitleTV;
        public RobotoTextView episodeCountTV;
        public RobotoTextView episodeDateTV;
        public CheckBox episodeWatchedCB;
    }
}
