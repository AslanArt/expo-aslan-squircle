import { requireNativeViewManager } from "expo-modules-core";
import * as React from "react";
import { View, StyleSheet, processColor } from "react-native";
import { SquircleViewProps, ExpoSquircleNativeViewProps } from "./ExpoSquircleView.types";

const NativeView: React.ComponentType<ExpoSquircleNativeViewProps> =
  requireNativeViewManager("ExpoSquircleView");

export const SquircleView = (props: SquircleViewProps) => {
  const { 
    cornerSmoothing,
    borderRadius,
    borderWidth,
    backgroundColor,
    borderColor,
    preserveSmoothing,
    backgroundImageSource,
    backgroundImageResizeMode,
    backgroundImageOpacity,
    children, 
    ...rest 
  } = props;

  return (
    <View {...rest}>
      <NativeView
        style={StyleSheet.absoluteFill}
        squircleBackgroundColor={processColor(backgroundColor)}
        squircleBorderColor={processColor(borderColor)}
        squircleBorderWidth={borderWidth}
        borderRadius={borderRadius}
        cornerSmoothing={cornerSmoothing}
        preserveSmoothing={preserveSmoothing}
        backgroundImageSource={backgroundImageSource}
        backgroundImageResizeMode={backgroundImageResizeMode}
        backgroundImageOpacity={backgroundImageOpacity}
      />
      {children}
    </View>
  );
};
