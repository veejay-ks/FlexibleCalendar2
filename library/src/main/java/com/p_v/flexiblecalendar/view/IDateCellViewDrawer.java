package com.p_v.flexiblecalendar.view;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * @author p-v
 */
public interface IDateCellViewDrawer extends ICellViewDrawer {
    /**
     * Date Cell view
     *
     * @param position
     * @param convertView
     * @param parent
     * @param cellType
     * @return
     */
    BaseCellView getCellView(int position, Component convertView, ComponentContainer parent,
                             @BaseCellView.CellType int cellType);
}
