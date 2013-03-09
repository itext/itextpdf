package com.itextpdf.text;

import java.util.*;
import java.util.List;

public class TabSettings {
    static public final float DEFAULT_TAB_INTERVAL = 36;

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

    public static TabStop getNextTabPosition(float currentPosition, float tabInterval, TabSettings tabSettings) {
        if (Float.isNaN(tabInterval))
            return getNextTabPosition(currentPosition, tabSettings);
        return getNextTabPosition(currentPosition, tabInterval);
    }

    public static TabStop getNextTabPosition(float currentPosition, TabSettings tabSettings) {
        if (tabSettings != null)
            return tabSettings.getNextTabPosition(currentPosition);
        return getNextTabPosition(currentPosition, DEFAULT_TAB_INTERVAL);
    }

    public static TabStop getNextTabPosition(float currentPosition, float tabInterval) {
        currentPosition = (float)Math.round(currentPosition * 1000) / 1000;
        tabInterval = (float)Math.round(tabInterval * 1000) / 1000;

        TabStop tabStop = new TabStop(currentPosition + tabInterval - currentPosition % tabInterval);
        return tabStop;
    }

    public TabStop getNextTabPosition(float currentPosition) {
        TabStop tabStop = null;
        if (tabStops != null) {
            for (TabStop currentTabStop : tabStops) {
                if (currentTabStop.getPosition() - currentPosition > 0.001) {
                    tabStop = currentTabStop;
                    break;
                }
            }
        }

        if (tabStop == null) {
            currentPosition = (float)Math.round(currentPosition * 1000) / 1000;
            tabInterval = (float)Math.round(tabInterval * 1000) / 1000;

            tabStop = new TabStop(currentPosition + tabInterval - currentPosition % tabInterval);
        }

        return tabStop;
    }
}
