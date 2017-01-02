package com.fimo_pitch.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by diep1 on 1/2/2017.
 */

public class DirectionStep {
    LatLng start;
    LatLng end;
    String description;
    String distance;

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
