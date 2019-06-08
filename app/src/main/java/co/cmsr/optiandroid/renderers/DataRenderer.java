package co.cmsr.optiandroid.renderers;

import java.util.LinkedList;

import co.cmsr.optiandroid.datastructures.DataPacket;

/**
 * Created by jonbuckley on 4/28/17.
 */

public interface DataRenderer {
    void renderData(LinkedList<DataPacket> dp_list);
    void printDebug(String val);
}
