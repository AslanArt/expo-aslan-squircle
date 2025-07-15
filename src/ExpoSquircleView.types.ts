import {
  AnimatableNumericValue,
  ColorValue,
  TouchableOpacityProps,
  ViewProps,
  processColor,
} from "react-native";

type SquircleProps = {
  cornerSmoothing?: number;
  borderRadius?: AnimatableNumericValue;
  borderWidth?: number;
  preserveSmoothing?: boolean;
  enabledIOSAnimation?: boolean;
  ignoreBorderWidthFromPadding?: boolean;
};

export type SquircleViewProps = {
  backgroundColor?: ColorValue;
  borderColor?: ColorValue;
  backgroundImage?: {
    source: number;
    resizeMode: string;
    opacity: number;
  };
  backgroundGradient?: {
    type: 'linear' | 'radial';
    colors: ColorValue[];
    angle?: number;
    center?: [number, number];
    radius?: number;
  };
  borderGradient?: {
    type: 'linear' | 'radial';
    colors: ColorValue[];
    angle?: number;
    center?: [number, number];
    radius?: number;
  };
} & ViewProps &
  SquircleProps;

export type SquircleButtonProps = {
  backgroundColor?: ColorValue;
  borderColor?: ColorValue;
  backgroundImage?: {
    source: number;
    resizeMode: string;
    opacity: number;
  };
  backgroundGradient?: {
    type: 'linear' | 'radial';
    colors: ColorValue[];
    angle?: number;
    center?: [number, number];
    radius?: number;
  };
  borderGradient?: {
    type: 'linear' | 'radial';
    colors: ColorValue[];
    angle?: number;
    center?: [number, number];
    radius?: number;
  };
} & TouchableOpacityProps &
  SquircleProps;

export type ExpoSquircleNativeViewProps = {
  squircleBackgroundColor?: ReturnType<typeof processColor>;
  squircleBorderColor?: ReturnType<typeof processColor>;
  squircleBorderWidth?: number;
  backgroundImageSource?: number;
  backgroundImageResizeMode?: string;
  backgroundImageOpacity?: number;
  backgroundGradientType?: string;
  backgroundGradientColors?: number[];
  backgroundGradientAngle?: number;
  backgroundGradientCenterX?: number;
  backgroundGradientCenterY?: number;
  backgroundGradientRadius?: number;
  borderGradientType?: string;
  borderGradientColors?: number[];
  borderGradientAngle?: number;
  borderGradientCenterX?: number;
  borderGradientCenterY?: number;
  borderGradientRadius?: number;
} & ViewProps &
  SquircleProps;
