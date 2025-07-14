import {
  AnimatableNumericValue,
  ColorValue,
  TouchableOpacityProps,
  ViewProps,
  processColor,
} from "react-native";

// Type pour la source d'image (compatible avec React Native)
export type ImageSource = {
  uri?: string;
} | number;

// Types pour la gestion des images de fond
export type BackgroundImageResizeMode = 'cover' | 'contain' | 'stretch' | 'repeat' | 'center';
export type BackgroundImagePosition = 'top' | 'center' | 'bottom' | 'left' | 'right' | 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right';

export interface BackgroundImageProps {
  source: ImageSource;
  resizeMode?: BackgroundImageResizeMode;
  position?: BackgroundImagePosition;
  opacity?: number;
}

// Types pour les gradients
export type GradientType = 'linear' | 'radial';

export interface GradientColorStop {
  color: ColorValue;
  position?: number; // 0-1, position du stop dans le gradient
}

export interface LinearGradientProps {
  type: 'linear';
  colors: ColorValue[] | GradientColorStop[];
  angle?: number; // Angle en degrés, 0 = horizontal de gauche à droite
  locations?: number[]; // Positions des couleurs de 0 à 1
}

export interface RadialGradientProps {
  type: 'radial';
  colors: ColorValue[] | GradientColorStop[];
  center?: [number, number]; // Position du centre [x, y] de 0 à 1
  radius?: number; // Rayon du gradient de 0 à 1
  locations?: number[]; // Positions des couleurs de 0 à 1
}

export type GradientProps = LinearGradientProps | RadialGradientProps;

type SquircleProps = {
  cornerSmoothing?: number;
  borderRadius?: AnimatableNumericValue;
  borderWidth?: number;
  preserveSmoothing?: boolean;
  enabledIOSAnimation?: boolean;
  ignoreBorderWidthFromPadding?: boolean;
  // Nouvelles propriétés pour images et gradients
  backgroundImage?: BackgroundImageProps;
  backgroundGradient?: GradientProps;
  borderGradient?: GradientProps;
};

export type ExpoSquircleNativeViewProps = {
  squircleBackgroundColor?: ReturnType<typeof processColor>;
  squircleBorderColor?: ReturnType<typeof processColor>;
  squircleBorderWidth?: number;
  // Nouvelles props pour les images et gradients
  backgroundImageSource?: string | number;
  backgroundImageResizeMode?: BackgroundImageResizeMode;
  backgroundImagePosition?: BackgroundImagePosition;
  backgroundImageOpacity?: number;
  backgroundGradientType?: GradientType;
  backgroundGradientColors?: string; // JSON string des couleurs
  backgroundGradientAngle?: number;
  backgroundGradientCenter?: string; // JSON string [x, y]
  backgroundGradientRadius?: number;
  backgroundGradientLocations?: string; // JSON string des positions
  borderGradientType?: GradientType;
  borderGradientColors?: string; // JSON string des couleurs
  borderGradientAngle?: number;
  borderGradientCenter?: string; // JSON string [x, y]
  borderGradientRadius?: number;
  borderGradientLocations?: string; // JSON string des positions
} & ViewProps &
  SquircleProps;

export type SquircleViewProps = {
  backgroundColor?: ColorValue;
  borderColor?: ColorValue;
} & ViewProps &
  SquircleProps;

export type SquircleButtonProps = {
  backgroundColor?: ColorValue;
  borderColor?: ColorValue;
} & TouchableOpacityProps &
  SquircleProps;
