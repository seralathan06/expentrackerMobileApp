package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<String> {
    public ExpenseAdapter(Context context, ArrayList<String> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String expense = getItem(position);
        TextView expenseTextView = convertView.findViewById(android.R.id.text1);
        expenseTextView.setText(expense);

        return convertView;
    }
}
