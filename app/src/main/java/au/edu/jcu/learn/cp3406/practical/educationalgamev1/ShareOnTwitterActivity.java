package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class ShareOnTwitterActivity extends AppCompatActivity {
    private TweetAdapter adapter;
    private Twitter twitter = TwitterFactory.getSingleton();
    private User user;
    private List<Tweet> tweets;

    ListView tweetList;
    private TextView tweetMessageView;
    private AppCompatButton seeOtherTweetsButton;

    //define variables that store the quiz information that can be shared via twitter
    //Temporarily assign some value to the variable
    public String userName = "Admin";
    public String subject ="Math";
    public float score = 7.0F;
    public float avgSeconds = (float) 2.9;
    String message;
    String hashtag ="#EducationalGameCommunity";

    //use it to prevent resend the tweet when rotating the screen
    boolean hasSent;
    boolean isDisplayingOtherTweets;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_twitter);
        tweetList = findViewById(R.id.tweets);

        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        tweetList.setAdapter(adapter);

        //get infor from intent
        userName = (String) getIntent().getExtras().get("userName");
        subject = (String) getIntent().getExtras().get("subject");
        score = getIntent().getFloatExtra("score",6.0f);
        avgSeconds = getIntent().getFloatExtra("avgSeconds", 5.5f);

        message = "Congratulation! The student: "+ userName+" has achieved the score: "+ String.format(Locale.getDefault(),"%.2f", score)+" with average "+ String.format(Locale.getDefault(),"%.2f", avgSeconds)+ " seconds per question for "+ subject+" subject!! "+ hashtag;
        tweetMessageView = findViewById(R.id.tweet_message_view);
        tweetMessageView.setText("\""+message+"\"");

        seeOtherTweetsButton = findViewById(R.id.see_other_tweets_button);
        seeOtherTweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeOtherTweets();
            }
        });

        if(savedInstanceState != null){
            hasSent =savedInstanceState.getBoolean("hasSent");
            isDisplayingOtherTweets = savedInstanceState.getBoolean("isDisplayingOtherTweets");
        } else{
            hasSent =false;
            isDisplayingOtherTweets = false;
        }
        if(isDisplayingOtherTweets){
            seeOtherTweets();
        }
    }

    void seeOtherTweets(){
        tweetList.setVisibility(View.VISIBLE);
        isDisplayingOtherTweets = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {
                    //uncomment this line of code
                    if(hasSent == false){
                        try {
                            twitter.updateStatus(message);
                            hasSent =true;
                        } catch (TwitterException ignored) {
                            hasSent =false;
                        }
                    }
                    //uncomment the code below

                    tweets.clear();
                    tweets.addAll(queryTwitter());
                } else {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

//    public void authorise(View view) {
//        Intent intent = new Intent(this, Authenticate.class);
//        startActivity(intent);
//    }

    private boolean isAuthorised() {
        try {
            user = twitter.verifyCredentials();
            Log.i("twitter", "verified");
            return true;
        } catch (Exception e) {
            Log.i("twitter", "not verified");
            return false;
        }
    }

    private List<Tweet> queryTwitter() {
        List<Tweet> results = new ArrayList<>();

        Query query = new Query();
        query.setQuery(hashtag);

        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                String name = status.getUser().getScreenName();
                String message = status.getText();
                Tweet tweet = new Tweet(name, message);
                results.add(tweet);
            }
        } catch (final Exception e) {
            Log.e("ShareOnTwitterActivity", "query error: " + e.getLocalizedMessage());
        }

        return results;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("hasSent", hasSent);
        savedInstanceState.putBoolean("isDisplayingOtherTweets", isDisplayingOtherTweets);
    }
}
