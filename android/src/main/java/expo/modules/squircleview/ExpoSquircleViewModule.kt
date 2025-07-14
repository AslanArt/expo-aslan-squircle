package expo.modules.squircleview

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoSquircleViewModule : Module() {
    override fun definition() = ModuleDefinition {

        Name("ExpoSquircleView")

        View(ExpoSquircleView::class) {
            // Propriétés existantes
            Prop("squircleBackgroundColor") { view: ExpoSquircleView, prop: Int? ->
                if (prop != null) {
                    view.setViewBackgroundColor(prop)
                }
            }

            Prop("squircleBorderColor") { view: ExpoSquircleView, prop: Int? ->
                if (prop != null) {
                    view.setBorderColor(prop)
                }
            }

            Prop("squircleBorderWidth") { view: ExpoSquircleView, prop: Float ->
                view.setBorderWidth(prop)
            }

            Prop("borderRadius") { view: ExpoSquircleView, prop: Float ->
                view.setBorderRadius(prop)
            }

            Prop("cornerSmoothing") { view: ExpoSquircleView, prop: Int ->
                view.setCornerSmoothing(prop)
            }

            Prop("preserveSmoothing") { view: ExpoSquircleView, prop: Boolean ->
                view.setPreserveSmoothing(prop)
            }

            // Nouvelles propriétés pour les images de fond
            Prop("backgroundImageSource") { view: ExpoSquircleView, prop: Any? ->
                view.setBackgroundImageSource(prop)
            }

            Prop("backgroundImageResizeMode") { view: ExpoSquircleView, prop: String? ->
                if (prop != null) {
                    view.setBackgroundImageResizeMode(prop)
                }
            }

            Prop("backgroundImagePosition") { view: ExpoSquircleView, prop: String? ->
                if (prop != null) {
                    view.setBackgroundImagePosition(prop)
                }
            }

            Prop("backgroundImageOpacity") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBackgroundImageOpacity(prop)
                }
            }

            // Nouvelles propriétés pour les gradients de fond
            Prop("backgroundGradientType") { view: ExpoSquircleView, prop: String? ->
                view.setBackgroundGradientType(prop)
            }

            Prop("backgroundGradientColors") { view: ExpoSquircleView, prop: String? ->
                view.setBackgroundGradientColors(prop)
            }

            Prop("backgroundGradientAngle") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBackgroundGradientAngle(prop)
                }
            }

            Prop("backgroundGradientCenter") { view: ExpoSquircleView, prop: String? ->
                view.setBackgroundGradientCenter(prop)
            }

            Prop("backgroundGradientRadius") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBackgroundGradientRadius(prop)
                }
            }

            Prop("backgroundGradientLocations") { view: ExpoSquircleView, prop: String? ->
                view.setBackgroundGradientLocations(prop)
            }

            // Nouvelles propriétés pour les gradients de bordure
            Prop("borderGradientType") { view: ExpoSquircleView, prop: String? ->
                view.setBorderGradientType(prop)
            }

            Prop("borderGradientColors") { view: ExpoSquircleView, prop: String? ->
                view.setBorderGradientColors(prop)
            }

            Prop("borderGradientAngle") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBorderGradientAngle(prop)
                }
            }

            Prop("borderGradientCenter") { view: ExpoSquircleView, prop: String? ->
                view.setBorderGradientCenter(prop)
            }

            Prop("borderGradientRadius") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBorderGradientRadius(prop)
                }
            }

            Prop("borderGradientLocations") { view: ExpoSquircleView, prop: String? ->
                view.setBorderGradientLocations(prop)
            }
        }
    }
}
