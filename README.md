# expo-squircle-view

Une implémentation native pour le lissage des coins Figma (Forme Squircle) pour les applications React Native Expo avec support avancé pour **gradients**, **images de fond** et **ultra-smoothing**.

<p align="center">
<img width="830" alt="Screenshot 2024-01-19 at 3 55 14 PM" src="https://github.com/Malaa-tech/expo-squircle-view/assets/24798045/1c403119-11bc-49c0-9310-d8211ae6b020">
</p>

## ✨ Nouvelles Fonctionnalités

### 🎨 Gradients
- **Gradients linéaires** avec angle personnalisable (0-360°)
- **Gradients radiaux** avec centre et rayon configurables  
- Support pour **fond ET bordures**
- Jusqu'à **10 couleurs** par gradient
- Contrôle précis des **positions des couleurs**

### 🖼️ Images de Fond
- Support pour **images locales** (ressources du projet)
- **5 modes de redimensionnement** : `cover`, `contain`, `stretch`, `repeat`, `center`
- **Contrôle d'opacité** (0-1)
- **Clipping automatique** sur forme squircle

### 🚀 Ultra-Smoothing 
- **Corner smoothing étendu** : 0-200% (au lieu de 0-100%)
- **Effet exotique** ultra-smooth pour des formes uniques
- **Algorithmes optimisés** pour des rendus fluides

## Installation

### Projets Expo managés
```bash
npm install expo-squircle-view 
# ou
yarn add expo-squircle-view 
```

Puis rebuilder votre app :
```bash
npx expo prebuild
```

### Projets React Native bare

Vous devez d'abord [installer et configurer le package `expo`](https://docs.expo.dev/bare/installing-expo-modules/) avant de continuer.

```bash
npm install expo-squircle-view
npx pod-install
```

## Exemple Rapide

```tsx
import { SquircleView } from "expo-squircle-view";

export default function App() {
  return (
    <SquircleView
      cornerSmoothing={150} // 150% pour ultra-smooth !
      style={{
        width: 200,
        height: 100,
        borderRadius: 30,
      }}
      // Gradient de fond
      backgroundGradient={{
        type: 'linear',
        colors: ['#FF6B6B', '#4ECDC4', '#45B7D1'],
        angle: 135,
      }}
      // Image de fond
      backgroundImage={{
        source: require('./assets/background.png'),
        resizeMode: 'cover',
        opacity: 0.8,
      }}
      // Gradient de bordure
      borderGradient={{
        type: 'radial',
        colors: ['#FFD93D', '#FF6B6B'],
        center: [0.5, 0.5],
        radius: 0.8,
      }}
    >
      <Text>Squircle magique!</Text>
    </SquircleView>
  );
}
```

## Props API

### Props de Base

<table>
  <tr>
    <th>Nom</th>
    <th>Type</th>
    <th>Description</th>
    <th>Défaut</th>
  </tr>
  <tr>
    <td><code>cornerSmoothing</code></td>
    <td><code>number</code></td>
    <td>Contrôle le lissage des coins. <code>0</code> = aucun lissage, <code>100</code> = lissage normal, <code>200</code> = ultra-smooth !</td>
    <td><code>100</code></td>
  </tr>
  <tr>
    <td><code>borderRadius</code></td>
    <td><code>number</code></td>
    <td>Rayon des coins</td>
    <td><code>0</code></td>
  </tr>
  <tr>
    <td><code>borderWidth</code></td>
    <td><code>number</code></td>
    <td>Largeur de la bordure</td>
    <td><code>0</code></td>
  </tr>
  <tr>
    <td><code>backgroundColor</code></td>
    <td><code>ColorValue</code></td>
    <td>Couleur de fond (ignorée si gradient défini)</td>
    <td><code>transparent</code></td>
  </tr>
  <tr>
    <td><code>borderColor</code></td>
    <td><code>ColorValue</code></td>
    <td>Couleur de bordure (ignorée si gradient défini)</td>
    <td><code>transparent</code></td>
  </tr>
  <tr>
    <td><code>preserveSmoothing</code></td>
    <td><code>boolean</code></td>
    <td>Préserver le lissage même sur petites tailles</td>
    <td><code>false</code></td>
  </tr>
</table>

### Nouvelles Props - Images de Fond

<table>
  <tr>
    <th>Nom</th>
    <th>Type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><code>backgroundImage</code></td>
    <td><code>BackgroundImageProps</code></td>
    <td>Configuration de l'image de fond</td>
  </tr>
</table>

**`BackgroundImageProps`:**
```typescript
interface BackgroundImageProps {
  source: ImageSource;                    // require('./image.png') ou {uri: 'url'}
  resizeMode?: 'cover' | 'contain' | 'stretch' | 'repeat' | 'center';
  position?: 'top' | 'center' | 'bottom' | 'left' | 'right' | 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right';
  opacity?: number;                       // 0-1
}
```

### Nouvelles Props - Gradients

<table>
  <tr>
    <th>Nom</th>
    <th>Type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><code>backgroundGradient</code></td>
    <td><code>GradientProps</code></td>
    <td>Gradient pour le fond</td>
  </tr>
  <tr>
    <td><code>borderGradient</code></td>
    <td><code>GradientProps</code></td>
    <td>Gradient pour la bordure</td>
  </tr>
</table>

**`LinearGradientProps`:**
```typescript
interface LinearGradientProps {
  type: 'linear';
  colors: ColorValue[];                   // Tableau de couleurs
  angle?: number;                         // Angle en degrés (0-360)
  locations?: number[];                   // Positions des couleurs (0-1)
}
```

**`RadialGradientProps`:**
```typescript
interface RadialGradientProps {
  type: 'radial';
  colors: ColorValue[];                   // Tableau de couleurs  
  center?: [number, number];              // Position du centre [x, y] (0-1)
  radius?: number;                        // Rayon du gradient (0-1)
  locations?: number[];                   // Positions des couleurs (0-1)
}
```

## Exemples Avancés

### Gradient Linéaire
```tsx
<SquircleView
  cornerSmoothing={120}
  style={{ width: 200, height: 100, borderRadius: 25 }}
  backgroundGradient={{
    type: 'linear',
    colors: ['#FF6B6B', '#4ECDC4', '#45B7D1'],
    angle: 45,
    locations: [0, 0.5, 1], // Répartition des couleurs
  }}
>
  <Text>Gradient Linéaire</Text>
</SquircleView>
```

### Gradient Radial avec Centre Personnalisé
```tsx
<SquircleView
  cornerSmoothing={100}
  style={{ width: 200, height: 100, borderRadius: 25 }}
  backgroundGradient={{
    type: 'radial',
    colors: ['#FFD93D', '#FF6B6B', '#8B5CF6'],
    center: [0.3, 0.3], // Décalé vers le haut-gauche
    radius: 0.8,
  }}
>
  <Text>Gradient Radial</Text>
</SquircleView>
```

### Bordure avec Gradient + Image de Fond
```tsx
<SquircleView
  cornerSmoothing={150} // Ultra-smooth !
  style={{ 
    width: 200, 
    height: 100, 
    borderRadius: 30,
    borderWidth: 4,
  }}
  backgroundImage={{
    source: require('./assets/hero.jpg'),
    resizeMode: 'cover',
    opacity: 0.7,
  }}
  borderGradient={{
    type: 'linear',
    colors: ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4'],
    angle: 90,
  }}
>
  <Text style={{ color: 'white', fontWeight: 'bold' }}>
    Combinaison Complète
  </Text>
</SquircleView>
```

### Ultra-Smoothing (200%)
```tsx
<SquircleView
  cornerSmoothing={200} // Maximum smoothing !
  style={{ 
    width: 150, 
    height: 150, 
    borderRadius: 40,
  }}
  backgroundGradient={{
    type: 'radial',
    colors: ['#FF9A9E', '#FECFEF', '#FECFEF'],
    center: [0.5, 0.5],
    radius: 1,
  }}
>
  <Text>Ultra Smooth</Text>
</SquircleView>
```

## Utilisation comme Bouton

```tsx
import { SquircleButton } from "expo-squircle-view";

<SquircleButton
  onPress={() => console.log('Pressed!')}
  cornerSmoothing={120}
  style={{ 
    width: 200, 
    height: 50, 
    borderRadius: 25,
    justifyContent: 'center',
    alignItems: 'center',
  }}
  backgroundGradient={{
    type: 'linear',
    colors: ['#667eea', '#764ba2'],
    angle: 135,
  }}
>
  <Text style={{ color: 'white', fontWeight: 'bold' }}>
    Bouton Squircle
  </Text>
</SquircleButton>
```

## Performance

- ✅ **Rendu natif** (iOS: CALayer, Android: Canvas)
- ✅ **Hardware acceleration** pour les gradients
- ✅ **Clipping optimisé** pour les images
- ✅ **Pas de bridge JavaScript** pendant le rendu
- ✅ **Réutilisation des objets** Paint/CALayer

## Compatibilité

- ✅ **iOS** 11.0+
- ✅ **Android** API 21+
- ✅ **Expo** 49+
- ✅ **React Native** 0.70+

## Crédits

Bibliothèques qui ont rendu ce projet possible :

- [figma-squircle](https://github.com/phamfoo/figma-squircle)
- [react-native-figma-squircle](https://github.com/phamfoo/react-native-figma-squircle)
- [react-native-squircle](https://github.com/samuel-rl/react-native-squircle)

Article du blog Figma expliquant les squircles :
[Desperately seeking squircles](https://www.figma.com/blog/desperately-seeking-squircles/)

## License

MIT


