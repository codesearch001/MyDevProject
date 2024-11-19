package com.snofed.publicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    // Array of images
    int image[] = {
            R.drawable.boarding1,
            R.drawable.boarding2,
            R.drawable.boarding3,
            R.drawable.boarding4,
            R.drawable.boarding5
    };

    // Array of text parts for each slide
    String[][] texts = {
            {"Check out all of the activitie, facilities and features that are in offer."},
            {"Take a closer look at your favourite trails"},
            {"Know what you want? Use the filters app to show you what you're interested in."},
            {"The Snofed app can also link you other great services like tickets, timetable and social media"},
            {"Got something to say? You can leave your feedback via the app too, it's simple!"}
    };

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        //Find the ImageView and TextView in the layout
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView textView = view.findViewById(R.id.slider_text);

        // Set the image for the current position
        imageView.setImageResource(image[position]);

        // Concatenate the text parts with line breaks for a single TextView
        StringBuilder combinedText = new StringBuilder();
        for (String text : texts[position]) {
            combinedText.append(text).append("\n"); // Adds line breaks between each text part
        }

        // Set the concatenated text to the TextView
        textView.setText(combinedText.toString());

        // Add the view to the container
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

