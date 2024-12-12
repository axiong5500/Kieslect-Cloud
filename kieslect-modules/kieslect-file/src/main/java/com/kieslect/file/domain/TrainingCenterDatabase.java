package com.kieslect.file.domain;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "TrainingCenterDatabase")
@XmlType(propOrder = {"activities"})
public class TrainingCenterDatabase {
    private List<Activity> activities;

    @XmlElementWrapper(name = "Activities")
    @XmlElement(name = "Activity")
    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @XmlType(propOrder = {"id", "lap"})
    public static class Activity {

        private String sport; // 用于映射 <Activity Sport="Biking"> 标签

        // 用于填充 <Activity> 标签
        @XmlAttribute(name = "Sport")
        public String getSport() {
            return sport;
        }

        public void setSport(String sport) {
            this.sport = sport;
        }

        private String id;
        private Lap lap;

        @XmlElement(name = "Id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlElement(name = "Lap")
        public Lap getLap() {
            return lap;
        }

        public void setLap(Lap lap) {
            this.lap = lap;
        }

        @XmlType(propOrder = {"calories", "track"})
        public static class Lap {

            private String startTime;
            private int calories;
            private Track track;

            @XmlAttribute(name = "StartTime")
            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            @XmlElement(name = "Calories")
            public int getCalories() {
                return calories;
            }

            public void setCalories(int calories) {
                this.calories = calories;
            }

            @XmlElement(name = "Track")
            public Track getTrack() {
                return track;
            }

            public void setTrack(Track track) {
                this.track = track;
            }
        }

        @XmlType(propOrder = {"trackpoint"})
        public static class Track {

            private List<Trackpoint> trackpoint;

            @XmlElement(name = "Trackpoint")
            public List<Trackpoint> getTrackpoint() {
                return trackpoint;
            }

            public void setTrackpoint(List<Trackpoint> trackpoint) {
                this.trackpoint = trackpoint;
            }

        }

        @XmlType(propOrder = {"time","position",  "altitudeMeters", "distanceMeters", "heartRateBpm", "cadence"})
        public static class Trackpoint {

            private String time;

            private Position position;
            private double altitudeMeters;
            private double distanceMeters;
            private int heartRateBpm;
            private int cadence;


            @XmlElement(name = "Time")
            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            @XmlElement(name = "Position")
            public Position getPosition() {
                return position;
            }

            public void setPosition(Position position) {
                this.position = position;
            }


            @XmlElement(name = "AltitudeMeters")
            public double getAltitudeMeters() {
                return altitudeMeters;
            }

            public void setAltitudeMeters(double altitudeMeters) {
                this.altitudeMeters = altitudeMeters;
            }

            @XmlElement(name = "DistanceMeters")
            public double getDistanceMeters() {
                return distanceMeters;
            }

            public void setDistanceMeters(double distanceMeters) {
                this.distanceMeters = distanceMeters;
            }

            @XmlElement(name = "HeartRateBpm")
            public int getHeartRateBpm() {
                return heartRateBpm;
            }

            public void setHeartRateBpm(int heartRateBpm) {
                this.heartRateBpm = heartRateBpm;
            }

            @XmlElement(name = "Cadence")
            public int getCadence() {
                return cadence;
            }

            public void setCadence(int cadence) {
                this.cadence = cadence;
            }

            @XmlRootElement(name = "Position")
            public static class Position {

                private double latitudeDegrees;
                private double longitudeDegrees;

                @XmlElement(name = "LatitudeDegrees")
                public double getLatitudeDegrees() {
                    return latitudeDegrees;
                }

                public void setLatitudeDegrees(double latitudeDegrees) {
                    this.latitudeDegrees = latitudeDegrees;
                }

                @XmlElement(name = "LongitudeDegrees")
                public double getLongitudeDegrees() {
                    return longitudeDegrees;
                }

                public void setLongitudeDegrees(double longitudeDegrees) {
                    this.longitudeDegrees = longitudeDegrees;
                }
            }


        }


    }
}
