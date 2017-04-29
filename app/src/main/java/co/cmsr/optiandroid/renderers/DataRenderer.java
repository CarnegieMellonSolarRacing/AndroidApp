package co.cmsr.optiandroid.renderers;

import co.cmsr.optiandroid.datastructures.BoatData;

/**
 * Created by jonbuckley on 4/28/17.
 */

public interface DataRenderer {
    void onPacketParsed(float elapsedTime, BoatData dp);
}
