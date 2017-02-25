package com.example.rrudz.bullyhound;

import android.app.ListActivity;
import android.os.Bundle;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

 public class TimelineActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        final UserTimeline userTimeline = new UserTimeline.Builder()
        .screenName("fabric")
        .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
        .setTimeline(userTimeline)
        .build();
        setListAdapter(adapter);
    }
}