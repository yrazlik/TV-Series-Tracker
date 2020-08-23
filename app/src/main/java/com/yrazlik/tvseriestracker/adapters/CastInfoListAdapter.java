package com.yrazlik.tvseriestracker.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.CastDto;
import com.yrazlik.tvseriestracker.data.CharacterDto;
import com.yrazlik.tvseriestracker.data.ImageDto;
import com.yrazlik.tvseriestracker.data.PersonDto;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class CastInfoListAdapter extends ArrayAdapter<CastDto> {

    private Context mContext;
    private List<CastDto> cast;

    public CastInfoListAdapter(Context context, int resource, List<CastDto> cast) {
        super(context, resource, cast);
        this.mContext = context;
        this.cast = cast;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_cast, parent, false);

            holder = new ViewHolder();
            holder.castIV = convertView.findViewById(R.id.castIV);
            holder.personTV = convertView.findViewById(R.id.personTV);
            holder.characterTV = convertView.findViewById(R.id.characterTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CastDto cast = getItem(position);
        PersonDto personDto = cast.getPerson();
        CharacterDto characterDto = cast.getCharacter();
        ImageDto castImg = cast.getPerson() == null ? null : cast.getPerson().getImage();

        PicassoImageLoader.getInstance(mContext).loadImage(castImg != null ? castImg.getMedium() : "", holder.castIV);
        holder.personTV.setText(personDto != null ? personDto.getName() : "-");
        holder.characterTV.setText(characterDto != null ? "as " + characterDto.getName() : "-");

        return convertView;
    }

    static class ViewHolder {
        public ImageView castIV;
        public RobotoTextView personTV;
        public RobotoTextView characterTV;
    }
}
