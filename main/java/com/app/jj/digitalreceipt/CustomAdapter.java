package com.app.jj.digitalreceipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jey on 01/07/2016.
 */
public class CustomAdapter extends ArrayAdapter<ReceiptData> {

    public CustomAdapter(Context context, ArrayList<ReceiptData> receipts) {
        super(context,R.layout.customlistview , receipts);
    }


    //This controls how the strings that were passed in are laid out
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflate = prepare or get ready for rendering
        //context = background information
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //this is equal to one custom row(view)
        View customView = inflater.inflate(R.layout.customlistview,parent,false);

        ReceiptData singleReceiptData = getItem(position);
        TextView headline = (TextView) customView.findViewById(R.id.title_id);
        TextView subheading = (TextView) customView.findViewById(R.id.subhead_id);

        headline.setText(singleReceiptData.getReceiptName());
        subheading.setText(singleReceiptData.getCompanyName());
        notifyDataSetChanged();

        return customView;
    }
}
