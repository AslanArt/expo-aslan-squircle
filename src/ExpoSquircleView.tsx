import { requireNativeViewManager } from "expo-modules-core";
import * as React from "react";
import {
  View,
  StyleSheet,
  TouchableOpacity,
  processColor,
  ViewProps,
  DimensionValue,
  Image,
} from "react-native";

import {
  SquircleButtonProps,
  SquircleViewProps,
  ExpoSquircleNativeViewProps,
  GradientProps,
  BackgroundImageProps,
} from "./ExpoSquircleView.types";

const NativeView: React.ComponentType<ExpoSquircleNativeViewProps> =
  requireNativeViewManager("ExpoSquircleView");

const ExpoSquircleViewNativeWrapper = (
  props: React.PropsWithChildren<SquircleViewProps | SquircleButtonProps>
) => {
  const {
    cornerSmoothing,
    backgroundColor,
    borderRadius,
    borderColor,
    borderWidth,
    preserveSmoothing,
    enabledIOSAnimation,
    backgroundImage,
    backgroundGradient,
    borderGradient,
  } = props;

  // Convertir les propriétés d'image
  const imageProps = React.useMemo(() => {
    if (!backgroundImage) return {};

    let imageSource: string | number | undefined;
    
    if (typeof backgroundImage.source === 'number') {
      imageSource = backgroundImage.source;
    } else if (backgroundImage.source && 'uri' in backgroundImage.source) {
      imageSource = backgroundImage.source.uri;
    }

    return {
      backgroundImageSource: imageSource,
      backgroundImageResizeMode: backgroundImage.resizeMode || 'cover',
      backgroundImagePosition: backgroundImage.position || 'center',
      backgroundImageOpacity: backgroundImage.opacity ?? 1.0,
    };
  }, [backgroundImage]);

  // Convertir les propriétés de gradient de fond
  const backgroundGradientProps = React.useMemo(() => {
    if (!backgroundGradient) return {};

    const colors = Array.isArray(backgroundGradient.colors) 
      ? backgroundGradient.colors.map(color => {
          if (typeof color === 'string' || typeof color === 'number') {
            return processColor(color);
          } else if (color && typeof color === 'object' && 'color' in color) {
            return processColor(color.color);
          }
          return processColor(color);
        }).filter(color => color !== null)
      : [];

    const locations = backgroundGradient.locations || 
      (Array.isArray(backgroundGradient.colors) 
        ? backgroundGradient.colors.map((color, index) => 
            typeof color === 'object' && color.position !== undefined 
              ? color.position 
              : index / (backgroundGradient.colors.length - 1)
          )
        : undefined);

    return {
      backgroundGradientType: backgroundGradient.type,
      backgroundGradientColors: JSON.stringify(colors),
      backgroundGradientAngle: backgroundGradient.type === 'linear' ? backgroundGradient.angle || 0 : undefined,
      backgroundGradientCenter: backgroundGradient.type === 'radial' 
        ? JSON.stringify(backgroundGradient.center || [0.5, 0.5]) 
        : undefined,
      backgroundGradientRadius: backgroundGradient.type === 'radial' ? backgroundGradient.radius || 0.5 : undefined,
      backgroundGradientLocations: locations ? JSON.stringify(locations) : undefined,
    };
  }, [backgroundGradient]);

  // Convertir les propriétés de gradient de bordure
  const borderGradientProps = React.useMemo(() => {
    if (!borderGradient) return {};

    const colors = Array.isArray(borderGradient.colors) 
      ? borderGradient.colors.map(color => {
          if (typeof color === 'string' || typeof color === 'number') {
            return processColor(color);
          } else if (color && typeof color === 'object' && 'color' in color) {
            return processColor(color.color);
          }
          return processColor(color);
        }).filter(color => color !== null)
      : [];

    const locations = borderGradient.locations || 
      (Array.isArray(borderGradient.colors) 
        ? borderGradient.colors.map((color, index) => 
            typeof color === 'object' && color.position !== undefined 
              ? color.position 
              : index / (borderGradient.colors.length - 1)
          )
        : undefined);

    return {
      borderGradientType: borderGradient.type,
      borderGradientColors: JSON.stringify(colors),
      borderGradientAngle: borderGradient.type === 'linear' ? borderGradient.angle || 0 : undefined,
      borderGradientCenter: borderGradient.type === 'radial' 
        ? JSON.stringify(borderGradient.center || [0.5, 0.5]) 
        : undefined,
      borderGradientRadius: borderGradient.type === 'radial' ? borderGradient.radius || 0.5 : undefined,
      borderGradientLocations: locations ? JSON.stringify(locations) : undefined,
    };
  }, [borderGradient]);

  return (
    <NativeView
      squircleBackgroundColor={processColor(backgroundColor)}
      squircleBorderColor={processColor(borderColor)}
      squircleBorderWidth={borderWidth}
      borderRadius={borderRadius}
      cornerSmoothing={cornerSmoothing}
      preserveSmoothing={preserveSmoothing}
      enabledIOSAnimation={enabledIOSAnimation}
      {...imageProps}
      {...backgroundGradientProps}
      {...borderGradientProps}
      style={StyleSheet.absoluteFill}
    />
  );
};

export const SquircleButton = (
  props: React.PropsWithChildren<SquircleButtonProps>
) => {
  const { children } = props;
  const { squircleProps, wrapperStyle } = useSquircleProps(props);

  return (
    <TouchableOpacity
      {...props}
      style={wrapperStyle}
    >
      <ExpoSquircleViewNativeWrapper
        {...squircleProps}
      />
      {children}
    </TouchableOpacity>
  );
};

export const SquircleView = (props: ViewProps & SquircleViewProps) => {
  const { children } = props;
  const { squircleProps, wrapperStyle } = useSquircleProps(props);

  return (
    <View
      {...props}
      style={wrapperStyle}
    >
      <ExpoSquircleViewNativeWrapper
        {...squircleProps}
      />
      {children}
    </View>
  );
};

const useSquircleProps = (
  props: SquircleViewProps | SquircleButtonProps
) => {
  const style = props.style ? StyleSheet.flatten(props.style) : undefined;

  const {
    cornerSmoothing,
    borderRadius,
    borderWidth,
    backgroundColor,
    borderColor,
    ignoreBorderWidthFromPadding,
  } = props;

  const { 
    padding,
    paddingVertical, 
    paddingHorizontal, 
    paddingBottom,
    paddingEnd,
    paddingLeft,
    paddingRight,
    paddingStart, 
    paddingTop 
  } = style || {};

  const calculatedPadding = React.useMemo(() => {
    const extraPadding = borderWidth || style?.borderWidth || 0;

    const calculatePadding = (_paddingValue: DimensionValue) => {
      if (typeof _paddingValue === "number") {
        return _paddingValue + extraPadding;
      }
      return _paddingValue;
    };

    return {
      padding: padding ? calculatePadding(padding) : extraPadding,
      paddingVertical: paddingVertical ? calculatePadding(paddingVertical) : undefined,
      paddingHorizontal: paddingHorizontal ? calculatePadding(paddingHorizontal) : undefined,
      paddingBottom: paddingBottom ? calculatePadding(paddingBottom) : undefined,
      paddingEnd: paddingEnd ? calculatePadding(paddingEnd) : undefined,
      paddingLeft: paddingLeft ? calculatePadding(paddingLeft) : undefined,
      paddingRight: paddingRight ? calculatePadding(paddingRight) : undefined,
      paddingStart: paddingStart ? calculatePadding(paddingStart) : undefined,
      paddingTop: paddingTop ? calculatePadding(paddingTop) : undefined,
    }
  }, [style, borderWidth])

  return {
    squircleProps: {
      ...props,
      borderRadius: borderRadius || style?.borderRadius || 0,
      borderWidth: borderWidth || style?.borderWidth || 0,
      backgroundColor:
        backgroundColor || style?.backgroundColor || "transparent",
      borderColor: borderColor || style?.borderColor || "transparent",
      cornerSmoothing: cornerSmoothing !== undefined ? cornerSmoothing : 100,
      preserveSmoothing: props.preserveSmoothing || false,
      enabledIOSAnimation: props.enabledIOSAnimation || false,
    },
    wrapperStyle: [
      styles.container,
      style,
      {
        // remove styles from wrapper
        borderWidth: undefined,
        borderColor: undefined,
        backgroundColor: undefined,
        ...(ignoreBorderWidthFromPadding === true ? undefined: calculatedPadding)
      },
    ],
  };
};

const styles = StyleSheet.create({
  container: { backgroundColor: "transparent" },
});
