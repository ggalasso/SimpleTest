package com.ggalasso.BggCollectionManager.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by truthd on 8/27/2015.
 */
@Root
public class GameId {
    @Attribute(required=false)
    private String objectId;

    public GameId() {
    }

    public GameId(String objectId) {
        this.objectId = objectId.trim();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId.trim();
    }

}
