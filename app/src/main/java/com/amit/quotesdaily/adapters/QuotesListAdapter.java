package com.amit.quotesdaily.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.quotesdaily.R;
import com.amit.quotesdaily.activity.SingleQuote;

import java.util.List;

/**
 * Created by Amit on 27-Mar-19.
 */
public class QuotesListAdapter extends RecyclerView.Adapter<QuotesListAdapter.QuoteViewHolder> {

    private List<String> quotesList;
    private String quotesTitleList;
    private Context context;

    public QuotesListAdapter(List<String> quotesList, String quoteTitle, Context context) {
        this.quotesList = quotesList;
        this.context = context;
        this.quotesTitleList = quoteTitle;
    }

    @NonNull
    @Override
    public QuotesListAdapter.QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_row_item, viewGroup, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesListAdapter.QuoteViewHolder quotesViewHolder, int position) {
        String[] colors = {"#1faa00", "#E91E63", "#ff8f00", "#9e9d24", "#0097a7", "#7b1fa2",
                "#795548", "#d81b60", "#f44336"};
        final String quote = quotesList.get(position);
        //final String quoteTitle = quotesTitleList.get(position);
        quotesViewHolder.txtQuote.setText(quote);
        quotesViewHolder.txtQuoteTitle.setText(quotesTitleList);
        int color = position % colors.length;
        final int intColor = Color.parseColor(colors[color]);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts" +
                "/PlayfairDisplayRegular.ttf");
        if (position == 0) {
            quotesViewHolder.txtQuote.setPadding(0, 0, 0, -13);
        }
        if (position == (quotesList.size() - 1)) {
            quotesViewHolder.txtQuote.setPadding(0, -13, 0, 0);
        }
        if (position % 2 == 0) {
            quotesViewHolder.txtQuote.setTypeface(custom_font);
        } else {
            quotesViewHolder.txtQuote.setTypeface(custom_font);
        }
        quotesViewHolder.txtQuoteTitle.setTypeface(custom_font);
        quotesViewHolder.quoteContainer.setBackgroundColor(intColor);

        quotesViewHolder.quoteContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, context.getString(R.string.app_name) + " - " + "\n" + quote, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        quotesViewHolder.quoteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent category = new Intent(view.getContext(), SingleQuote.class);
                category.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Bundle b = new Bundle();
                b.putSerializable("quote", quote);
                b.putSerializable("quoteTitle", quotesTitleList);
                category.putExtras(b); //pass bundle to your intent
                context.startActivity(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }

    class QuoteViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuote;
        TextView txtQuoteTitle;
        LinearLayout quoteContainer;

        QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuote = itemView.findViewById(R.id.txtQuote);
            txtQuoteTitle = itemView.findViewById(R.id.txtQuoteTitle);
            quoteContainer = itemView.findViewById(R.id.quoteContainer);
        }

    }
}
