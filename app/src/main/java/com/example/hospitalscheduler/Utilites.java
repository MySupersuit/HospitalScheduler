package com.example.hospitalscheduler;

import java.util.ArrayList;

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
            case UROLOGY:
//                return R.drawable.urology;
                return R.drawable.ic_urology_icon;
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
            case UROLOGY:
                return "#98FB98";
            default:
                return "errorInUtilities";
        }
    }

    public static ArrayList<OperatingTheatre> createSampleOTList() {
        ArrayList<Operation> schedule1 = new ArrayList<>();
        schedule1.add(new Operation("Head Deflation", NEURO, 1,
                "Dr. John Zoidberg", "Annie",
                "JD", "Rev. Nichols",
                null, null,
                0, 0, "20"));
        schedule1.add(new Operation("Broken Heart", CARDIOTHOR, 0,
                "Dr. Who", "Greg",
                "Amelia", "Derrick Alek",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule2 = new ArrayList<>();
        schedule2.add(new Operation("Vascularitis", VASCULAR, 3,
                "Dr. Nick Riviera", "Annie",
                "Turk", "Archbishop Tutu",
                null, null,
                0, 0, "6"));
        schedule2.add(new Operation("Head Deflation", NEURO, 0,
                "Dr. John Zoidberg", "Annie",
                "JD", "Rev. Nichols",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule3 = new ArrayList<>();
        schedule3.add(new Operation("Blocked Nose", HEAD_AND_NECK, 0,
                "Dr. Perry Cox", "Tess",
                "Todd", "Francis",
                null, null,
                0, 0, null));
        schedule3.add(new Operation("Vascularitis", VASCULAR, 0,
                "Dr. Nick Riviera", "Annie",
                "Turk", "Archbishop Tutu",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule4 = new ArrayList<>();
        schedule4.add(new Operation("Spare Ribs", ORTHO, 0,
                "Dr. Greg House", "Dave",
                "Elliot", "Tom Cruise",
                null, null,
                0, 0, null));
        schedule4.add(new Operation("Blocked Nose", HEAD_AND_NECK, 0,
                "Dr. Perry Cox", "Tess",
                "Todd", "Francis",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule5 = new ArrayList<>();
        schedule5.add(new Operation("Broken Heart", UROLOGY, 0,
                "Dr. Who", "Greg",
                "Amelia", "Derrick Alek",
                null, null,
                0, 0, null));
        schedule5.add(new Operation("Spare Ribs", ORTHO, 0,
                "Dr. Greg House", "Dave",
                "Elliot", "Tom Cruise",
                null, null,
                0, 0, null));

        ArrayList<OperatingTheatre> lstOTv2 = new ArrayList<>();
        lstOTv2.add(new OperatingTheatre(1, 0, schedule1));
        lstOTv2.add(new OperatingTheatre(2, 0, schedule2));
        lstOTv2.add(new OperatingTheatre(3, 0, schedule3));
        lstOTv2.add(new OperatingTheatre(4, 0, schedule4));
        lstOTv2.add(new OperatingTheatre(5, 0, schedule5));

        return lstOTv2;
    }
}
