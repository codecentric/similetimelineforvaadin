/* 
 * Copyright 2009 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.vaadin.chronographer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.vaadin.chronographer.gwt.client.connect.ChronoGrapherServerRpc;
import org.vaadin.chronographer.gwt.client.model.Events;
import org.vaadin.chronographer.gwt.client.model.TimelineBandInfo;
import org.vaadin.chronographer.gwt.client.model.TimelineEvent;
import org.vaadin.chronographer.gwt.client.model.theme.TimelineTheme;
import org.vaadin.chronographer.gwt.client.shared.ChronoGrapherState;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
@JavaScript(value = { "gwt/public/js/api/timeline-api.js" })
public class ChronoGrapher extends AbstractComponent {
    private transient DateFormat df = new SimpleDateFormat(
            "EEE MMM dd yyyy HH:mm:ss Z", Locale.US);

    private final List<TimelineBandInfo> bandInfos;
    private final Events timelineEvents;
    private final List<TimelineTheme> timelineThemes;

    private String width = "100%";
    private String height = "100%";

    private boolean stateDirty;

    public ChronoGrapher() {
        super();
        bandInfos = new ArrayList<TimelineBandInfo>();
        timelineThemes = new ArrayList<TimelineTheme>();
        timelineEvents = new Events();

        registerRpc(new ChronoGrapherServerRpc() {
            @Override
            public void onClick(int id, int x, int y) {
                Notification.show("Event " + id + " clicked @ (" + x + "," + y
                        + ")");
            }
        });
    }

    public void addBandInfo(TimelineBandInfo bandInfo) {
        bandInfos.add(bandInfo);

        getState().width = width;
        getState().height = height;
        getState().bandInfos = bandInfos;
        System.out.println("..structure");
    }

    public void addTheme(TimelineTheme theme) {
        timelineThemes.add(theme);
        getState().timelineThemes = timelineThemes;
        System.out.println("...theme");
        drawChronoGrapher();
    }

    public void addEvent(TimelineEvent event, boolean redraw) {
        timelineEvents.add(event);
        getState().eventsJson = paintEventsOnJSON();
        System.out.println("...events");
        if (redraw) {
            drawChronoGrapher();
        }
    }

    public void addEvents(TimelineEvent... events) {
        for (TimelineEvent e : events) {
            timelineEvents.add(e);
        }
        getState().eventsJson = paintEventsOnJSON();
        System.out.println("...events");
        drawChronoGrapher();
    }

    public void addEvents(List<TimelineEvent> events) {
        for (TimelineEvent e : events) {
            timelineEvents.add(e);
        }
        getState().eventsJson = paintEventsOnJSON();
        System.out.println("...events");
        drawChronoGrapher();
    }

    public void clearBandInfos() {
        bandInfos.clear();
        getState().bandInfos = null;
        drawChronoGrapher();
    }

    public void clearEvents() {
        timelineEvents.clear();
        getState().eventsJson = null;
        drawChronoGrapher();
    }

    public void drawChronoGrapher() {
        stateDirty = true;
        beforeClientResponse(false);
    }

    @Override
    public ChronoGrapherState getState() {
        return (ChronoGrapherState) super.getState();
    }

    private String paintBandInfosAndZones() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(bandInfos);
    }

    private String paintEventsOnJSON() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(timelineEvents);
    }

    private String paintThemesOnJSON() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(timelineThemes);
    }

    public void setDateFormatter(DateFormat df) {
        this.df = df;
    }

    public DateFormat getDateFormatter() {
        return df;
    }

    @Override
    public void setWidth(String width) {
        this.width = width;
        super.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
    }
}