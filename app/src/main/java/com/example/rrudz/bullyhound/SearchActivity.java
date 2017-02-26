package com.example.rrudz.bullyhound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;


public class SearchActivity extends Activity{

        EditText edtSearch;
        Button btnSearch;
        private final String TWIT_CONS_KEY = "H8KFZggtlb6revjqZ2ADVD1Fo";
        private final String TWIT_CONS_SEC_KEY = "1Omxt717VmKS3mGExZjFXYjist6iLMl2B8hHZ0wUUBYanDB0AV";
        ListView list;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            edtSearch = (EditText) findViewById(R.id.edtSearch);
            btnSearch = (Button) findViewById(R.id.btnSearch);
            list = (ListView) findViewById(R.id.list);
            btnSearch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new SearchOnTwitter().execute(edtSearch.getText().toString());
                }
            });
        }

        class SearchOnTwitter extends AsyncTask<String, Void, Integer> {
            ArrayList<Tweet> tweets;
            final int SUCCESS = 0;
            final int FAILURE = SUCCESS + 1;
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(getApplicationContext(), "", getString(R.string.searching));
            }

            @Override
            protected Integer doInBackground(String... params) {
                try {
                    ConfigurationBuilder builder = new ConfigurationBuilder();
                    builder.setApplicationOnlyAuthEnabled(true);
                    builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                    builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

                    OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

                    builder = new ConfigurationBuilder();
                    builder.setApplicationOnlyAuthEnabled(true);
                    builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                    builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
                    builder.setOAuth2TokenType(token.getTokenType());
                    builder.setOAuth2AccessToken(token.getAccessToken());

                    Twitter twitter = new TwitterFactory(builder.build()).getInstance();

                    Query query = new Query(params[0]);
                    // YOu can set the count of maximum records here
                    query.setCount(50);
                    QueryResult result;
                    result = twitter.search(query);
                    List<twitter4j.Status> tweets = result.getTweets();
                    StringBuilder str = new StringBuilder();
                    if (tweets != null) {
                        this.tweets = new ArrayList<Tweet>();
                        for (twitter4j.Status tweet : tweets) {
                            str.append("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n");
                            System.out.println(str);
                            this.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText()));
                        }
                        return SUCCESS;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return FAILURE;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                dialog.dismiss();
                if (result == SUCCESS) {
                    list.setAdapter(new TweetAdapter(getApplicationContext(), tweets));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            }
        }
    }