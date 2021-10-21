/***************************************************************************************************
 *
 * Copyright (c) 2013 - 2021 Universitat Politecnica de Valencia - www.upv.es
 * Copyright (c) 2018 - 2021 Open Universiteit - www.ou.nl
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

/**
 *  @author Sebastian Bauersfeld
 */

package org.fruit.alayer;

public final class Tags extends TagsBase {

    private Tags() {}

    /** Usually attached to widgets. It tells whether this widget this widget is rendered.
     * If so, it does not necessarily mean that it is also visible, since it could still
     * be obfuscated by other controls. */
    public static final Tag<Boolean> Rendered = from("Rendered", Boolean.class);

    /** A short human-readable descriptive text for a widget / action / or system */
    public static final Tag<String> Desc = from("Desc", String.class);

    /** determines whether a widget is enabled (e.g. in many GUI-Frameworks disabled items are
     * greyed out) */
    public static final Tag<Boolean> Enabled = from("Enabled", Boolean.class);

    /** determines whether a system is running */
    public static final Tag<Boolean> IsRunning = from("IsRunning", Boolean.class);

    /** determines whether a widget is blocked by anything or whether it is accessible. For example:
     * If there is a modal message box on the screen, this message box usually blocks other widgets
     * which are consequently not clickable. */
    public static final Tag<Boolean> Blocked = from("Blocked", Boolean.class);

    /** determines whether a widget / system / window is in the foreground */
    public static final Tag<Boolean> Foreground = from("Foreground", Boolean.class);

    /** the title of a widget (usually visible text, such as on a button) */
    public static final Tag<String> Title = from("Title", String.class);

    /** ZIndex of a widget (objects with higher values are drawn on top of objects with lower values) */
    public static final Tag<Double> ZIndex = from("ZIndex", Double.class);

    /** Maximum observed ZIndex for a UI state */
    public static final Tag<Double> MaxZIndex = from("MaxZIndex", Double.class);
    /** Minimum observed ZIndex for a UI state */
    public static final Tag<Double> MinZIndex = from("MinZIndex", Double.class);


    /** The value of the help text usually attached to many widgets (e.g. when you hover over an item and a little box with a description appears) */
    public static final Tag<String> ToolTipText = from("ToolTipText", String.class);

    /** Usually attached to window widgets. Determines whether the widget is modal (blocks other widgets) */
    public static final Tag<Boolean> Modal = from("Modal", Boolean.class);

    /** Usually attached to sliders or scrollbars. Determines the orientation (horizontal, vertical or an arbitrary angle) */
    public static final Tag<Double> Angle = from("Angle", Double.class);

    /** The text of a widget (e.g. the text within a text box) */
    public static final Tag<String> Text = from("Text", String.class);

    /** The pattern value of a web-widget : hyperlink or text in input field */
    public static final Tag<String> ValuePattern = from("ValuePattern", String.class);


    public static final Tag<String> Path = from("Path", String.class);



    /** For actions that apply to a single target, keep the target ID (abstract) */
    public static final Tag<String> TargetID = from("TargetID", String.class);

    /** The Process ID. Usually attached to systems. */
    public static final Tag<Long> PID = from("PID", Long.class);

    /** A handle identifier to a window */
    public static final Tag<Long> HWND = from("HWND", Long.class);

    /** The Process HANDLE. Usually attached to systems. */
    public static final Tag<Long> HANDLE = from("HANDLE", Long.class);

    public static final Tag<Long> TimeStamp = from("TimeStamp", Long.class);

    //public static final Tag<Image> Screenshot = from("Screenshot", Image.class);
    public static final Tag<String> ScreenshotPath = from("ScreenshotPath", String.class);


}
