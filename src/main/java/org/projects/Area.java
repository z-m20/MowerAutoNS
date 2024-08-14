package org.projects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Area {
    int length;
    int width;

    public Area(int length, int width) {
        if (width < 0 || length < 0) {
            throw new IllegalArgumentException("Either length (" + length + ") or width (" + width + ") are negative");
        }
    }

    public void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Failed to set area's width, provided value (" + width + ") is negative");
        }
        this.width = width;
    }

    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Failed to set area's length, provided value (" + length + ") is negative");
        }
        this.length = length;
    }
}
