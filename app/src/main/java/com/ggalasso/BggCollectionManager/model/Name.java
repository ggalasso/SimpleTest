package com.ggalasso.BggCollectionManager.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Chris on 9/3/2015.
 */

@Root
public class Name {

        @Attribute
        private String value;

        @Attribute
        private String sortindex;

        @Attribute
        private String type;

        public Name() {
        }

        public Name(String value, String sortindex, String type) {
                this.value = value;
                this.sortindex = sortindex;
                this.type = type;
        }

        public String getValue() {
                return value;
        }

        public String getType() {
                return type;
        }
}
