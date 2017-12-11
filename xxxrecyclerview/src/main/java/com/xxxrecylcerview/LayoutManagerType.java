package com.xxxrecylcerview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by ijays on 10/12/2017.
 */
@IntDef({LayoutManagerType.LINEAR, LayoutManagerType.GRID, LayoutManagerType.STAGGERED_GRID})
@Retention(RetentionPolicy.SOURCE)
public @interface LayoutManagerType {
    int LINEAR = 0x01;
    int GRID = 0x02;
    int STAGGERED_GRID = 0x03;
}
