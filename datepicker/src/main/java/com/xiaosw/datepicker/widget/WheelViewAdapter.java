/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xiaosw.datepicker.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.xiaosw.datepicker.view.WheelView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
    public int getItemsCount();

    public View getItem(int index, View convertView, ViewGroup parent);

    public View getEmptyItem(View convertView, ViewGroup parent);

    /**
     * Register an observer that is called when changes happen to the data used
     * by this adapter.
     *
     * @param observer the observer to be registered
     */
    public void registerDataSetObserver(DataSetObserver observer);

    /**
     * Unregister an observer that has previously been registered
     *
     * @param observer the observer to be unregistered
     */
    void unregisterDataSetObserver(DataSetObserver observer);


    /**
     * Abstract Wheel adapter.
     */
    public abstract class AbstractWheelAdapter implements WheelViewAdapter {
        // Observers
        private List<DataSetObserver> datasetObservers;

        @Override
        public View getEmptyItem(View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            if (datasetObservers == null) {
                datasetObservers = new LinkedList<DataSetObserver>();
            }
            datasetObservers.add(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (datasetObservers != null) {
                datasetObservers.remove(observer);
            }
        }

        /**
         * Notifies observers about data changing
         */
        protected void notifyDataChangedEvent() {
            if (datasetObservers != null) {
                for (DataSetObserver observer : datasetObservers) {
                    observer.onChanged();
                }
            }
        }

        /**
         * Notifies observers about invalidating data
         */
        protected void notifyDataInvalidatedEvent() {
            if (datasetObservers != null) {
                for (DataSetObserver observer : datasetObservers) {
                    observer.onInvalidated();
                }
            }
        }
    }


    public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {

        /**
         * Text view resource. Used as a default view for adapter.
         */
        public static final int TEXT_VIEW_ITEM_RESOURCE = -1;

        /**
         * No resource constant.
         */
        protected static final int NO_RESOURCE = 0;

        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF101010;

        /**
         * Default text color
         */
        public static final int LABEL_COLOR = 0xFF700070;

        /**
         * Default text size
         */
        public static final int DEFAULT_MAX_TEXT_SIZE = 24;
        public static final int DEFAULT_MIN_TEXT_SIZE = 14;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;
        private int textSize = DEFAULT_MAX_TEXT_SIZE;

        // Current context
        protected Context context;
        // Layout inflater
        protected LayoutInflater inflater;

        // Items resources
        protected int itemResourceId;
        protected int itemTextResourceId;

        // Empty items resources
        protected int emptyItemResourceId;

        private int currentIndex = 0;
        private int mMaxTextSize = DEFAULT_MAX_TEXT_SIZE;
        private int mMinTextSize = DEFAULT_MIN_TEXT_SIZE;
        private ArrayList<View> arrayList = new ArrayList<View>();

        /**
         * Constructor
         *
         * @param context the current context
         */
        protected AbstractWheelTextAdapter(Context context) {
            this(context, TEXT_VIEW_ITEM_RESOURCE);
        }

        /**
         * Constructor
         *
         * @param context      the current context
         * @param itemResource the resource ID for a layout file containing a TextView to use
         *                     when instantiating items views
         */
        protected AbstractWheelTextAdapter(Context context, int itemResource) {
            this(context, itemResource, NO_RESOURCE, 0, DEFAULT_MAX_TEXT_SIZE, DEFAULT_MIN_TEXT_SIZE);
        }

        /**
         * Constructor
         *
         * @param context          the current context
         * @param itemResource     the resource ID for a layout file containing a TextView to use
         *                         when instantiating items views
         * @param itemTextResource the resource ID for a text view in the item layout
         */
        protected AbstractWheelTextAdapter(Context context, int itemResource, int itemTextResource, int currentIndex,
                                           int maxsize, int minsize) {
            this.context = context;
            itemResourceId = itemResource;
            itemTextResourceId = itemTextResource;
            this.currentIndex = currentIndex;
            this.mMaxTextSize = maxsize;
            this.mMinTextSize = minsize;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         * get the mDateDatas of show textview
         *
         * @return the array of textview
         */
        public ArrayList<View> getTestViews() {
            return arrayList;
        }

        /**
         * Gets text color
         *
         * @return the text color
         */
        public int getTextColor() {
            return textColor;
        }

        /**
         * Sets text color
         *
         * @param textColor the text color to set
         */
        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        /**
         * Gets text size
         *
         * @return the text size
         */
        public int getTextSize() {
            return textSize;
        }

        /**
         * Sets text size
         *
         * @param textSize the text size to set
         */
        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        /**
         * Gets resource Id for items views
         *
         * @return the item resource Id
         */
        public int getItemResource() {
            return itemResourceId;
        }

        /**
         * Sets resource Id for items views
         *
         * @param itemResourceId the resource Id to set
         */
        public void setItemResource(int itemResourceId) {
            this.itemResourceId = itemResourceId;
        }

        /**
         * Gets resource Id for text view in item layout
         *
         * @return the item text resource Id
         */
        public int getItemTextResource() {
            return itemTextResourceId;
        }

        /**
         * Sets resource Id for text view in item layout
         *
         * @param itemTextResourceId the item text resource Id to set
         */
        public void setItemTextResource(int itemTextResourceId) {
            this.itemTextResourceId = itemTextResourceId;
        }

        /**
         * Gets resource Id for empty items views
         *
         * @return the empty item resource Id
         */
        public int getEmptyItemResource() {
            return emptyItemResourceId;
        }

        /**
         * Sets resource Id for empty items views
         *
         * @param emptyItemResourceId the empty item resource Id to set
         */
        public void setEmptyItemResource(int emptyItemResourceId) {
            this.emptyItemResourceId = emptyItemResourceId;
        }

        /**
         * Returns text for specified item
         *
         * @param index the item index
         * @return the text of specified items
         */
        protected abstract CharSequence getItemText(int index);

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            if (index >= 0 && index < getItemsCount()) {
                if (convertView == null) {
                    convertView = getView(itemResourceId, parent);
                }
                TextView textView = getTextView(convertView, itemTextResourceId);
                if (!arrayList.contains(textView)) {
                    arrayList.add(textView);
                }
                if (textView != null) {
                    CharSequence text = getItemText(index);
                    if (text == null) {
                        text = "";
                    }
                    textView.setText(text);
                    Log.i("aa", "index:" + index + "     currentIndex:" + currentIndex);
                    if (index == currentIndex) {
                        textView.setTextSize(mMaxTextSize);
                    } else {
                        textView.setTextSize(mMinTextSize);
                    }

                    if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                        configureTextView(textView);
                    }
                }
                return convertView;
            }
            return null;
        }

        @Override
        public View getEmptyItem(View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getView(emptyItemResourceId, parent);
            }
            if (emptyItemResourceId == TEXT_VIEW_ITEM_RESOURCE && convertView instanceof TextView) {
                configureTextView((TextView) convertView);
            }

            return convertView;
        }

        /**
         * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
         *
         * @param view the text view to be configured
         */
        protected void configureTextView(TextView view) {
            view.setTextColor(textColor);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(textSize);
            view.setLines(1);
            view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        }

        /**
         * Loads a text view from view
         *
         * @param view         the text view or layout containing it
         * @param textResource the text resource Id in layout
         * @return the loaded text view
         */
        private TextView getTextView(View view, int textResource) {
            TextView text = null;
            try {
                if (textResource == NO_RESOURCE && view instanceof TextView) {
                    text = (TextView) view;
                } else if (textResource != NO_RESOURCE) {
                    text = (TextView) view.findViewById(textResource);
                }
            } catch (ClassCastException e) {
                Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", e);
            }

            return text;
        }

        /**
         * Loads view from resources
         *
         * @param resource the resource Id
         * @return the loaded view or null if resource is not set
         */
        private View getView(int resource, ViewGroup parent) {
            switch (resource) {
                case NO_RESOURCE:
                    return null;
                case TEXT_VIEW_ITEM_RESOURCE:
                    return new TextView(context);
                default:
                    return inflater.inflate(resource, parent, false);
            }
        }

        /**
         * 设置字体大小
         */
        public String updateTextStyle(int position) {
            ArrayList<View> arrayList = getTestViews();
            int size = arrayList.size();
            String currentDateText = getItemText(position).toString().trim();
            if (TextUtils.isEmpty(currentDateText)) {
                return null;
            }
            for (int i = 0; i < size; i++) {
                TextView textvew = (TextView) arrayList.get(i);
                if (textvew.getText().toString().equals(currentDateText)) {
                    textvew.setTextSize(mMaxTextSize);
                } else {
                    textvew.setTextSize(mMinTextSize);
                }
            }
            return currentDateText;
        }
    }

    public class AdapterWheel extends AbstractWheelTextAdapter {
        private WheelAdapter adapter;

        public AdapterWheel(Context context, WheelAdapter adapter) {
            super(context);

            this.adapter = adapter;
        }

        public WheelAdapter getAdapter() {
            return adapter;
        }

        @Override
        public int getItemsCount() {
            return adapter.getItemsCount();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return adapter.getItem(index);
        }

    }

    public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

        // items
        private T[] items;

        public ArrayWheelAdapter(Context context, T[] items) {
            super(context);
            // setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
            this.items = items;
        }

        @Override
        public CharSequence getItemText(int index) {
            if (index >= 0 && index < items.length) {
                T item = items[index];
                if (item instanceof CharSequence) {
                    return (CharSequence) item;
                }
                return item.toString();
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            return items.length;
        }
    }

    /**
     * Numeric Wheel adapter.
     */
    public class NumericWheelAdapter extends AbstractWheelTextAdapter {

        /**
         * The default min value
         */
        public static final int DEFAULT_MAX_VALUE = 9;

        /**
         * The default max value
         */
        private static final int DEFAULT_MIN_VALUE = 0;

        // Values
        private int minValue;
        private int maxValue;

        // format
        private String format;

        /**
         * Constructor
         *
         * @param context the current context
         */
        public NumericWheelAdapter(Context context) {
            this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
        }

        /**
         * Constructor
         *
         * @param context  the current context
         * @param minValue the wheel min value
         * @param maxValue the wheel max value
         */
        public NumericWheelAdapter(Context context, int minValue, int maxValue) {
            this(context, minValue, maxValue, null);
        }

        /**
         * Constructor
         *
         * @param context  the current context
         * @param minValue the wheel min value
         * @param maxValue the wheel max value
         * @param format   the format string
         */
        public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
            super(context);

            this.minValue = minValue;
            this.maxValue = maxValue;
            this.format = format;
        }

        @Override
        public CharSequence getItemText(int index) {
            if (index >= 0 && index < getItemsCount()) {
                int value = minValue + index;
                return format != null ? String.format(format, value) : Integer.toString(value);
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            return maxValue - minValue + 1;
        }
    }

    /**
     * Wheel adapter interface
     *
     * @deprecated Use WheelViewAdapter
     */
    public interface WheelAdapter {
        /**
         * Gets items count
         *
         * @return the count of wheel items
         */
        public int getItemsCount();

        /**
         * Gets a wheel item by index.
         *
         * @param index the item index
         * @return the wheel item text or null
         */
        public String getItem(int index);

        /**
         * Gets maximum item length. It is used to determine the wheel width. If -1
         * is returned there will be used the default wheel width.
         *
         * @return the maximum item length or -1
         */
        public int getMaximumLength();


        public class WheelScroller {
            /**
             * Scrolling listener interface
             */
            public interface ScrollingListener {
                /**
                 * Scrolling callback called when scrolling is performed.
                 *
                 * @param distance the distance to scroll
                 */
                void onScroll(int distance);

                /**
                 * Starting callback called when scrolling is started
                 */
                void onStarted();

                /**
                 * Finishing callback called after justifying
                 */
                void onFinished();

                /**
                 * Justifying callback called to justify a view when scrolling is ended
                 */
                void onJustify();
            }

            /**
             * Scrolling duration
             */
            private static final int SCROLLING_DURATION = 400;

            /**
             * Minimum delta for scrolling
             */
            public static final int MIN_DELTA_FOR_SCROLLING = 1;

            // Listener
            private ScrollingListener listener;

            // Context
            private Context context;

            // Scrolling
            private GestureDetector gestureDetector;
            private Scroller scroller;
            private int lastScrollY;
            private float lastTouchedY;
            private boolean isScrollingPerformed;

            /**
             * Constructor
             *
             * @param context  the current context
             * @param listener the scrolling listener
             */
            public WheelScroller(Context context, ScrollingListener listener) {
                gestureDetector = new GestureDetector(context, gestureListener);
                gestureDetector.setIsLongpressEnabled(false);

                scroller = new Scroller(context);

                this.listener = listener;
                this.context = context;
            }

            /**
             * Set the the specified scrolling interpolator
             *
             * @param interpolator the interpolator
             */
            public void setInterpolator(Interpolator interpolator) {
                scroller.forceFinished(true);
                scroller = new Scroller(context, interpolator);
            }

            /**
             * Scroll the wheel
             *
             * @param distance the scrolling distance
             * @param time     the scrolling duration
             */
            public void scroll(int distance, int time) {
                scroller.forceFinished(true);

                lastScrollY = 0;

                scroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
                setNextMessage(messageScroll);

                startScrolling();
            }

            /**
             * Stops scrolling
             */
            public void stopScrolling() {
                scroller.forceFinished(true);
            }

            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchedY = event.getY();
                        scroller.forceFinished(true);
                        clearMessages();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // perform scrolling
                        int distanceY = (int) (event.getY() - lastTouchedY);
                        if (distanceY != 0) {
                            startScrolling();
                            listener.onScroll(distanceY);
                            lastTouchedY = event.getY();
                        }
                        break;
                    default:
                        break;
                }

                if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
                    justify();
                }

                return true;
            }

            // gesture listener
            private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    // Do scrolling in onTouchEvent() since onScroll() are not call
                    // immediately
                    // when user touch and move the wheel
                    return true;
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    lastScrollY = 0;
                    final int maxY = 0x7FFFFFFF;
                    final int minY = -maxY;
                    scroller.fling(0, lastScrollY, 0, (int) -velocityY, 0, 0, minY, maxY);
                    setNextMessage(messageScroll);
                    return true;
                }
            };

            // Messages
            private final int messageScroll = 0;
            private final int messageJustify = 1;

            /**
             * Set next message to queue. Clears queue before.
             *
             * @param message the message to set
             */
            private void setNextMessage(int message) {
                clearMessages();
                animationHandler.sendEmptyMessage(message);
            }

            /**
             * Clears messages from queue
             */
            private void clearMessages() {
                animationHandler.removeMessages(messageScroll);
                animationHandler.removeMessages(messageJustify);
            }

            // animation handler
            private Handler animationHandler = new Handler() {
                public void handleMessage(Message msg) {
                    scroller.computeScrollOffset();
                    int currY = scroller.getCurrY();
                    int delta = lastScrollY - currY;
                    lastScrollY = currY;
                    if (delta != 0) {
                        listener.onScroll(delta);
                    }

                    // scrolling is not finished when it comes to final Y
                    // so, finish it manually
                    if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                        currY = scroller.getFinalY();
                        scroller.forceFinished(true);
                    }
                    if (!scroller.isFinished()) {
                        animationHandler.sendEmptyMessage(msg.what);
                    } else if (msg.what == messageScroll) {
                        justify();
                    } else {
                        finishScrolling();
                    }
                }
            };

            /**
             * Justifies wheel
             */
            private void justify() {
                listener.onJustify();
                setNextMessage(messageJustify);
            }

            /**
             * Starts scrolling
             */
            private void startScrolling() {
                if (!isScrollingPerformed) {
                    isScrollingPerformed = true;
                    listener.onStarted();
                }
            }

            /**
             * Finishes scrolling
             */
            void finishScrolling() {
                if (isScrollingPerformed) {
                    listener.onFinished();
                    isScrollingPerformed = false;
                }
            }
        }
    }


    /**
     * Wheel clicked listener interface.
     * <p/>
     * The onItemClicked() method is called whenever a wheel item is clicked
     * <li>New Wheel position is set
     * <li>Wheel view is scrolled
     */
    public interface OnWheelClickedListener {
        /**
         * Callback method to be invoked when current item clicked
         *
         * @param wheel     the wheel view
         * @param itemIndex the index of clicked item
         */
        void onItemClicked(WheelView wheel, int itemIndex);

        public interface OnWheelChangedListener {
            /**
             * Callback method to be invoked when current item changed
             *
             * @param wheel    the wheel view whose state has changed
             * @param oldValue the old value of current item
             * @param newValue the new value of current item
             */
            void onChanged(WheelView wheel, int oldValue, int newValue);
        }

        public interface OnWheelScrollListener {
            /**
             * Callback method to be invoked when scrolling started.
             *
             * @param wheel the wheel view whose state has changed.
             */
            void onScrollingStarted(WheelView wheel);

            /**
             * Callback method to be invoked when scrolling ended.
             *
             * @param wheel the wheel view whose state has changed.
             */
            void onScrollingFinished(WheelView wheel);
        }
    }

}
