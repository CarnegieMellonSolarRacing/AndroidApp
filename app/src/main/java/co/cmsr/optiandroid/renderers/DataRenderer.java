package co.cmsr.optiandroid.renderers;

import co.cmsr.optiandroid.datastructures.BoatData;

/**
 * Created by jonbuckley on 4/28/17.
 */

public interface DataRenderer {
    void renderData(float elapsedTime, BoatData dp);
    void printDebug(String val);
}
