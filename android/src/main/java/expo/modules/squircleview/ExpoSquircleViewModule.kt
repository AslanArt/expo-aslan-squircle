package expo.modules.squircleview

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoSquircleViewModule : Module() {
    override fun definition() = ModuleDefinition {

        Name("ExpoSquircleView")

        View(ExpoSquircleView::class) {
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

            Prop("backgroundImageSource") { view: ExpoSquircleView, prop: Map<String, Any>? ->
                val uri = prop?.get("uri") as? String
                view.setBackgroundImageSource(uri)
            }

            Prop("backgroundImageResizeMode") { view: ExpoSquircleView, prop: String? ->
                if (prop != null) {
                    view.setBackgroundImageResizeMode(prop)
                }
            }

            Prop("backgroundImageOpacity") { view: ExpoSquircleView, prop: Float? ->
                if (prop != null) {
                    view.setBackgroundImageOpacity(prop)
                }
            }

            Prop("borderGradientColors") { view: ExpoSquircleView, prop: List<Int>? ->
                view.setBorderGradientColors(prop)
            }

            Prop("borderGradientAngle") { view: ExpoSquircleView, prop: Float? ->
                view.setBorderGradientAngle(prop ?: 0f)
            }
        }
    }
}
