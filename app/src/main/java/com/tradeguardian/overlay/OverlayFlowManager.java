package com.tradeguardian.overlay;

import com.tradeguardian.utils.KillzoneUtils;

public class OverlayFlowManager {

    private static OverlayFlowManager instance;

    public static OverlayFlowManager get() {
        if (instance == null) instance = new OverlayFlowManager();
        return instance;
    }

    public OverlayStep start() {
        KillzoneUtils.Killzone zone = KillzoneUtils.getCurrentKillzone();

        return zone == KillzoneUtils.Killzone.NONE
                ? OverlayStep.OUTSIDE_KILLZONE
                : OverlayStep.INSIDE_KILLZONE;
    }

    public OverlayStep next(OverlayStep step, boolean yes) {
        switch (step) {
            case OUTSIDE_KILLZONE:
                return yes ? OverlayStep.ANALYSIS_CHECK : OverlayStep.DISCIPLINE_WARNING;
            case INSIDE_KILLZONE:
                return OverlayStep.ANALYSIS_CHECK;
            case ANALYSIS_CHECK:
                return yes ? null : OverlayStep.DISCIPLINE_WARNING;
            default:
                return null;
        }
    }
}
