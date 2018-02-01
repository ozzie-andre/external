// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<?>>
{
    private LineData mLineData;
    private BarData mBarData;
    private ScatterData mScatterData;
    private CandleData mCandleData;
    private BubbleData mBubbleData;
    
    public CombinedData() {
    }
    
    public CombinedData(final List<String> xVals) {
        super(xVals);
    }
    
    public CombinedData(final String[] xVals) {
        super(xVals);
    }
    
    public void setData(final LineData data) {
        this.mLineData = data;
        //this.mDataSets.addAll((Collection<? extends T>)data.getDataSets());
        this.mDataSets.addAll(data.getDataSets()); 
        this.init();
    }
    
    public void setData(final BarData data) {
        this.mBarData = data;
        //this.mDataSets.addAll((Collection<? extends T>)data.getDataSets());
        this.mDataSets.addAll(data.getDataSets()); 
        this.init();
    }
    
    public void setData(final ScatterData data) {
        this.mScatterData = data;
        //this.mDataSets.addAll((Collection<? extends T>)data.getDataSets());
        this.mDataSets.addAll(data.getDataSets());
        this.init();
    }
    
    public void setData(final CandleData data) {
        this.mCandleData = data;
        //this.mDataSets.addAll((Collection<? extends T>)data.getDataSets());
        this.mDataSets.addAll(data.getDataSets());
        this.init();
    }
    
    public void setData(final BubbleData data) {
        this.mBubbleData = data;
        //this.mDataSets.addAll((Collection<? extends T>)data.getDataSets());
        this.mDataSets.addAll(data.getDataSets());
        this.init();
    }
    
    public BubbleData getBubbleData() {
        return this.mBubbleData;
    }
    
    public LineData getLineData() {
        return this.mLineData;
    }
    
    public BarData getBarData() {
        return this.mBarData;
    }
    
    public ScatterData getScatterData() {
        return this.mScatterData;
    }
    
    public CandleData getCandleData() {
        return this.mCandleData;
    }
    
    @SuppressWarnings("rawtypes")
	public List<ChartData> getAllData() {
        final List<ChartData> data = new ArrayList<ChartData>();
        if (this.mLineData != null) {
            data.add(this.mLineData);
        }
        if (this.mBarData != null) {
            data.add(this.mBarData);
        }
        if (this.mScatterData != null) {
            data.add(this.mScatterData);
        }
        if (this.mCandleData != null) {
            data.add(this.mCandleData);
        }
        if (this.mBubbleData != null) {
            data.add(this.mBubbleData);
        }
        return data;
    }
    
    @Override
    public void notifyDataChanged() {
        if (this.mLineData != null) {
            this.mLineData.notifyDataChanged();
        }
        if (this.mBarData != null) {
            this.mBarData.notifyDataChanged();
        }
        if (this.mCandleData != null) {
            this.mCandleData.notifyDataChanged();
        }
        if (this.mScatterData != null) {
            this.mScatterData.notifyDataChanged();
        }
        if (this.mBubbleData != null) {
            this.mBubbleData.notifyDataChanged();
        }
        this.init();
    }
}
