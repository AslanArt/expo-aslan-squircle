import { StatusBar } from "expo-status-bar";
import React, { useState } from "react";
import {
  StyleSheet,
  View,
  TextInput,
  Button,
  Keyboard,
  Text,
  ScrollView,
  SafeAreaView,
  Image,
} from "react-native";
import { SquircleView } from "expo-squircle-view";
import { Slider } from "@miblanchard/react-native-slider";

const localImageSource = Image.resolveAssetSource(require("./assets/bg.jpg"));

export default function App() {
  const [imageUri, setImageUri] = useState(
    "https://www.creative-engineer.com/assets/imgs//portfolio/zbrush/byakuya//extralarge/1.jpg"
  );
  const [inputText, setInputText] = useState(imageUri);

  const [cornerSmoothing, setCornerSmoothing] = useState(100);
  const [borderRadius, setBorderRadius] = useState(80);
  const [borderWidth, setBorderWidth] = useState(25);
  const [opacity, setOpacity] = useState(1);
  const [gradientAngle, setGradientAngle] = useState(45);

  const handlePress = () => {
    setImageUri(inputText);
    Keyboard.dismiss();
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollView}>
        <Text style={styles.title}>Test avec image locale</Text>
        <SquircleView
          style={styles.squircle}
          backgroundImageSource={localImageSource}
          cornerSmoothing={cornerSmoothing}
          borderRadius={borderRadius}
          borderWidth={borderWidth}
          borderColor="#333"
          backgroundImageOpacity={opacity}
        />

        <View style={styles.separator} />

        <Text style={styles.title}>Test avec URL externe et contrôles</Text>

        <SquircleView
          style={styles.squircle}
          cornerSmoothing={cornerSmoothing}
          borderRadius={borderRadius}
          borderWidth={borderWidth}
          borderColor="#666"
          squircleBackgroundColor="#e0e0e0"
          backgroundImageSource={{ uri: imageUri }}
          backgroundImageResizeMode="cover"
          backgroundImageOpacity={opacity}
        />

        <View style={styles.controlsContainer}>
          <View style={styles.inputContainer}>
            <TextInput
              style={styles.input}
              placeholder="Collez une URL d'image"
              placeholderTextColor="#999"
              value={inputText}
              onChangeText={setInputText}
              onSubmitEditing={handlePress}
            />
            <Button title="Valider" onPress={handlePress} />
          </View>

          <View style={styles.sliderControl}>
            <Text style={styles.sliderLabel}>Lissage: {cornerSmoothing.toFixed(0)}</Text>
            <Slider
              value={cornerSmoothing}
              onValueChange={(value) => setCornerSmoothing(value[0])}
              minimumValue={0}
              maximumValue={100}
              step={1}
            />
          </View>
          
          <View style={styles.sliderControl}>
            <Text style={styles.sliderLabel}>Rayon: {borderRadius.toFixed(0)}</Text>
            <Slider
              value={borderRadius}
              onValueChange={(value) => setBorderRadius(value[0])}
              minimumValue={0}
              maximumValue={150}
              step={1}
            />
          </View>

          <View style={styles.sliderControl}>
            <Text style={styles.sliderLabel}>Bordure: {borderWidth.toFixed(0)}</Text>
            <Slider
              value={borderWidth}
              onValueChange={(value) => setBorderWidth(value[0])}
              minimumValue={0}
              maximumValue={50}
              step={1}
            />
          </View>

          <View style={styles.sliderControl}>
            <Text style={styles.sliderLabel}>Opacité: {opacity.toFixed(2)}</Text>
            <Slider
              value={opacity}
              onValueChange={(value) => setOpacity(value[0])}
              minimumValue={0}
              maximumValue={1}
              step={0.05}
            />
          </View>

          <View style={styles.sliderControl}>
            <Text style={styles.sliderLabel}>Angle du dégradé: {gradientAngle.toFixed(0)}</Text>
            <Slider
              value={gradientAngle}
              onValueChange={(value) => setGradientAngle(value[0])}
              minimumValue={0}
              maximumValue={360}
              step={1}
            />
          </View>
        </View>
        <StatusBar style="auto" />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f0f0f0",
  },
  scrollView: {
    paddingHorizontal: 20,
    paddingBottom: 40,
  },
  title: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
    marginTop: 25,
    marginBottom: 15,
    textAlign: 'center'
  },
  squircle: {
    width: 250,
    height: 250,
    alignSelf: 'center'
  },
  separator: {
    height: 1,
    backgroundColor: '#ddd',
    marginVertical: 30,
    marginHorizontal: 20,
  },
  controlsContainer: {
    marginTop: 20,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    width: "100%",
    marginBottom: 20,
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: "#ccc",
    backgroundColor: '#fff',
    color: "#000",
    paddingHorizontal: 10,
    paddingVertical: 8,
    borderRadius: 8,
    marginRight: 10,
  },
  sliderControl: {
    width: "100%",
    marginBottom: 10,
  },
  sliderLabel: {
    color: '#555'
  }
});
