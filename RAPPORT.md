# Rapport de Modification : Modernisation du Chargement d'Image

Ce document résume les changements apportés au module `expo-squircle-view` pour corriger le problème d'affichage des images de fond.

## 1. Le Problème Initial

Le composant natif Android était conçu pour recevoir la source de l'image (`backgroundImageSource`) sous la forme d'un **entier (Integer)**. C'était la méthode utilisée dans d'anciennes versions de React Native, où `require('image.png')` était résolu en un identifiant de ressource Android.

Dans les versions modernes d'Expo et React Native, `require()` résout l'image en un **objet JavaScript complexe** qui, une fois transmis au code natif, devient une `Map` contenant des informations détaillées comme l'URI (`uri`), la largeur et la hauteur.

Le code natif ne comprenant pas ce nouveau format, il ignorait simplement la propriété, et donc aucune image n'était chargée.

## 2. La Solution Implémentée

La stratégie a été de moderniser la partie native Android pour la rendre compatible avec le fonctionnement actuel d'Expo.

### Étape 1 : Changer la Réception de la Propriété

J'ai d'abord modifié le "contrôleur" du module (`ExpoSquircleViewModule.kt`) pour qu'il accepte une `Map` (un dictionnaire) au lieu d'un `Integer`. Il sait maintenant comment extraire l'**URI** de l'image depuis cette `Map`.

### Étape 2 : Intégrer une Bibliothèque de Chargement d'Image Moderne (Coil)

Le simple fait d'avoir l'URI ne suffit pas ; il faut un mécanisme pour charger l'image depuis cette URI. L'ancienne méthode (`BitmapFactory.decodeResource`) ne fonctionne qu'avec les identifiants de ressource.

J'ai donc intégré **Coil**, une bibliothèque Kotlin moderne, légère et performante, recommandée par Google pour le chargement d'images.

### Étape 3 : Remplacer la Logique de Chargement

Enfin, j'ai entièrement remplacé le code de chargement dans la vue `ExpoSquircleView.kt` :
- L'ancien code tentait de charger l'image de manière **synchrone** (bloquante) directement dans la méthode de dessin `onDraw`, ce qui est très mauvais pour les performances.
- Le nouveau code utilise Coil pour lancer un chargement **asynchrone** (non-bloquant) en arrière-plan. Une fois l'image prête, la vue est automatiquement invalidée et redessinée avec l'image, sans jamais geler l'interface utilisateur.

## 3. Pourquoi c'est la Bonne Méthode

1.  **Compatibilité :** C'est la méthode standard actuelle pour gérer les assets d'image entre React Native/Expo et le code natif. Votre module est maintenant à l'épreuve du futur.
2.  **Performance :** Coil est extrêmement optimisé. Il gère automatiquement :
    *   Le **threading** : Le téléchargement et le décodage se font en arrière-plan.
    *   Le **caching** : Les images sont mises en cache (en mémoire et sur le disque) pour un rechargement quasi-instantané.
    *   La **gestion de la mémoire** : Il redimensionne intelligemment les images pour éviter les erreurs de type `OutOfMemoryError`, un risque courant avec l'ancienne méthode.
3.  **Robustesse :** La nouvelle implémentation inclut une gestion basique des erreurs (via `onError`). Si le chargement échoue, l'application ne plantera pas.
4.  **Flexibilité :** Cette méthode fonctionnera pour les images locales (`require`), mais aussi pour des images provenant d'Internet (`{uri: 'https://...'}`), ce qui rend votre composant beaucoup plus polyvalent.

En résumé, la modification a non seulement corrigé le bug, mais a aussi rendu le module plus performant, plus stable et conforme aux standards de développement actuels. 