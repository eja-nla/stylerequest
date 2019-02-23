/*
Copyright 2010 Alexandre Gellibert

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/
LICENSE-2.0 Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS"
BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied. See the License for the specific language governing permissions
and limitations under the License.
*/

package geocell.model;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
@Entity
@Cache
public class GeoLocation extends AbstractActorEnablerEntity implements LocationCapable {

//    @PrimaryKey
//    @Persistent
    @Id
    private Long id;

//    @Persistent
    private double latitude;

//    @Persistent
    private double longitude;

//    @Persistent
    @Index
    private List<String> geocells;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getGeocells() {
        return geocells;
    }

    public void setGeocells(List<String> geocells) {
        this.geocells = geocells;
    }

    public String getKeyString() {
        return Long.valueOf(id).toString();
    }

    @Override
    public Point getLocation() {
        return new Point(latitude, longitude);
    }

}
