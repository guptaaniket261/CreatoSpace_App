package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.cards;
import com.example.myapplication.R;

import java.util.List;
// the work of arrayAdapter is to simply populate the xml file by taking it from another resource
public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items)
    {
        super(context,resourceId,items);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        cards cards_item = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.item_name);
        TextView age = (TextView)convertView.findViewById(R.id.item_age);
        TextView qualification = (TextView)convertView.findViewById(R.id.item_qualification);
        TextView skills = (TextView)convertView.findViewById(R.id.item_skills);
        TextView experience = (TextView)convertView.findViewById(R.id.item_Experience);

        ImageView image = (ImageView)convertView.findViewById(R.id.item_image);

        name.setText(cards_item.getName());
        age.setText(cards_item.getAge());
        qualification.setText(cards_item.getQualification());
        skills.setText(cards_item.getSkills());
        experience.setText(cards_item.getExperience());

        switch (cards_item.getProfileImageUrl())
        {
            case "default":
                //Toast.makeText(convertView.getContext(),"image found",Toast.LENGTH_SHORT).show();
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher_round).into(image);
                break;

            default:
                //Toast.makeText(convertView.getContext(),"true image",Toast.LENGTH_SHORT).show();
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(cards_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }
}