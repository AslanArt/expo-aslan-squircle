import android.graphics.Matrix
import android.graphics.Path
import androidx.core.graphics.PathParser
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

data class CurveProperties(
    var a: Float,
    var b: Float,
    var c: Float,
    var d: Float,
    var p: Float,
    var arcSectionLength: Float,
    var cornerRadius: Float
)

class SquirclePath(
    private var width: Float,
    private var height: Float,
    private var borderRadius: Float,
    private var cornerSmoothing: Float,
    private var preserveSmoothing: Boolean,
) : Path() {

    init {
        val checkedRadius = minOf(this.borderRadius, this.width / 2f, this.height / 2f)
        val checkedCornerSmoothing = maxOf(minOf(this.cornerSmoothing, 1f),0f)

        val curvedProperties = calculateCurveProperties(
            checkedRadius,
            checkedCornerSmoothing,
            this.preserveSmoothing,
            min(this.width , this.height) / 2
        );
        val path =
            PathParser.createPathFromPathData(
                getSVGPathFromPathParams(
                    this.width,
                    this.height,
                    curvedProperties
                )
            )

        this.addPath(path)
    }


    private fun getSVGPathFromPathParams(
        width: Float,
        height: Float,
        curveProperties: CurveProperties
    ): String {
        return """
                M ${width - curveProperties.p} 0 
                ${getTopRightPath(curveProperties)} 
                L $width ${height - curveProperties.p} 
                ${getBottomRightPath(curveProperties)} 
                L ${curveProperties.p} $height 
                ${getBottomLeftPath(curveProperties)}  
                L 0 ${curveProperties.p} 
                ${getTopLeftPath(curveProperties)}  
                Z""".trimIndent();
    }

    private fun getTopLeftPath(curveProperties: CurveProperties): String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c 0 ${-curveProperties.a} 0 ${-(curveProperties.a + curveProperties.b)} ${curveProperties.d} ${-(curveProperties.a + curveProperties.b + curveProperties.c)}
            a ${curveProperties.cornerRadius} ${curveProperties.cornerRadius} 0 0 1 ${curveProperties.arcSectionLength} -${curveProperties.arcSectionLength}
            c ${curveProperties.c} ${-curveProperties.d} ${curveProperties.b + curveProperties.c} ${-curveProperties.d} ${curveProperties.a + curveProperties.b + curveProperties.c} ${-curveProperties.d}
        """.trimIndent()
        }
        return ""
    }

    private fun getBottomLeftPath(curveProperties: CurveProperties): String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c ${-curveProperties.a} 0 ${-(curveProperties.a + curveProperties.b)} 0 ${-(curveProperties.a + curveProperties.b + curveProperties.c)} ${-curveProperties.d}
            a ${curveProperties.cornerRadius} ${curveProperties.cornerRadius} 0 0 1 -${curveProperties.arcSectionLength} -${curveProperties.arcSectionLength}
            c ${-curveProperties.d} ${-curveProperties.c} ${-curveProperties.d} ${-(curveProperties.b + curveProperties.c)} ${-curveProperties.d} ${-(curveProperties.a + curveProperties.b + curveProperties.c)}
        """.trimIndent()
        }
        return ""
    }

    private fun getBottomRightPath(curveProperties: CurveProperties): String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c 0 ${curveProperties.a} 0 ${curveProperties.a + curveProperties.b} ${-curveProperties.d} ${curveProperties.a + curveProperties.b + curveProperties.c}
            a ${curveProperties.cornerRadius} ${curveProperties.cornerRadius} 0 0 1 -${curveProperties.arcSectionLength} ${curveProperties.arcSectionLength}
            c ${-curveProperties.c} ${curveProperties.d} ${-(curveProperties.b + curveProperties.c)} ${curveProperties.d} ${-(curveProperties.a + curveProperties.b + curveProperties.c)} ${curveProperties.d}
        """.trimIndent()
        }
        return ""
    }

    private fun getTopRightPath(curveProperties: CurveProperties): String {
        if (curveProperties.cornerRadius >= 0) {
            return """
            c ${curveProperties.a} 0 ${curveProperties.a + curveProperties.b} 0 ${curveProperties.a + curveProperties.b + curveProperties.c} ${curveProperties.d}
            a ${curveProperties.cornerRadius} ${curveProperties.cornerRadius} 0 0 1 ${curveProperties.arcSectionLength} ${curveProperties.arcSectionLength}
            c ${curveProperties.d} ${curveProperties.c} ${curveProperties.d} ${curveProperties.c + curveProperties.d} ${curveProperties.d} ${curveProperties.a + curveProperties.b + curveProperties.c}
        """.trimIndent()
        }
        return ""
    }

    private fun calculateCurveProperties(
        cornerRadius: Float,
        cornerSmoothing: Float,
        preserveSmoothing: Boolean,
        roundingAndSmoothingBudget: Float
    ): CurveProperties {

        // Support étendu pour corner smoothing jusqu'à 200% (2.0)
        val maxSmoothingSupported = 2.0f
        val clampedSmoothing = minOf(cornerSmoothing, maxSmoothingSupported)
        
        var p = (1 + clampedSmoothing) * cornerRadius
        var cornerSmoothingVar = clampedSmoothing

        if (!preserveSmoothing) {
            val maxCornerSmoothing = minOf(roundingAndSmoothingBudget / cornerRadius - 1, maxSmoothingSupported)
            cornerSmoothingVar = minOf(cornerSmoothingVar, maxCornerSmoothing)
            p = minOf(p, roundingAndSmoothingBudget)
        }

        // Formule modifiée pour supporter des valeurs > 100%
        // Pour 0-100%: comportement normal
        // Pour 100-200%: curve extra-smooth avec transition progressive
        val arcMeasure = if (cornerSmoothingVar <= 1.0f) {
            90 * (1 - cornerSmoothingVar)
        } else {
            // Pour > 100%, on utilise une progression exponentielle pour un effet plus smooth
            val extraSmoothing = cornerSmoothingVar - 1.0f
            val baseArc = 90 * (1 - 1.0f) // 0 degrees pour 100%
            // Progression vers des valeurs négatives contrôlées pour ultra-smooth
            baseArc - (extraSmoothing * 45f) // Permet d'aller jusqu'à -45° pour 200%
        }
        
        val arcSectionLength = if (arcMeasure > 0) {
            sin(toRadians(arcMeasure / 2)) * cornerRadius * sqrt(2f)
        } else {
            // Pour les valeurs négatives, on utilise une formule spéciale
            cornerRadius * sqrt(2f) * (1 + kotlin.math.abs(arcMeasure) / 90f)
        }
        
        val angleAlpha = kotlin.math.abs(90 - arcMeasure) / 2
        val p3ToP4Distance = cornerRadius * tan(toRadians(angleAlpha / 2))
        
        // Support étendu pour angleBeta avec smooth exagéré pour > 100%
        val angleBeta = if (cornerSmoothingVar <= 1.0f) {
            45 * cornerSmoothingVar
        } else {
            // Pour > 100%, on augmente progressivement jusqu'à 90° pour un effet ultra-smooth
            val extraSmoothing = cornerSmoothingVar - 1.0f
            45f + (extraSmoothing * 45f) // Va de 45° à 90° entre 100% et 200%
        }
        val c = p3ToP4Distance * cos(toRadians(angleBeta))
        val d = c * tan(toRadians(angleBeta))
        var b = (p - arcSectionLength - c - d) / 3
        var a = 2 * b

        if (preserveSmoothing && p > roundingAndSmoothingBudget) {
            val p1ToP3MaxDistance = roundingAndSmoothingBudget - d - arcSectionLength - c
            val minA = p1ToP3MaxDistance / 6
            val maxB = p1ToP3MaxDistance - minA
            b = minOf(b, maxB)
            a = p1ToP3MaxDistance - b
            p = minOf(p, roundingAndSmoothingBudget)
        }

        return CurveProperties(a, b, c, d, p, arcSectionLength, cornerRadius)
    }

    private fun toRadians(degrees: Float): Float {
        return degrees * (Math.PI.toFloat() / 180f)
    }
}