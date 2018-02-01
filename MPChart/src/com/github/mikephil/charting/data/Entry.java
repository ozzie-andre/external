// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import android.os.ParcelFormatException;
import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable
{
    private float mVal;
    private int mXIndex;
    private Object mData;
    public static final Parcelable.Creator<Entry> CREATOR;
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<Entry>() {
            public Entry createFromParcel(final Parcel source) {
                return new Entry(source);
            }
            
            public Entry[] newArray(final int size) {
                return new Entry[size];
            }
        };
    }
    
    public Entry(final float val, final int xIndex) {
        this.mVal = 0.0f;
        this.mXIndex = 0;
        this.mData = null;
        this.mVal = val;
        this.mXIndex = xIndex;
    }
    
    public Entry(final float val, final int xIndex, final Object data) {
        this(val, xIndex);
        this.mData = data;
    }
    
    public int getXIndex() {
        return this.mXIndex;
    }
    
    public void setXIndex(final int x) {
        this.mXIndex = x;
    }
    
    public float getVal() {
        return this.mVal;
    }
    
    public void setVal(final float val) {
        this.mVal = val;
    }
    
    public Object getData() {
        return this.mData;
    }
    
    public void setData(final Object data) {
        this.mData = data;
    }
    
    public Entry copy() {
        final Entry e = new Entry(this.mVal, this.mXIndex, this.mData);
        return e;
    }
    
    public boolean equalTo(final Entry e) {
        return e != null && e.mData == this.mData && e.mXIndex == this.mXIndex && Math.abs(e.mVal - this.mVal) <= 1.0E-5f;
    }
    
    @Override
    public String toString() {
        return "Entry, xIndex: " + this.mXIndex + " val (sum): " + this.getVal();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeFloat(this.mVal);
        dest.writeInt(this.mXIndex);
        if (this.mData != null) {
            if (!(this.mData instanceof Parcelable)) {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }
            dest.writeInt(1);
            dest.writeParcelable((Parcelable)this.mData, flags);
        }
        else {
            dest.writeInt(0);
        }
    }
    
    protected Entry(final Parcel in) {
        this.mVal = 0.0f;
        this.mXIndex = 0;
        this.mData = null;
        this.mVal = in.readFloat();
        this.mXIndex = in.readInt();
        if (in.readInt() == 1) {
            this.mData = in.readParcelable(Object.class.getClassLoader());
        }
    }
}
