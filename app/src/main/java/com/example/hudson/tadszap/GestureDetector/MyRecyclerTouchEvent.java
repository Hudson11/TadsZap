package com.example.hudson.tadszap.GestureDetector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MyRecyclerTouchEvent implements RecyclerView.OnItemTouchListener {


    public interface OnItemClickListener{
        void mapLinkTouchEvent(View view, int position);
    }

    OnItemClickListener myListener;
    GestureDetector myGestureDetector;

    public MyRecyclerTouchEvent(Context context, final RecyclerView view, OnItemClickListener listener) {

        myListener = listener;
        myGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e){
                super.onSingleTapUp(e);
                View childView = view.findChildViewUnder(e.getX(), e.getY());
                if(childView != null && myListener != null)
                    myListener.mapLinkTouchEvent(childView, view.getChildAdapterPosition(childView));
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        myGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
