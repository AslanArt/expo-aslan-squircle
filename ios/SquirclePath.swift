//
//  SquirclePath.swift
//  ExpoSquircleView
//
//  Created by Wadah Esam on 18/01/2024.
//

import Foundation
import PocketSVG

struct CurveProperties {
    var a: CGFloat
    var b: CGFloat
    var c: CGFloat
    var d: CGFloat
    var p: CGFloat
    var arcSectionLength: CGFloat
    var cornerRadius: CGFloat
}

struct SquirclePath {
    
    static func create(width: CGFloat, height: CGFloat, radius: CGFloat, cornerSmoothing: CGFloat, preserveSmoothing: Bool) -> CGPath {
        
        let checkedRadius = min(radius, width  / 2, height / 2)
        let checkedCornerSmoothing =  max(min(cornerSmoothing / 100, 1), 0)
        
        let curveProperties = calculateCurveProperties(cornerRadius: checkedRadius, cornerSmoothing: checkedCornerSmoothing, preserveSmoothing: preserveSmoothing, roundingAndSmoothingBudget: min(width , height) / 2)
        let stringPath = getSVGPathFromPathParams(width: width, height: height, curveProperties: curveProperties)
        let svgString = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 \(height) \(width)'><path d='\(stringPath)'/></svg>"
        let paths = SVGBezierPath.paths(fromSVGString: svgString)
        return paths[0].cgPath
    }
    
    static func getSVGPathFromPathParams(width: CGFloat, height: CGFloat, curveProperties: CurveProperties) -> String {
        let pathString = """
            M \(width - curveProperties.p) 0
            \(SquirclePath.getTopRightPath(curveProperties: curveProperties))
            L \(width) \(height - curveProperties.p)
            \(SquirclePath.getBottomRightPath(curveProperties: curveProperties))
            L \(curveProperties.p) \(height)
            \(SquirclePath.getBottomLeftPath(curveProperties: curveProperties))
            L 0 \(curveProperties.p)
            \(SquirclePath.getTopLeftPath(curveProperties: curveProperties))
            Z
            """
        
        return pathString.replacingOccurrences(of: "[\\t\\s\\n]+", with: " ", options: .regularExpression).trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    static func getTopLeftPath(curveProperties: CurveProperties) -> String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c 0 \(-curveProperties.a) 0 \(-(curveProperties.a + curveProperties.b)) \(curveProperties.d) \(-(curveProperties.a + curveProperties.b + curveProperties.c))
            a \(curveProperties.cornerRadius) \(curveProperties.cornerRadius) 0 0 1 \(curveProperties.arcSectionLength) -\(curveProperties.arcSectionLength)
            c \(curveProperties.c) \(-curveProperties.d)  \(curveProperties.b + curveProperties.c) \(-curveProperties.d) \(curveProperties.a + curveProperties.b + curveProperties.c) \(-curveProperties.d)
            """;
        }
        return "";
    }
    
    static func getBottomLeftPath(curveProperties: CurveProperties) -> String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c \(-curveProperties.a) 0 \(-(curveProperties.a + curveProperties.b)) 0 \(-(curveProperties.a + curveProperties.b + curveProperties.c)) \(-curveProperties.d)
            a \(curveProperties.cornerRadius) \(curveProperties.cornerRadius) 0 0 1 -\(curveProperties.arcSectionLength) -\(curveProperties.arcSectionLength)
            c \(-curveProperties.d) \(-curveProperties.c) \(-curveProperties.d) \(-(curveProperties.b + curveProperties.c)) \(-curveProperties.d) \(-(curveProperties.a + curveProperties.b + curveProperties.c))
            """;
        }
        return "";
    }
    
    static func getBottomRightPath(curveProperties: CurveProperties) -> String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c 0 \(curveProperties.a) 0 \(curveProperties.a + curveProperties.b) \(-curveProperties.d) \(curveProperties.a + curveProperties.b + curveProperties.c)
            a \(curveProperties.cornerRadius) \(curveProperties.cornerRadius) 0 0 1 -\(curveProperties.arcSectionLength) \(curveProperties.arcSectionLength)
            c \(-curveProperties.c) \(curveProperties.d) \(-(curveProperties.b + curveProperties.c)) \(curveProperties.d) \(-(curveProperties.a + curveProperties.b + curveProperties.c)) \(curveProperties.d)
            """;
        }
        return "";
    }
    
    static func getTopRightPath(curveProperties: CurveProperties) -> String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c \(curveProperties.a) 0 \(curveProperties.a + curveProperties.b) 0 \(curveProperties.a + curveProperties.b + curveProperties.c) \(curveProperties.d)
            a \(curveProperties.cornerRadius) \(curveProperties.cornerRadius) 0 0 1 \(curveProperties.arcSectionLength) \(curveProperties.arcSectionLength)
            c \(curveProperties.d) \(curveProperties.c) \(curveProperties.d) \(curveProperties.c + curveProperties.d) \(curveProperties.d) \(curveProperties.a + curveProperties.b + curveProperties.c)
            """;
        }
        return "";
    }
    
    
    static func calculateCurveProperties(cornerRadius: CGFloat, cornerSmoothing: CGFloat, preserveSmoothing: Bool, roundingAndSmoothingBudget: CGFloat) -> CurveProperties {
        // Support étendu pour corner smoothing jusqu'à 200% (2.0)
        let maxSmoothingSupported: CGFloat = 2.0
        let clampedSmoothing = min(cornerSmoothing, maxSmoothingSupported)
        
        var p = (1 + clampedSmoothing) * cornerRadius
        var cornerSmoothing = clampedSmoothing
        
        if !preserveSmoothing {
            let maxCornerSmoothing = min(roundingAndSmoothingBudget / cornerRadius - 1, maxSmoothingSupported)
            cornerSmoothing = min(cornerSmoothing, maxCornerSmoothing)
            p = min(p, roundingAndSmoothingBudget)
        }
        
        // Formule modifiée pour supporter des valeurs > 100%
        let arcMeasure: CGFloat
        if cornerSmoothing <= 1.0 {
            arcMeasure = 90 * (1 - cornerSmoothing)
        } else {
            // Pour > 100%, progression vers ultra-smooth
            let extraSmoothing = cornerSmoothing - 1.0
            let baseArc: CGFloat = 90 * (1 - 1.0) // 0 degrees pour 100%
            arcMeasure = baseArc - (extraSmoothing * 45) // Permet d'aller jusqu'à -45° pour 200%
        }
        
        let arcSectionLength: CGFloat
        if arcMeasure > 0 {
            arcSectionLength = sin(toRadians(arcMeasure / 2)) * cornerRadius * sqrt(2)
        } else {
            // Pour les valeurs négatives, formule spéciale pour ultra-smooth
            arcSectionLength = cornerRadius * sqrt(2) * (1 + abs(arcMeasure) / 90)
        }
        
        let angleAlpha = abs(90 - arcMeasure) / 2
        let p3ToP4Distance = cornerRadius * tan(toRadians(angleAlpha / 2))
        
        // Support étendu pour angleBeta avec smooth exagéré pour > 100%
        let angleBeta: CGFloat
        if cornerSmoothing <= 1.0 {
            angleBeta = 45 * cornerSmoothing
        } else {
            // Pour > 100%, progression jusqu'à 90° pour effet ultra-smooth
            let extraSmoothing = cornerSmoothing - 1.0
            angleBeta = 45 + (extraSmoothing * 45) // Va de 45° à 90° entre 100% et 200%
        }
        let c = p3ToP4Distance * cos(toRadians(angleBeta))
        let d = c * tan(toRadians(angleBeta))
        var b = (p - arcSectionLength - c - d) / 3
        var a = 2 * b
        
        if preserveSmoothing && p > roundingAndSmoothingBudget {
            let p1ToP3MaxDistance = roundingAndSmoothingBudget - d - arcSectionLength - c
            let minA = p1ToP3MaxDistance / 6
            let maxB = p1ToP3MaxDistance - minA
            b = min(b, maxB)
            a = p1ToP3MaxDistance - b
            p = min(p, roundingAndSmoothingBudget)
        }
        
        return CurveProperties(a: a, b: b, c: c, d: d, p: p, arcSectionLength: arcSectionLength, cornerRadius: cornerRadius)
    }
    
    static func toRadians(_ degrees: CGFloat) -> CGFloat {
        return degrees * .pi / 180
    }
}
