package com.android.andrewgarver.recipegrabber;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class CustomList_ArrayAdapter extends ArrayAdapter<String>{

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = CustomList_ArrayAdapter.class.getSimpleName();

    /**
     *
     */
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;



    /***************************    Never Used  **************************/

    /**
     *
     *
     * @param context
     * @param web
     * @param imageId
     */
    public CustomList_ArrayAdapter(Activity context,
                                   String[] web, Integer[] imageId) {
        super(context, R.layout.frag_cookbook, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

    /**
     *
     *
     *
     * @param position
     * @param view
     * @param parent
     * @return rowView
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        /**
         *
         */
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        /**
         *
         */
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}