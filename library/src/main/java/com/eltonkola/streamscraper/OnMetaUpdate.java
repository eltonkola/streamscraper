package com.eltonkola.streamscraper;

import net.moraleboost.streamscraper.Stream;

/**
 * Created by Elton on 19/01/2016.
 */
public interface OnMetaUpdate {

    enum Error{
        ServerUnknown ,
        ConnectionError ,
        NoData,
        NoError
    }

    void onError(Error error);
    void onUpdate(Stream stream);

}
