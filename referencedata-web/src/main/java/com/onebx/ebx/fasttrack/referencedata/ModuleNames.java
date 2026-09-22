package com.onebx.ebx.fasttrack.referencedata;

/** Stable deployed module identity; display labels are deliberately separate. */
final class ModuleNames {
    static final String REFERENCE_DATA = "ebx-reference-data";
    private ModuleNames() { }

    static String schema(final String filename) {
        return "urn:ebx:module:" + REFERENCE_DATA + ":/WEB-INF/ebx/schemas/" + filename;
    }
}
