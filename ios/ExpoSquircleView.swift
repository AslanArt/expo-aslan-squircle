import UIKit
import ExpoModulesCore
import PocketSVG

class ExpoSquircleView: ExpoView {
    let squircleLayer = CAShapeLayer()
    var backgroundGradientLayer: CAGradientLayer?
    var borderGradientLayer: CAGradientLayer?
    var imageLayer: CALayer?
    
    var radius: CGFloat = 0
    var cornerSmoothing: CGFloat = 0
    var preserveSmoothing: Bool = false
    var isAnimationEnabled: Bool = false
    
    // Variables pour les gradients
    private var backgroundGradientType: String?
    private var backgroundGradientColors: [CGColor]?
    private var backgroundGradientAngle: CGFloat = 0
    private var backgroundGradientCenter: CGPoint?
    private var backgroundGradientRadius: CGFloat = 0.5
    private var backgroundGradientLocations: [NSNumber]?
    
    private var borderGradientType: String?
    private var borderGradientColors: [CGColor]?
    private var borderGradientAngle: CGFloat = 0
    private var borderGradientCenter: CGPoint?
    private var borderGradientRadius: CGFloat = 0.5
    private var borderGradientLocations: [NSNumber]?
    
    // Variables pour les images
    private var backgroundImage: UIImage?
    private var backgroundImageResizeMode: String = "cover"
    private var backgroundImagePosition: String = "center"
    private var backgroundImageOpacity: CGFloat = 1.0
    
    required init(appContext: AppContext? = nil) {
        super.init(appContext: appContext)
        setupSquircleLayer()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupSquircleLayer() {
        layer.addSublayer(squircleLayer)
        squircleLayer.fillRule = .evenOdd
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        updateLayers()
    }
    
    private func updateLayers() {
        let squirclePath = createSquirclePath()
        squircleLayer.path = squirclePath
        squircleLayer.frame = bounds
        
        // Mettre à jour les gradients de fond
        updateBackgroundGradient()
        
        // Mettre à jour l'image de fond
        updateBackgroundImage()
        
        // Mettre à jour les gradients de bordure
        updateBorderGradient()
    }
    
    private func createSquirclePath() -> CGPath {
        let width: CGFloat = bounds.width
        let height: CGFloat = bounds.height
        
        let path = SquirclePath.create(width: width - squircleLayer.lineWidth, height: height - squircleLayer.lineWidth, radius: radius, cornerSmoothing: cornerSmoothing / 100, preserveSmoothing: preserveSmoothing); // Support 0-200% (0.0-2.0)
        
        // if borderWidth is greater than 0, we need to shift the shape
        // to match the original width & height
        var translationTransform = CGAffineTransform(translationX: squircleLayer.lineWidth / 2, y: squircleLayer.lineWidth / 2)
        
        if let translatedPath = path.copy(using: &translationTransform) {
            return translatedPath
        }
        
        return path
    }
    
    private func updateBackgroundGradient() {
        backgroundGradientLayer?.removeFromSuperlayer()
        backgroundGradientLayer = nil
        
        guard let gradientType = backgroundGradientType,
              let colors = backgroundGradientColors else {
            return
        }
        
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = bounds
        gradientLayer.colors = colors
        
        if let locations = backgroundGradientLocations {
            gradientLayer.locations = locations
        }
        
        switch gradientType {
        case "linear":
            setupLinearGradient(gradientLayer, angle: backgroundGradientAngle)
        case "radial":
            setupRadialGradient(gradientLayer, center: backgroundGradientCenter, radius: backgroundGradientRadius)
        default:
            break
        }
        
        // Appliquer le masque squircle
        let maskLayer = CAShapeLayer()
        maskLayer.path = createSquirclePath()
        gradientLayer.mask = maskLayer
        
        layer.insertSublayer(gradientLayer, below: squircleLayer)
        backgroundGradientLayer = gradientLayer
    }
    
    private func updateBackgroundImage() {
        imageLayer?.removeFromSuperlayer()
        imageLayer = nil
        
        guard let image = backgroundImage else {
            return
        }
        
        let imageLayerInstance = CALayer()
        imageLayerInstance.frame = bounds
        imageLayerInstance.contents = image.cgImage
        imageLayerInstance.opacity = Float(backgroundImageOpacity)
        
        // Appliquer le mode de redimensionnement
        switch backgroundImageResizeMode {
        case "cover":
            imageLayerInstance.contentsGravity = .resizeAspectFill
        case "contain":
            imageLayerInstance.contentsGravity = .resizeAspect
        case "stretch":
            imageLayerInstance.contentsGravity = .resize
        case "center":
            imageLayerInstance.contentsGravity = .center
        case "repeat":
            imageLayerInstance.contentsGravity = .resize
        default:
            imageLayerInstance.contentsGravity = .resizeAspectFill
        }
        
        // Appliquer le masque squircle
        let maskLayer = CAShapeLayer()
        maskLayer.path = createSquirclePath()
        imageLayerInstance.mask = maskLayer
        
        layer.insertSublayer(imageLayerInstance, below: squircleLayer)
        imageLayer = imageLayerInstance
    }
    
    private func updateBorderGradient() {
        borderGradientLayer?.removeFromSuperlayer()
        borderGradientLayer = nil
        
        guard let gradientType = borderGradientType,
              let colors = borderGradientColors,
              squircleLayer.lineWidth > 0 else {
            return
        }
        
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = bounds
        gradientLayer.colors = colors
        
        if let locations = borderGradientLocations {
            gradientLayer.locations = locations
        }
        
        switch gradientType {
        case "linear":
            setupLinearGradient(gradientLayer, angle: borderGradientAngle)
        case "radial":
            setupRadialGradient(gradientLayer, center: borderGradientCenter, radius: borderGradientRadius)
        default:
            break
        }
        
        // Créer un masque pour la bordure uniquement
        let borderMask = CAShapeLayer()
        borderMask.path = createSquirclePath()
        borderMask.fillColor = UIColor.clear.cgColor
        borderMask.strokeColor = UIColor.black.cgColor
        borderMask.lineWidth = squircleLayer.lineWidth
        gradientLayer.mask = borderMask
        
        layer.addSublayer(gradientLayer)
        borderGradientLayer = gradientLayer
    }
    
    private func setupLinearGradient(_ gradientLayer: CAGradientLayer, angle: CGFloat) {
        let angleRad = angle * .pi / 180
        let startPoint = CGPoint(x: 0.5 - cos(angleRad) * 0.5, y: 0.5 - sin(angleRad) * 0.5)
        let endPoint = CGPoint(x: 0.5 + cos(angleRad) * 0.5, y: 0.5 + sin(angleRad) * 0.5)
        
        gradientLayer.startPoint = startPoint
        gradientLayer.endPoint = endPoint
        gradientLayer.type = .axial
    }
    
    private func setupRadialGradient(_ gradientLayer: CAGradientLayer, center: CGPoint?, radius: CGFloat) {
        let centerPoint = center ?? CGPoint(x: 0.5, y: 0.5)
        gradientLayer.startPoint = centerPoint
        gradientLayer.endPoint = CGPoint(x: centerPoint.x + radius, y: centerPoint.y + radius)
        gradientLayer.type = .radial
    }
    
    func setBackgroundColor(_ backgroundColor: UIColor) {
        CATransaction.begin()
        CATransaction.setDisableActions(!isAnimationEnabled)
        squircleLayer.fillColor = backgroundColor.cgColor
        CATransaction.commit()
    }
    
    func setBorderColor(_ borderColor: UIColor) {
        CATransaction.begin()
        CATransaction.setDisableActions(!isAnimationEnabled)
        squircleLayer.strokeColor = borderColor.cgColor
        CATransaction.commit()
    }
    
    func setBorderWidth(_ borderRadius: CGFloat) {
        CATransaction.begin()
        CATransaction.setDisableActions(!isAnimationEnabled)
        squircleLayer.lineWidth = borderRadius
        CATransaction.commit()
        setNeedsLayout()
    }
    
    func setCornerSmoothing(_ cornerSmoothing: CGFloat) {
        self.cornerSmoothing = cornerSmoothing
        squircleLayer.setNeedsLayout()
        setNeedsLayout()
    }
    
    func setRadius(_ radius: CGFloat) {
        self.radius = radius
        squircleLayer.setNeedsLayout()
        setNeedsLayout()
    }
    
    func setPreserveSmoothing(_ preserveSmoothing: Bool) {
        self.preserveSmoothing = preserveSmoothing
        squircleLayer.setNeedsLayout()
        setNeedsLayout()
    }
    
    // Nouvelles méthodes pour les gradients de fond
    func setBackgroundGradientType(_ type: String?) {
        backgroundGradientType = type
        setNeedsLayout()
    }
    
    func setBackgroundGradientColors(_ colorsJson: String?) {
        guard let json = colorsJson,
              let data = json.data(using: .utf8),
              let colorValues = try? JSONSerialization.jsonObject(with: data) as? [Int] else {
            backgroundGradientColors = nil
            return
        }
        
        backgroundGradientColors = colorValues.map { value in
            let uiColor = UIColor(red: CGFloat((value >> 16) & 0xFF) / 255.0,
                                green: CGFloat((value >> 8) & 0xFF) / 255.0,
                                blue: CGFloat(value & 0xFF) / 255.0,
                                alpha: CGFloat((value >> 24) & 0xFF) / 255.0)
            return uiColor.cgColor
        }
        setNeedsLayout()
    }
    
    func setBackgroundGradientAngle(_ angle: Float) {
        backgroundGradientAngle = CGFloat(angle)
        setNeedsLayout()
    }
    
    func setBackgroundGradientCenter(_ centerJson: String?) {
        guard let json = centerJson,
              let data = json.data(using: .utf8),
              let centerValues = try? JSONSerialization.jsonObject(with: data) as? [Float],
              centerValues.count >= 2 else {
            backgroundGradientCenter = nil
            return
        }
        
        backgroundGradientCenter = CGPoint(x: CGFloat(centerValues[0]), y: CGFloat(centerValues[1]))
        setNeedsLayout()
    }
    
    func setBackgroundGradientRadius(_ radius: Float) {
        backgroundGradientRadius = CGFloat(radius)
        setNeedsLayout()
    }
    
    func setBackgroundGradientLocations(_ locationsJson: String?) {
        guard let json = locationsJson,
              let data = json.data(using: .utf8),
              let locationValues = try? JSONSerialization.jsonObject(with: data) as? [Float] else {
            backgroundGradientLocations = nil
            return
        }
        
        backgroundGradientLocations = locationValues.map { NSNumber(value: $0) }
        setNeedsLayout()
    }
    
    // Nouvelles méthodes pour les gradients de bordure
    func setBorderGradientType(_ type: String?) {
        borderGradientType = type
        setNeedsLayout()
    }
    
    func setBorderGradientColors(_ colorsJson: String?) {
        guard let json = colorsJson,
              let data = json.data(using: .utf8),
              let colorValues = try? JSONSerialization.jsonObject(with: data) as? [Int] else {
            borderGradientColors = nil
            return
        }
        
        borderGradientColors = colorValues.map { value in
            let uiColor = UIColor(red: CGFloat((value >> 16) & 0xFF) / 255.0,
                                green: CGFloat((value >> 8) & 0xFF) / 255.0,
                                blue: CGFloat(value & 0xFF) / 255.0,
                                alpha: CGFloat((value >> 24) & 0xFF) / 255.0)
            return uiColor.cgColor
        }
        setNeedsLayout()
    }
    
    func setBorderGradientAngle(_ angle: Float) {
        borderGradientAngle = CGFloat(angle)
        setNeedsLayout()
    }
    
    func setBorderGradientCenter(_ centerJson: String?) {
        guard let json = centerJson,
              let data = json.data(using: .utf8),
              let centerValues = try? JSONSerialization.jsonObject(with: data) as? [Float],
              centerValues.count >= 2 else {
            borderGradientCenter = nil
            return
        }
        
        borderGradientCenter = CGPoint(x: CGFloat(centerValues[0]), y: CGFloat(centerValues[1]))
        setNeedsLayout()
    }
    
    func setBorderGradientRadius(_ radius: Float) {
        borderGradientRadius = CGFloat(radius)
        setNeedsLayout()
    }
    
    func setBorderGradientLocations(_ locationsJson: String?) {
        guard let json = locationsJson,
              let data = json.data(using: .utf8),
              let locationValues = try? JSONSerialization.jsonObject(with: data) as? [Float] else {
            borderGradientLocations = nil
            return
        }
        
        borderGradientLocations = locationValues.map { NSNumber(value: $0) }
        setNeedsLayout()
    }
    
    // Nouvelles méthodes pour les images de fond
    func setBackgroundImageSource(_ source: Any?) {
        if let imageResource = source as? Int {
            // Image depuis les ressources du bundle
            backgroundImage = UIImage(named: String(imageResource))
        } else if let imageName = source as? String {
            // Image depuis le nom de fichier
            backgroundImage = UIImage(named: imageName)
        } else {
            backgroundImage = nil
        }
        setNeedsLayout()
    }
    
    func setBackgroundImageResizeMode(_ mode: String) {
        backgroundImageResizeMode = mode
        setNeedsLayout()
    }
    
    func setBackgroundImagePosition(_ position: String) {
        backgroundImagePosition = position
        setNeedsLayout()
    }
    
    func setBackgroundImageOpacity(_ opacity: Float) {
        backgroundImageOpacity = CGFloat(max(0, min(1, opacity)))
        setNeedsLayout()
    }
}

