package com.eltonkola.streamscraper.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eltonkola.streamscraper.OnMetaUpdate;
import com.eltonkola.streamscraper.SmartScraper;

import net.moraleboost.streamscraper.Stream;

public class MainActivity extends AppCompatActivity {

    private TextView mStatus;
    private Button button1, button2, button3;
    private SmartScraper mSmartScraper;

    private final String SERVER_SHOUTCAST = "http://live.rkoha.net:2015/;stream.mp3";
    private final String SERVER_ICECAST = "http://funkturm.radio-zusa.net:8000/opus";
    private final String SERVER_UNKNOWN = "http://cdn.gotradio.com/asx/100hitz_top40.asx";

    private final int timeout = 30;

    private OnMetaUpdate onUpdate = new OnMetaUpdate() {
        @Override
        public void onError(Error error) {
            mStatus.append("onError:" + error.toString() + "\n");
        }

        @Override
        public void onUpdate(Stream stream) {
            if(stream.getTitle() !=null && stream.getDescription() !=null ){
                mStatus.append("onUpdate:" + stream.getTitle() + " - " + stream.getDescription() + "\n");
            }else{
                mStatus.append("onUpdate:" + stream.getCurrentSong() + "\n");
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatus = (TextView)findViewById(R.id.status);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSmartScraper!=null) mSmartScraper.reset();
                mSmartScraper =  new SmartScraper(MainActivity.this, SERVER_SHOUTCAST , timeout, onUpdate, SmartScraper.ServerType.SHOUCAST);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSmartScraper!=null) mSmartScraper.reset();
                mSmartScraper =  new SmartScraper(MainActivity.this, SERVER_ICECAST , timeout, onUpdate, SmartScraper.ServerType.ICECAST);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSmartScraper!=null) mSmartScraper.reset();
                mSmartScraper =  new SmartScraper(MainActivity.this, SERVER_UNKNOWN , timeout, onUpdate, SmartScraper.ServerType.ICECAST);
            }
        });


    }
}
