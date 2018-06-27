package com.hwx.logprocessor.vo;


import java.util.List;

public class LogTuple {
    private List<Recommendation> recList;
    private LogData ld;

    public LogTuple(List<Recommendation> lst, LogData ld) {
        recList = lst;
        this.ld = ld;
    }


    public List<Recommendation> getRecList() {
        return recList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogTuple tuple = (LogTuple) o;
        return this.recList.equals(tuple.recList);
    }

    @Override
    public int hashCode() {

        return recList.hashCode();
    }

    public LogData getLd() {
        return ld;
    }


}
