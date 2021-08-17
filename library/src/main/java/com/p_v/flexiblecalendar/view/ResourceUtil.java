package com.p_v.flexiblecalendar.view;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;

public class ResourceUtil {
    public static Element getElementFromResourceId(int resourceId) {
        RgbColor rgbColor = new RgbColor(resourceId);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(rgbColor);
        return shapeElement;
    }
}
