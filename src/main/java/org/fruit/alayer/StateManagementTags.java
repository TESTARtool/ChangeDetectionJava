package org.fruit.alayer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashSet;
import java.util.Set;

public class StateManagementTags implements IStateManagementTags {


    // widget pattern tags
    public static final Tag<Boolean> WidgetAnnotationPattern = Tag.from("Widget Annotation Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetDockPattern = Tag.from("Widget Dock Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetDragPattern = Tag.from("Widget Drag Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetDropTargetPattern = Tag.from("Widget DropTarget Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetExpandCollapsePattern = Tag.from("Widget ExpandCollapse Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetGridItemPattern = Tag.from("Widget GridItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetGridPattern = Tag.from("Widget Grid Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetInvokePattern = Tag.from("Widget Invoke Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetItemContainerPattern = Tag.from("Widget ItemContainer Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetLegacyIAccessiblePattern = Tag.from("Widget LegacyIAccessible Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetMultipleViewPattern = Tag.from("Widget MultipleView Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetObjectModelPattern = Tag.from("Widget ObjectModel Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetRangeValuePattern = Tag.from("Widget RangeValue Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetScrollItemPattern = Tag.from("Widget ScrollItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetScrollPattern = Tag.from("Widget Scroll Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetSelectionItemPattern = Tag.from("Widget SelectionItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetSelectionPattern = Tag.from("Widget Selection Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetSpreadsheetPattern = Tag.from("Widget Spreadsheet Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetSpreadsheetItemPattern = Tag.from("Widget SpreadsheetItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetStylesPattern = Tag.from("Widget Styles Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetSynchronizedInputPattern = Tag.from("Widget SynchronizedInput Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTableItemPattern = Tag.from("Widget TableItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTablePattern = Tag.from("Widget Table Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTextChildPattern = Tag.from("Widget TextChild Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTextPattern = Tag.from("Widget Text Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTextPattern2 = Tag.from("Widget Text Pattern2", Boolean.class);
    public static final Tag<Boolean> WidgetTogglePattern = Tag.from("Widget Toggle Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTransformPattern = Tag.from("Widget Transform Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetTransformPattern2 = Tag.from("Widget Transform Pattern2", Boolean.class);
    public static final Tag<Boolean> WidgetValuePattern = Tag.from("Widget Value Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetVirtualizedItemPattern = Tag.from("Widget VirtualizedItem Pattern", Boolean.class);
    public static final Tag<Boolean> WidgetWindowPattern = Tag.from("Widget Window Pattern", Boolean.class);


    // a widget's control type
    public static final Tag<String> WidgetControlType = Tag.from("Widget control type", String.class);

    // the internal handle to a widget's window
    public static final Tag<Long> WidgetWindowHandle = Tag.from("Widget window handle", Long.class);

    // is the widget enabled?
    public static final Tag<Boolean> WidgetIsEnabled = Tag.from("Widget is enabled", Boolean.class);

    // the widget's title
    public static final Tag<String> WidgetTitle = Tag.from("Widget title", String.class);

    // a help text string that may be set for the widget
    public static final Tag<String> WidgetHelpText = Tag.from("Widget helptext", String.class);

    // the automation id for a particular version of a widget's application
    public static final Tag<String> WidgetAutomationId = Tag.from("Widget automation id", String.class);

    // the widget's classname
    public static final Tag<String> WidgetClassName = Tag.from("Widget class name", String.class);

    // // identifier for the framework that this widget belongs to (swing, flash, wind32, etc..)
    public static final Tag<String> WidgetFrameworkId = Tag.from("Widget framework id", String.class);

    // identifier for the orientation that the widget may or may not have
    public static final Tag<String> WidgetOrientationId = Tag.from("Widget orientation id", String.class);

    // is the widget a content element?
    public static final Tag<Boolean> WidgetIsContentElement = Tag.from("Widget is a content element", Boolean.class);

    // is the widget a control element?
    public static final Tag<Boolean> WidgetIsControlElement = Tag.from("Widget is a control element", Boolean.class);

    // the widget currently has keyboard focus
    public static final Tag<Boolean> WidgetHasKeyboardFocus = Tag.from("Widget has keyboard focus", Boolean.class);

    // it is possible for the widget to receive keyboard focus
    public static final Tag<Boolean> WidgetIsKeyboardFocusable = Tag.from("Widget can have keyboard focus", Boolean.class);

    // the item type of the widget
    public static final Tag<String> WidgetItemType = Tag.from("Widget item type", String.class);

    // a string describing the item status of the widget
    public static final Tag<String> WidgetItemStatus = Tag.from("Widget item status", String.class);

    // the path in the widget tree that leads to the widget
    public static final Tag<String> WidgetPath = Tag.from("Path to the widget", String.class);

    // the on-screen boundaries for the widget (coordinates)
    public static final Tag<String> WidgetBoundary = Tag.from("Widget on-screen boundaries", String.class);

    // is the widget off-screen?
    public static final Tag<Boolean> WidgetIsOffscreen = Tag.from("Widget is off-screen", Boolean.class);

    // accelator key combinations for the widget
    public static final Tag<String> WidgetAccelatorKey = Tag.from("Widget accelator key", String.class);

    // access key that will trigger the widget
    public static final Tag<String> WidgetAccessKey = Tag.from("Widget access key", String. class);

    // Aria properties of a uia element
    public static final Tag<String> WidgetAriaProperties = Tag.from("Widget aria properties", String.class);

    // Aria role of a UIA element
    public static final Tag<String> WidgetAriaRole = Tag.from("Widget aria role", String.class);

    // is the widget a dialog window?
    public static final Tag<Boolean> WidgetIsDialog = Tag.from("Widget is a dialog windows", Boolean.class);

    // does the widget contain password info?
    public static final Tag<Boolean> WidgetIsPassword = Tag.from("Widget contains password info", Boolean.class);

    // Indicated whether the Widget/UIA element represents peripheral UI.
    public static final Tag<Boolean> WidgetIsPeripheral = Tag.from("Widget represents peripheral UI", Boolean.class);

    // is the widget required input for a form?
    public static final Tag<Boolean> WidgetIsRequiredForForm = Tag.from("Widget is required input for a form", Boolean.class);

    // Is the widget/uiaelement part of a landmark/group?
    public static final Tag<Long> WidgetLandmarkType = Tag.from("Widget element grouping", Long.class);

    // the level in a hierarchical group
    public static final Tag<Long>  WidgetGroupLevel = Tag.from("Widget's level in hierarchy", Long.class);

    // widget's live setting
    public static final Tag<Long> WidgetLiveSetting = Tag.from("Widget's live setting", Long.class);

    // widget's position compared to its siblings
    public static final Tag<Long> WidgetSetPosition = Tag.from("Widget's position in sibling set", Long.class);

    // the size of the set of the element and its siblings
    public static final Tag<Long> WidgetSetSize = Tag.from("Widget's sibling set size (inclusive)", Long.class);

    // the angle of the widget's rotation
    public static final Tag<Long> WidgetRotation = Tag.from("Widget's rotation (degrees)", Long.class);

    // Web widgets
    public static final Tag<String> WebWidgetId = Tag.from("Web Widget id", String.class);
    public static final Tag<String> WebWidgetName = Tag.from("Web Widget name", String.class);
    public static final Tag<String> WebWidgetTagName = Tag.from("Web Widget tag name", String.class);
    public static final Tag<String> WebWidgetTextContent= Tag.from("Web Widget text content", String.class);
    public static final Tag<String> WebWidgetTitle = Tag.from("Web Widget title", String.class);
    public static final Tag<String> WebWidgetHref = Tag.from("Web Widget href", String.class);
    public static final Tag<String> WebWidgetValue = Tag.from("Web Widget value", String.class);
    public static final Tag<String> WebWidgetStyle = Tag.from("Web Widget style", String.class);
    public static final Tag<String> WebWidgetTarget = Tag.from("Web Widget target", String.class);
    public static final Tag<String> WebWidgetAlt = Tag.from("Web Widget alt", String.class);
    public static final Tag<String> WebWidgetType = Tag.from("Web Widget type", String.class);
    public static final Tag<String> WebWidgetCssClasses = Tag.from("Web Widget css classes", String.class);
    public static final Tag<String> WebWidgetDisplay = Tag.from("Web Widget display", String.class);
    public static final Tag<Boolean> WebWidgetIsOffScreen = Tag.from("Web Widget Is Off Screen", Boolean.class);
    public static final Tag<String> WebWidgetSrc = Tag.from("Web Widget src", String.class);


    // Value control pattern
    public static final Tag<Boolean> WidgetValueIsReadOnly = Tag.from("WidgetValueIsReadOnly", Boolean.class);
    public static final Tag<String> WidgetValueValue = Tag.from("WidgetValueValue", String.class);

    private static Set<Tag<?>> stateManagementTags = new HashSet<Tag<?>>() {
        {
            add(WidgetControlType);
//            add(WidgetWindowHandle); // this property changes between different executions of the sut
            add(WidgetIsEnabled);
            add(WidgetTitle);
            add(WidgetHelpText);
            add(WidgetAutomationId);
            add(WidgetClassName);
            add(WidgetFrameworkId);
            add(WidgetOrientationId);
            add(WidgetIsContentElement);
            add(WidgetIsControlElement);
            add(WidgetHasKeyboardFocus);
            add(WidgetIsKeyboardFocusable);
            add(WidgetItemType);
            add(WidgetItemStatus);
            add(WidgetPath);
            add(WidgetBoundary);
            // new
            add(WidgetAccelatorKey);
            add(WidgetAccessKey);
            add(WidgetAriaProperties);
            add(WidgetAriaRole);
//            add(WidgetIsDialog); (deactived for now, because the UIA API does not seem to recognize it)
            add(WidgetIsPassword);
            add(WidgetIsPeripheral);
            add(WidgetIsRequiredForForm);
            add(WidgetLandmarkType);
            add(WidgetGroupLevel);
            add(WidgetLiveSetting);
            add(WidgetSetPosition);
            add(WidgetSetSize);
            add(WidgetRotation);

            // patterns
            add(WidgetAnnotationPattern);
            add(WidgetDockPattern);
            add(WidgetDragPattern);
            add(WidgetDropTargetPattern);
            add(WidgetExpandCollapsePattern);
            add(WidgetGridItemPattern);
            add(WidgetGridPattern);
            add(WidgetInvokePattern);
            add(WidgetItemContainerPattern);
            add(WidgetLegacyIAccessiblePattern);
            add(WidgetMultipleViewPattern);
            add(WidgetObjectModelPattern);
            add(WidgetRangeValuePattern);
            add(WidgetScrollItemPattern);
            add(WidgetScrollPattern);
            add(WidgetSelectionItemPattern);
            add(WidgetSelectionPattern);
            add(WidgetSpreadsheetPattern);
            add(WidgetSpreadsheetItemPattern);
            add(WidgetStylesPattern);
            add(WidgetSynchronizedInputPattern);
            add(WidgetTableItemPattern);
            add(WidgetTablePattern);
            add(WidgetTextChildPattern);
            add(WidgetTextPattern);
            add(WidgetTextPattern2);
            add(WidgetTogglePattern);
            add(WidgetTransformPattern);
            add(WidgetTransformPattern2);
            add(WidgetValuePattern);
            add(WidgetValueValue);
            add(WidgetVirtualizedItemPattern);
            add(WidgetWindowPattern);

            //Webdriver
            add(WebWidgetId);
            add(WebWidgetName);
            add(WebWidgetTagName);
            add(WebWidgetTextContent);
            add(WebWidgetTitle);
            add(WebWidgetHref);
            add(WebWidgetValue);
            add(WebWidgetStyle);
            add(WebWidgetTarget);
            add(WebWidgetAlt);
            add(WebWidgetType);
            add(WebWidgetCssClasses);
            add(WebWidgetDisplay);
            add(WebWidgetIsOffScreen);
            add(WebWidgetSrc);
        }
    };


    // a bi-directional mapping from the state management tags to a string equivalent for use in the application.settings file
    private static BiMap<Tag<?>, String> settingsMap = HashBiMap.create(stateManagementTags.size());
    static {
        settingsMap.put(WidgetControlType, "WidgetControlType");
        settingsMap.put(WidgetWindowHandle, "WidgetWindowHandle");
        settingsMap.put(WidgetIsEnabled, "WidgetIsEnabled");
        settingsMap.put(WidgetTitle, "WidgetTitle");
        settingsMap.put(WidgetHelpText, "WidgetHelpText");
        settingsMap.put(WidgetAutomationId, "WidgetAutomationId");
        settingsMap.put(WidgetClassName, "WidgetClassName");
        settingsMap.put(WidgetFrameworkId, "WidgetFrameworkId");
        settingsMap.put(WidgetOrientationId, "WidgetOrientationId");
        settingsMap.put(WidgetIsContentElement, "WidgetIsContentElement");
        settingsMap.put(WidgetIsControlElement, "WidgetIsControlElement");
        settingsMap.put(WidgetHasKeyboardFocus, "WidgetHasKeyboardFocus");
        settingsMap.put(WidgetIsKeyboardFocusable, "WidgetIsKeyboardFocusable");
        settingsMap.put(WidgetItemType, "WidgetItemType");
        settingsMap.put(WidgetItemStatus, "WidgetItemStatus");
        settingsMap.put(WidgetPath, "WidgetPath");
        settingsMap.put(WidgetAccelatorKey, "WidgetAccelatorKey");
        settingsMap.put(WidgetAccessKey, "WidgetAccessKey");
        settingsMap.put(WidgetAriaProperties, "WidgetAriaProperties");
        settingsMap.put(WidgetAriaRole, "WidgetAriaRole");
        settingsMap.put(WidgetIsDialog, "WidgetIsDialog");
        settingsMap.put(WidgetIsPassword, "WidgetIsPassword");
        settingsMap.put(WidgetIsPeripheral, "WidgetIsPeripheral");
        settingsMap.put(WidgetIsRequiredForForm, "WidgetIsRequiredForForm");
        settingsMap.put(WidgetLandmarkType, "WidgetLandmarkType");
        settingsMap.put(WidgetGroupLevel, "WidgetGroupLevel");
        settingsMap.put(WidgetLiveSetting, "WidgetLiveSetting");
        settingsMap.put(WidgetSetPosition, "WidgetSetPosition");
        settingsMap.put(WidgetSetSize, "WidgetSetSize");
        settingsMap.put(WidgetRotation, "WidgetRotation");
        settingsMap.put(WidgetAnnotationPattern, "WidgetAnnotationPattern");
        settingsMap.put(WidgetDockPattern, "WidgetDockPattern");
        settingsMap.put(WidgetDragPattern, "WidgetDragPattern");
        settingsMap.put(WidgetDropTargetPattern, "WidgetDropTargetPattern");
        settingsMap.put(WidgetExpandCollapsePattern, "WidgetExpandCollapsePattern");
        settingsMap.put(WidgetGridItemPattern, "WidgetGridItemPattern");
        settingsMap.put(WidgetGridPattern, "WidgetGridPattern");
        settingsMap.put(WidgetInvokePattern, "WidgetInvokePattern");
        settingsMap.put(WidgetItemContainerPattern, "WidgetItemContainerPattern");
        settingsMap.put(WidgetLegacyIAccessiblePattern, "WidgetLegacyIAccessiblePattern");
        settingsMap.put(WidgetMultipleViewPattern, "WidgetMultipleViewPattern");
        settingsMap.put(WidgetObjectModelPattern, "WidgetObjectModelPattern");
        settingsMap.put(WidgetRangeValuePattern, "WidgetRangeValuePattern");
        settingsMap.put(WidgetScrollItemPattern, "WidgetScrollItemPattern");
        settingsMap.put(WidgetScrollPattern, "WidgetScrollPattern");
        settingsMap.put(WidgetSelectionItemPattern, "WidgetSelectionItemPattern");
        settingsMap.put(WidgetSelectionPattern, "WidgetSelectionPattern");
        settingsMap.put(WidgetSpreadsheetPattern, "WidgetSpreadsheetPattern");
        settingsMap.put(WidgetSpreadsheetItemPattern, "WidgetSpreadsheetItemPattern");
        settingsMap.put(WidgetStylesPattern, "WidgetStylesPattern");
        settingsMap.put(WidgetSynchronizedInputPattern, "WidgetSynchronizedInputPattern");
        settingsMap.put(WidgetTableItemPattern, "WidgetTableItemPattern");
        settingsMap.put(WidgetTablePattern, "WidgetTablePattern");
        settingsMap.put(WidgetTextChildPattern, "WidgetTextChildPattern");
        settingsMap.put(WidgetTextPattern, "WidgetTextPattern");
        settingsMap.put(WidgetTextPattern2, "WidgetTextPattern2");
        settingsMap.put(WidgetTogglePattern, "WidgetTogglePattern");
        settingsMap.put(WidgetTransformPattern, "WidgetTransformPattern");
        settingsMap.put(WidgetTransformPattern2, "WidgetTransformPattern2");
        settingsMap.put(WidgetValuePattern, "WidgetValuePattern");
        settingsMap.put(WidgetValueValue, "WidgetValueValue");
        settingsMap.put(WidgetVirtualizedItemPattern, "WidgetVirtualizedItemPattern");
        settingsMap.put(WidgetWindowPattern, "WidgetWindowPattern");

        //Webdriver
        settingsMap.put(WebWidgetId, "WebWidgetId");
        settingsMap.put(WebWidgetName, "WebWidgetName");
        settingsMap.put(WebWidgetTagName, "WebWidgetTagName");
        settingsMap.put(WebWidgetTextContent, "WebWidgetTextContent");
        settingsMap.put(WebWidgetTitle, "WebWidgetTitle");
        settingsMap.put(WebWidgetHref, "WebWidgetHref");
        settingsMap.put(WebWidgetValue, "WebWidgetValue");
        settingsMap.put(WebWidgetStyle, "WebWidgetStyle");
        settingsMap.put(WebWidgetTarget, "WebWidgetTarget");
        settingsMap.put(WebWidgetAlt, "WebWidgetAlt");
        settingsMap.put(WebWidgetType, "WebWidgetType");
        settingsMap.put(WebWidgetCssClasses, "WebWidgetCssClasses");
        settingsMap.put(WebWidgetDisplay, "WebWidgetDisplay");
        settingsMap.put(WebWidgetIsOffScreen, "WebWidgetIsOffScreen");
        settingsMap.put(WebWidgetSrc, "WebWidgetSrc");
    }

    public Tag getTagFromSettingsString(String settingsString) {
        return settingsMap.inverse().getOrDefault(settingsString, null);
    }
}
