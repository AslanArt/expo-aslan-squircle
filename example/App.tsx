import { Button, SafeAreaView, ScrollView, StyleSheet, Text, View, Switch } from "react-native";

import { SquircleView } from "expo-squircle-view";
import { Slider } from "@miblanchard/react-native-slider";
import React from "react";
import Animated, { useAnimatedProps, useAnimatedStyle, useSharedValue, withTiming } from "react-native-reanimated";


export default function App() {
  const WIDTH = 390;
  const HEIGHT = 100;
  const CORNER_RADIUS = 40;
  const CORNER_SMOOTHING = 100;
  const BORDER_WIDTH = 4;
  const BORDER_COLOR = "gray";
  const BACKGROUND_COLOR = "red";
  const PRESERVE_SMOOTHING = true;

  const [width, setWidth] = React.useState(WIDTH);
  const [height, setHeight] = React.useState(HEIGHT);
  const [cornerRadius, setCornerRadius] = React.useState(CORNER_RADIUS);
  const [cornerSmoothing, setCornerSmoothing] = React.useState(CORNER_SMOOTHING);
  const [borderWidth, setBorderWidth] = React.useState(BORDER_WIDTH);
  const [padding, setPadding] = React.useState(0);
  
  // Nouveaux états pour les gradients et images
  const [showBackgroundGradient, setShowBackgroundGradient] = React.useState(false);
  const [showBorderGradient, setShowBorderGradient] = React.useState(false);
  const [showBackgroundImage, setShowBackgroundImage] = React.useState(false);
  const [gradientAngle, setGradientAngle] = React.useState(45);
  const [imageOpacity, setImageOpacity] = React.useState(1);

  // Exemples de gradients
  const backgroundGradient = showBackgroundGradient ? {
    type: 'linear' as const,
    colors: ['#ff0000', '#00ff00', '#0000ff'],
    angle: gradientAngle,
  } : undefined;

  const borderGradient = showBorderGradient ? {
    type: 'radial' as const,
    colors: ['#ffff00', '#ff00ff'],
    center: [0.5, 0.5] as [number, number],
    radius: 0.8,
  } : undefined;

  const backgroundImage = showBackgroundImage ? {
    source: require('./assets/icon.png'), // Utilise l'icône existante
    resizeMode: 'cover' as const,
    position: 'center' as const,
    opacity: imageOpacity,
  } : undefined;

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentInsetAdjustmentBehavior="automatic">
        <View style={styles.controlsContainer}>
          <Text style={styles.title}>Expo Squircle View Demo</Text>
          
          {/* Contrôles existants */}
          <View style={styles.sliderContainer}>
            <Text>Width: {width}</Text>
            <Slider
              value={width}
              onValueChange={(value) => setWidth(value[0])}
              minimumValue={100}
              maximumValue={400}
              step={10}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          <View style={styles.sliderContainer}>
            <Text>Height: {height}</Text>
            <Slider
              value={height}
              onValueChange={(value) => setHeight(value[0])}
              minimumValue={50}
              maximumValue={200}
              step={10}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          <View style={styles.sliderContainer}>
            <Text>Corner Radius: {cornerRadius}</Text>
            <Slider
              value={cornerRadius}
              onValueChange={(value) => setCornerRadius(value[0])}
              minimumValue={0}
              maximumValue={100}
              step={1}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          <View style={styles.sliderContainer}>
            <Text>Corner Smoothing: {cornerSmoothing}% {cornerSmoothing > 100 ? '(ULTRA SMOOTH!)' : ''}</Text>
            <Slider
              value={cornerSmoothing}
              onValueChange={(value) => setCornerSmoothing(value[0])}
              minimumValue={0}
              maximumValue={200}
              step={5}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          <View style={styles.sliderContainer}>
            <Text>Border Width: {borderWidth}</Text>
            <Slider
              value={borderWidth}
              onValueChange={(value) => setBorderWidth(value[0])}
              minimumValue={0}
              maximumValue={20}
              step={1}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          <View style={styles.sliderContainer}>
            <Text>Padding: {padding}</Text>
            <Slider
              value={padding}
              onValueChange={(value) => setPadding(value[0])}
              minimumValue={0}
              maximumValue={100}
              step={1}
              animateTransitions
              minimumTrackTintColor={"black"}
              maximumTrackTintColor={"gray"}
            />
          </View>

          {/* Nouveaux contrôles pour les gradients et images */}
          <View style={styles.switchContainer}>
            <Text>Background Gradient:</Text>
            <Switch
              value={showBackgroundGradient}
              onValueChange={setShowBackgroundGradient}
            />
          </View>

          <View style={styles.switchContainer}>
            <Text>Border Gradient:</Text>
            <Switch
              value={showBorderGradient}
              onValueChange={setShowBorderGradient}
            />
          </View>

          <View style={styles.switchContainer}>
            <Text>Background Image:</Text>
            <Switch
              value={showBackgroundImage}
              onValueChange={setShowBackgroundImage}
            />
          </View>

          {showBackgroundGradient && (
            <View style={styles.sliderContainer}>
              <Text>Gradient Angle: {gradientAngle}°</Text>
              <Slider
                value={gradientAngle}
                onValueChange={(value) => setGradientAngle(value[0])}
                minimumValue={0}
                maximumValue={360}
                step={15}
                animateTransitions
                minimumTrackTintColor={"black"}
                maximumTrackTintColor={"gray"}
              />
            </View>
          )}

          {showBackgroundImage && (
            <View style={styles.sliderContainer}>
              <Text>Image Opacity: {imageOpacity.toFixed(2)}</Text>
              <Slider
                value={imageOpacity}
                onValueChange={(value) => setImageOpacity(value[0])}
                minimumValue={0}
                maximumValue={1}
                step={0.1}
                animateTransitions
                minimumTrackTintColor={"black"}
                maximumTrackTintColor={"gray"}
              />
            </View>
          )}
        </View>

        {/* Exemples de SquircleView */}
        <View style={styles.examplesContainer}>
          <Text style={styles.sectionTitle}>Basic Example</Text>
          <SquircleView
            cornerSmoothing={cornerSmoothing}
            preserveSmoothing={PRESERVE_SMOOTHING}
            style={{
              width,
              height,
              padding,
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
              backgroundColor: showBackgroundGradient || showBackgroundImage ? 'transparent' : BACKGROUND_COLOR,
              borderColor: showBorderGradient ? 'transparent' : BORDER_COLOR,
              borderRadius: cornerRadius,
              borderWidth: borderWidth,
              overflow: 'hidden',
            }}
            backgroundGradient={backgroundGradient}
            borderGradient={borderGradient}
            backgroundImage={backgroundImage}
          >
            <Text style={styles.squircleText}>Squircle</Text>
            <View style={{ backgroundColor: 'yellow', height: 20, width: '100%', opacity: 0.7 }} />
          </SquircleView>

          <Text style={styles.sectionTitle}>Linear Gradient Example</Text>
          <SquircleView
            cornerSmoothing={100}
            preserveSmoothing={true}
            style={{
              width: 200,
              height: 100,
              justifyContent: "center",
              alignItems: "center",
              borderRadius: 30,
            }}
            backgroundGradient={{
              type: 'linear',
              colors: ['#FF6B6B', '#4ECDC4', '#45B7D1'],
              angle: 135,
            }}
          >
            <Text style={styles.squircleText}>Linear</Text>
          </SquircleView>

          <Text style={styles.sectionTitle}>Radial Gradient Example</Text>
          <SquircleView
            cornerSmoothing={100}
            preserveSmoothing={true}
            style={{
              width: 200,
              height: 100,
              justifyContent: "center",
              alignItems: "center",
              borderRadius: 30,
            }}
            backgroundGradient={{
              type: 'radial',
              colors: ['#FFD93D', '#FF6B6B'],
              center: [0.3, 0.3],
              radius: 0.8,
            }}
          >
            <Text style={styles.squircleText}>Radial</Text>
          </SquircleView>

          <Text style={styles.sectionTitle}>Border Gradient Example</Text>
          <SquircleView
            cornerSmoothing={100}
            preserveSmoothing={true}
            style={{
              width: 200,
              height: 100,
              justifyContent: "center",
              alignItems: "center",
              backgroundColor: 'white',
              borderRadius: 30,
              borderWidth: 5,
            }}
            borderGradient={{
              type: 'linear',
              colors: ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4'],
              angle: 45,
            }}
          >
            <Text style={[styles.squircleText, { color: 'black' }]}>Border</Text>
          </SquircleView>

          <Text style={styles.sectionTitle}>Image Background Example</Text>
          <SquircleView
            cornerSmoothing={100}
            preserveSmoothing={true}
            style={{
              width: 200,
              height: 100,
              justifyContent: "center",
              alignItems: "center",
              borderRadius: 30,
              borderWidth: 3,
              borderColor: '#333',
            }}
            backgroundImage={{
              source: require('./assets/icon.png'),
              resizeMode: 'cover',
              position: 'center',
              opacity: 0.7,
            }}
          >
            <Text style={[styles.squircleText, { backgroundColor: 'rgba(0,0,0,0.5)', paddingHorizontal: 10, borderRadius: 5 }]}>
              Image BG
            </Text>
          </SquircleView>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  controlsContainer: {
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 20,
    color: '#333',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
    marginVertical: 15,
    color: '#333',
  },
  sliderContainer: {
    marginVertical: 10,
  },
  switchContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginVertical: 10,
    paddingHorizontal: 10,
  },
  examplesContainer: {
    padding: 20,
    alignItems: 'center',
  },
  squircleText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 16,
  },
});
