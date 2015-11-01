package org.dynamicloud.api;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains data returned from Dynamicloud servers.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/26/15
 **/
public class RecordResults<T> {
    private List<T> records = new LinkedList<>();
    private int totalRecords;
    private int fastReturnedSize;

    /**
     * @return list of records
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * Sets list of records
     *
     * @param records list of records
     */
    protected void setRecords(List<T> records) {
        this.records = records;
    }

    /**
     * @return total records in model
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * Sets total records
     *
     * @param totalRecords total records
     */
    protected void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * Gets the returned size
     *
     * @return returned size
     */
    public int getFastReturnedSize() {
        return fastReturnedSize;
    }

    /**
     * Sets fast returned size
     *
     * @param fastReturnedSize returned size
     */
    protected void setFastReturnedSize(int fastReturnedSize) {
        this.fastReturnedSize = fastReturnedSize;
    }

    @Override
    public String toString() {
        return "RecordResults{" +
                "records=" + records +
                ", totalRecords=" + totalRecords +
                ", fastReturnedSize=" + fastReturnedSize +
                '}';
    }
}