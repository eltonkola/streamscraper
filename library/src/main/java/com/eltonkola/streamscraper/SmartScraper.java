package com.eltonkola.streamscraper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.moraleboost.streamscraper.Stream;
import net.moraleboost.streamscraper.scraper.IceCastScraper;
import net.moraleboost.streamscraper.scraper.ShoutCastScraper;
import net.moraleboost.streamscraper.util.CharsetUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elton on 19/01/2016.
 */
public class SmartScraper {

    public enum ServerType{
        SHOUCAST, ICECAST, UNKNOWN;
    }

    private IceCastScraper mIceCastScraper;
    private ShoutCastScraper mShoutCastScraper;

    private int mInterval;
    private String mUrl;

    public static Context sContext;
    private OnMetaUpdate.Error mError = OnMetaUpdate.Error.NoError;

    public SmartScraper(final Context context, final String url, final int interval, final OnMetaUpdate onUpdate) {
        this(context, url, interval, onUpdate, ServerType.UNKNOWN);
    }

    public SmartScraper(final Context context, final String url, final int interval, final OnMetaUpdate onUpdate, final ServerType serverType) {
        mIceCastScraper = new IceCastScraper();
        mShoutCastScraper = new ShoutCastScraper();
        mInterval = interval;
        mUrl = url;
        sContext = context;
        new GetData(serverType, onUpdate).execute();

    }

    public void reset() {
        mIceCastScraper = null;
        mShoutCastScraper = null;
    }


    private class GetData extends AsyncTask<Void, Void, Void> {

        final ServerType serverType;
        final OnMetaUpdate onUpdate;

        private List<Stream> streams = new ArrayList<>();

        public GetData(final ServerType serverType, final OnMetaUpdate onUpdate) {
            this.serverType = serverType;
            this.onUpdate = onUpdate;
            Log.v("eltonkola", "----------------------");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("eltonkola", "on post execute");
            Log.v("eltonkola", "streams:" + streams);
            Log.v("eltonkola", "mError:" + mError);
            if (streams != null && streams.size() > 0) {
                for (Stream stream : streams) {
                    System.out.println("Song Title: " + stream.getCurrentSong());
                    System.out.println("URI: " + stream.getUri());
                    onUpdate.onUpdate(stream);
                }
            }
            if(mError != OnMetaUpdate.Error.NoError) {
                onUpdate.onError(mError);
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("eltonkola", "doInBackground serverType:" + serverType);
            if (serverType == ServerType.ICECAST) {
                step2();
            } else {
                step1(serverType == ServerType.UNKNOWN);
            }
            return null;
        }


        private void step1(boolean goToNext) {
            Log.v("eltonkola", "step1 goToNext:" + goToNext);
            try {
                streams.clear();
                streams = mShoutCastScraper.scrape(new URI(mUrl));
                if(streams.size() == 0){
                    mError = OnMetaUpdate.Error.NoData;
                    if(goToNext) {
                        step2();
                    }
                }
            } catch (Exception e) {
                Log.v("eltonkola", "step1 error:" + e.getMessage());
                e.printStackTrace();
                mError = OnMetaUpdate.Error.ServerUnknown;
                if(goToNext) {
                    step2();
                }
            }
        }

        public void step2() {
            Log.v("eltonkola", "step2");
            try {
                streams.clear();
                streams = mIceCastScraper.scrape(new URI(mUrl));
                if(streams.size() == 0) {
                    mError = OnMetaUpdate.Error.NoData;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("eltonkola", "step2 error:" + e.getMessage());
                mError = OnMetaUpdate.Error.ServerUnknown;

            }
        }
    }

}