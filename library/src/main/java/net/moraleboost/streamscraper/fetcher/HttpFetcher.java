/*
 **
 **  Jul. 20, 2009
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.streamscraper.fetcher;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.eltonkola.streamscraper.ByteRequest;
import com.eltonkola.streamscraper.SmartScraper;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import net.moraleboost.streamscraper.FetchException;
import net.moraleboost.streamscraper.Fetcher;

public class HttpFetcher implements Fetcher
{
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (compatible; StreamScraper/1.0; +http://code.google.com/p/streamscraper/)";

    public byte[] fetch(URI uri) throws FetchException
    {
        RequestFuture<byte[]> future = RequestFuture.newFuture();
        RequestFuture<String> error = RequestFuture.newFuture();
        ByteRequest request = new ByteRequest(uri.toString(), future, error);
        Volley.newRequestQueue(SmartScraper.sContext).add(request);
        try {
            byte[] response = future.get(10000, TimeUnit.SECONDS);
            return response;
        } catch (Exception e) {
            throw new FetchException(e);
        }
    }

}
