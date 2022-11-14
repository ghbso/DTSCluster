/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence;

import java.util.List;
import model.patterndiscovery.TimeSeries;

/**
 *
 * @author Josenildo
 */
public interface TimeSeriesDAO {

    /**
     * Loads timeseries data for a given file name and returns a TimeSeries object.
     * TimeSeries is based on a Vector of Double.
     * @param fileName is file path
     * @return a TimeSeries
     * @throws java.io.IOException if filepath is invalid
     */
    TimeSeries load(String fileName) throws Exception;

    List<TimeSeries> load(String fileName, int size) throws Exception;

    /**
     *
     * @param ts is a time series to be saved
     * @param fileName is the file name where the time series must be saved
     */
    void save(TimeSeries ts, String fileName);

}
