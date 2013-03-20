package com.itextpdf.text;

import java.util.*;
import java.util.List;

public class TabSettings {
    static public final float DEFAULT_TAB_INTERVAL = 36;

    public static TabStop getTabStopNewInstance(float currentPosition, TabSettings tabSettings) {
        if (tabSettings != null)
            return tabSettings.getTabStopNewInstance(currentPosition);
        return TabStop.newInstance(currentPosition, DEFAULT_TAB_INTERVAL);
    }

    private java.util.List<TabStop> tabStops = new ArrayList<TabStop>();
    private float tabInterval = DEFAULT_TAB_INTERVAL;

    public TabSettings(){}

    public TabSettings(java.util.List<TabStop> tabStops) {
        this.tabStops = tabStops;
    }

    public TabSettings(float tabInterval) {
        this.tabInterval = tabInterval;
    }

    public TabSettings(java.util.List<TabStop> tabStops, float tabInterval) {
        this.tabStops = tabStops;
        this.tabInterval = tabInterval;
    }

    public List<TabStop> getTabStops() {
        return tabStops;
    }

    public void setTabStops(List<TabStop> tabStops) {
        this.tabStops = tabStops;
    }

    public float getTabInterval() {
        return tabInterval;
    }

    public void setTabInterval(float tabInterval) {
        this.tabInterval = tabInterval;
    }

    public TabStop getTabStopNewInstance(float currentPosition) {
        TabStop tabStop = null;
        if (tabStops != null) {
            for (TabStop currentTabStop : tabStops) {
                if (currentTabStop.getPosition() - currentPosition > 0.001) {
                    tabStop = new TabStop(currentTabStop);
                    break;
                }
            }
        }

        if (tabStop == null) {
            tabStop = TabStop.newInstance(currentPosition, tabInterval);
        }

        return tabStop;
    }
}
