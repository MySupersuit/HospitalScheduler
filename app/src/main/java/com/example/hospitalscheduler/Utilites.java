package com.example.hospitalscheduler;

import static com.example.hospitalscheduler.Constants.*;

public final class Utilites {

    // TODO fix with remaining icons
    public static int categoryToDrawable(String category) {
        switch (category) {
            case NEURO:
                return R.drawable.humanbrain;
            case VASCULAR:
                return R.drawable.vascularicon;
            case HEAD_AND_NECK:
                return R.drawable.ent_icon;
            case ORTHO:
                return R.drawable.ortho_icon;
            case CARDIOTHOR:
                return R.drawable.intestine_icon;
            default:
                return -1;
        }
    }

    // TODO update with all new icons
    public static String categoryToColour(String category) {
        switch (category) {
            case NEURO:
                return "#ADD8E6";
            case HEAD_AND_NECK:
                return "#FFC0CB";
            case CARDIOTHOR:
                return "#FFFFAA";
            case VASCULAR:
                return "#E6E6FA";
            case ORTHO:
                return "#FFFFFF";
            default:
                return "errorInUtilities";
        }
    }
}
