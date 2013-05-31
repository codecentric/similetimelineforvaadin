package org.vaadin.chronographer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.chronographer.gwt.client.model.TimeUnit;
import org.vaadin.chronographer.gwt.client.model.TimelineBandInfo;
import org.vaadin.chronographer.gwt.client.model.TimelineEvent;
import org.vaadin.chronographer.gwt.client.model.TimelineZone;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SimpleTimelineExample extends VerticalLayout {

    private static final long serialVersionUID = 9211917184063343587L;
    private final DateFormat df = new SimpleDateFormat("yyyy");
    private ChronoGrapher timeline;

    public SimpleTimelineExample() {
        setId("timeline-ex1-verticallayout");
    }

    @Override
    public void attach() {
        super.attach();
        if (timeline == null) {
            com.vaadin.ui.Label infoLabel = new com.vaadin.ui.Label(
                    "This simple example demonstrates basic usage of the ChronoGrapher widget");
            infoLabel.setId("timeline-ex1-infolabel");
            addComponent(infoLabel);
            try {
                addComponent(getTimelineComponent());
                createEvents();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private Component getTimelineComponent() throws ParseException {
        timeline = new ChronoGrapher();
        timeline.setId("timeline-ex1-timelinewidget");
        timeline.setImmediate(true);
        timeline.setWidth("100%");
        timeline.setHeight("100px");

        TimelineZone zone = new TimelineZone();
        zone.setStart(df.parse("1917"));
        zone.setEnd(df.parse("1920"));
        zone.setUnit(TimeUnit.WEEK);
        zone.setMultiple(1);
        zone.setMagnify(10);

        TimelineBandInfo bandInfo = new TimelineBandInfo();
        bandInfo.setDate(df.parse("2013"));
        bandInfo.setTimeZone(+2);
        bandInfo.setWidth("100%");
        bandInfo.setIntervalUnit(TimeUnit.YEAR);
        bandInfo.setIntervalPixels(35);
        bandInfo.addTimelineZone(zone);

        timeline.addBandInfo(bandInfo);
        return timeline;
    }

    private void createEvents() throws ParseException {
        String[] names = new String[] { "K. J. Sthålber", "L. K. Relander",
                "P. E. Svinhuvud", "Kyösti Kallio", "Risto Ryti",
                "C. G. Mannerheim", "J. K. Paasikivi", "Urho Kekkonen",
                "Mauno Koivisto", "Martti Ahtisaari", "Tarja Halonen",
                "Sauli Niinistö" };
        String[] colors = new String[] { "#3399ff", "#33cc33", "#3399ff",
                "#33cc33", "#3399ff", "#ffffff", "#3399ff", "#3399ff",
                "#33cc33", "#ff6666", "#ff6666", "#3399ff" };
        String[][] dates = new String[][] { { "1919", "1925" },
                { "1925", "1931" }, { "1931", "1937" }, { "1937", "1940" },
                { "1940", "1944" }, { "1944", "1946" }, { "1946", "1956" },
                { "1956", "1982" }, { "1982", "1994" }, { "1994", "2000" },
                { "2000", "2012" }, { "2012", "2016" } };

        List<TimelineEvent> events = new ArrayList<TimelineEvent>();
        for (int i = 0; i < names.length; i++) {
            TimelineEvent event = new TimelineEvent();
            event.setIsDuration(true);
            event.setStart(df.parse(dates[i][0]));
            event.setEnd(df.parse(dates[i][1]));
            event.setTitle(names[i]);
            event.setId(i);
            event.setColor(colors[i]);
            events.add(event);
        }
        timeline.addEvents(events);
    }
}