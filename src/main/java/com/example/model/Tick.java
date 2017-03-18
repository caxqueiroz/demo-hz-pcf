package com.example.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * {"tick":{"instrument":"EUR_USD","time":"1461396366924145","bid":1.13929,"ask":1.14029}}
 * Created by cq on 11/2/17.
 */
public class Tick implements Comparable<Tick>, DataSerializable {

    private static final Long serialVersionUID = 1L;

    private long time;

    private String instrument;

    private double bid;

    private double ask;

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long dateTime) {
        this.time = dateTime;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tick{");
        sb.append("instrument='").append(instrument).append('\'');
        sb.append(", time=").append(time);
        sb.append(", bid=").append(bid);
        sb.append(", ask=").append(ask);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Tick that) {
        int instrumentCompare = this.instrument.compareTo(that.getInstrument());
        return instrumentCompare;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(this.time);
        objectDataOutput.writeUTF(this.instrument);
        objectDataOutput.writeDouble(this.bid);
        objectDataOutput.writeDouble(this.ask);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.time = objectDataInput.readLong();
        this.instrument = objectDataInput.readUTF();
        this.bid = objectDataInput.readDouble();
        this.ask = objectDataInput.readDouble();
    }
}