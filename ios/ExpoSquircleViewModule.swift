import ExpoModulesCore

public class ExpoSquircleViewModule: Module {
    public func definition() -> ModuleDefinition {
        Name("ExpoSquircleView")
        
        View(ExpoSquircleView.self) { 
            // Propriétés existantes
            Prop("squircleBackgroundColor") { (view: ExpoSquircleView, prop: UIColor) in
                view.setBackgroundColor(prop)
            }
            Prop("squircleBorderColor") { (view: ExpoSquircleView, prop: UIColor) in
                view.setBorderColor(prop)
            }
            Prop("squircleBorderWidth") { (view: ExpoSquircleView, prop: Float) in
                view.setBorderWidth(CGFloat(prop))
            }
            Prop("borderRadius") { (view: ExpoSquircleView, prop: Float) in
                view.setRadius(CGFloat(prop))
            }
            Prop("cornerSmoothing") { (view: ExpoSquircleView, prop: Int) in
                view.setCornerSmoothing(CGFloat(prop))
            }
            Prop("preserveSmoothing") { (view: ExpoSquircleView, prop: Bool) in
                view.setPreserveSmoothing(prop)
            }
            Prop("enabledIOSAnimation") { (view: ExpoSquircleView, prop: Bool) in
                view.isAnimationEnabled = prop
            }
            
            // Nouvelles propriétés pour les images de fond
            Prop("backgroundImageSource") { (view: ExpoSquircleView, prop: Any?) in
                view.setBackgroundImageSource(prop)
            }
            Prop("backgroundImageResizeMode") { (view: ExpoSquircleView, prop: String?) in
                if let mode = prop {
                    view.setBackgroundImageResizeMode(mode)
                }
            }
            Prop("backgroundImagePosition") { (view: ExpoSquircleView, prop: String?) in
                if let position = prop {
                    view.setBackgroundImagePosition(position)
                }
            }
            Prop("backgroundImageOpacity") { (view: ExpoSquircleView, prop: Float?) in
                if let opacity = prop {
                    view.setBackgroundImageOpacity(opacity)
                }
            }
            
            // Nouvelles propriétés pour les gradients de fond
            Prop("backgroundGradientType") { (view: ExpoSquircleView, prop: String?) in
                view.setBackgroundGradientType(prop)
            }
            Prop("backgroundGradientColors") { (view: ExpoSquircleView, prop: String?) in
                view.setBackgroundGradientColors(prop)
            }
            Prop("backgroundGradientAngle") { (view: ExpoSquircleView, prop: Float?) in
                if let angle = prop {
                    view.setBackgroundGradientAngle(angle)
                }
            }
            Prop("backgroundGradientCenter") { (view: ExpoSquircleView, prop: String?) in
                view.setBackgroundGradientCenter(prop)
            }
            Prop("backgroundGradientRadius") { (view: ExpoSquircleView, prop: Float?) in
                if let radius = prop {
                    view.setBackgroundGradientRadius(radius)
                }
            }
            Prop("backgroundGradientLocations") { (view: ExpoSquircleView, prop: String?) in
                view.setBackgroundGradientLocations(prop)
            }
            
            // Nouvelles propriétés pour les gradients de bordure
            Prop("borderGradientType") { (view: ExpoSquircleView, prop: String?) in
                view.setBorderGradientType(prop)
            }
            Prop("borderGradientColors") { (view: ExpoSquircleView, prop: String?) in
                view.setBorderGradientColors(prop)
            }
            Prop("borderGradientAngle") { (view: ExpoSquircleView, prop: Float?) in
                if let angle = prop {
                    view.setBorderGradientAngle(angle)
                }
            }
            Prop("borderGradientCenter") { (view: ExpoSquircleView, prop: String?) in
                view.setBorderGradientCenter(prop)
            }
            Prop("borderGradientRadius") { (view: ExpoSquircleView, prop: Float?) in
                if let radius = prop {
                    view.setBorderGradientRadius(radius)
                }
            }
            Prop("borderGradientLocations") { (view: ExpoSquircleView, prop: String?) in
                view.setBorderGradientLocations(prop)
            }
        }
    }
}
