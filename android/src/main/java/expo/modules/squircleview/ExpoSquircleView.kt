package expo.modules.squircleview

import SquirclePath
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView
import org.json.JSONArray
import java.io.IOException
import kotlin.math.cos
import kotlin.math.sin

class ExpoSquircleView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(paint)
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var cornerSmoothing = 0
    private var borderColor = 0xFF000000.toInt()
    private var borderWidth = 0f
    private var backgroundColor = 0x00000000
    private var borderRadius = 0f
    private var preserveSmoothing = false

    // Variables pour les gradients
    private var backgroundGradientType: String? = null
    private var backgroundGradientColors: IntArray? = null
    private var backgroundGradientAngle: Float = 0f
    private var backgroundGradientCenter: FloatArray? = null
    private var backgroundGradientRadius: Float = 0.5f
    private var backgroundGradientLocations: FloatArray? = null
    
    private var borderGradientType: String? = null
    private var borderGradientColors: IntArray? = null
    private var borderGradientAngle: Float = 0f
    private var borderGradientCenter: FloatArray? = null
    private var borderGradientRadius: Float = 0.5f
    private var borderGradientLocations: FloatArray? = null

    // Variables pour les images de fond
    private var backgroundImageBitmap: Bitmap? = null
    private var backgroundImageResizeMode: String = "cover"
    private var backgroundImagePosition: String = "center"
    private var backgroundImageOpacity: Float = 1.0f

    init {
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Sauvegarder l'état du canvas pour le clipping
        canvas.save()
        canvas.clipPath(path)
        
        // Dessiner l'arrière-plan (couleur, gradient ou image)
        drawBackground(canvas)
        
        // Dessiner l'image de fond si définie
        drawBackgroundImage(canvas)
        
        // Restaurer l'état du canvas
        canvas.restore()

        // Dessiner la bordure
        drawBorder(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        if (backgroundGradientType != null && backgroundGradientColors != null) {
            // Dessiner avec gradient
            val shader = createGradientShader(
                backgroundGradientType!!,
                backgroundGradientColors!!,
                backgroundGradientAngle,
                backgroundGradientCenter,
                backgroundGradientRadius,
                backgroundGradientLocations
            )
            paint.shader = shader
        } else {
            // Dessiner avec couleur unie
            paint.color = backgroundColor
            paint.shader = null
        }
        
        canvas.drawPath(path, paint)
    }

    private fun drawBackgroundImage(canvas: Canvas) {
        backgroundImageBitmap?.let { bitmap ->
            imagePaint.alpha = (backgroundImageOpacity * 255).toInt()
            
            val pathBounds = RectF()
            path.computeBounds(pathBounds, true)
            
            val imageMatrix = calculateImageMatrix(bitmap, pathBounds)
            canvas.drawBitmap(bitmap, imageMatrix, imagePaint)
        }
    }

    private fun drawBorder(canvas: Canvas) {
        if (borderWidth > 0) {
            if (borderGradientType != null && borderGradientColors != null) {
                // Dessiner bordure avec gradient
                val shader = createGradientShader(
                    borderGradientType!!,
                    borderGradientColors!!,
                    borderGradientAngle,
                    borderGradientCenter,
                    borderGradientRadius,
                    borderGradientLocations
                )
                borderPaint.shader = shader
            } else {
                // Dessiner bordure avec couleur unie
                borderPaint.color = borderColor
                borderPaint.shader = null
            }
            
            borderPaint.style = Paint.Style.STROKE
            borderPaint.strokeWidth = Utils.convertDpToPixel(borderWidth, context).toFloat()
            canvas.drawPath(path, borderPaint)
        }
    }

    private fun createGradientShader(
        type: String,
        colors: IntArray,
        angle: Float,
        center: FloatArray?,
        radius: Float,
        locations: FloatArray?
    ): Shader {
        val bounds = RectF()
        path.computeBounds(bounds, true)
        
        return when (type) {
            "linear" -> {
                val angleRad = Math.toRadians(angle.toDouble())
                val centerX = bounds.centerX()
                val centerY = bounds.centerY()
                val length = kotlin.math.sqrt((bounds.width() * bounds.width() + bounds.height() * bounds.height()).toDouble()).toFloat() / 2
                
                val startX = centerX - cos(angleRad).toFloat() * length
                val startY = centerY - sin(angleRad).toFloat() * length
                val endX = centerX + cos(angleRad).toFloat() * length
                val endY = centerY + sin(angleRad).toFloat() * length
                
                LinearGradient(
                    startX, startY, endX, endY,
                    colors, locations,
                    Shader.TileMode.CLAMP
                )
            }
            "radial" -> {
                val centerX = if (center != null && center.size >= 2) {
                    bounds.left + bounds.width() * center[0]
                } else {
                    bounds.centerX()
                }
                val centerY = if (center != null && center.size >= 2) {
                    bounds.top + bounds.height() * center[1]
                } else {
                    bounds.centerY()
                }
                val gradientRadius = kotlin.math.min(bounds.width(), bounds.height()) * radius / 2
                
                RadialGradient(
                    centerX, centerY, gradientRadius,
                    colors, locations,
                    Shader.TileMode.CLAMP
                )
            }
            else -> {
                // Fallback vers gradient linéaire
                LinearGradient(
                    bounds.left, bounds.top, bounds.right, bounds.bottom,
                    colors, locations,
                    Shader.TileMode.CLAMP
                )
            }
        }
    }

    private fun calculateImageMatrix(bitmap: Bitmap, bounds: RectF): Matrix {
        val matrix = Matrix()
        
        when (backgroundImageResizeMode) {
            "cover" -> {
                val scaleX = bounds.width() / bitmap.width
                val scaleY = bounds.height() / bitmap.height
                val scale = kotlin.math.max(scaleX, scaleY)
                matrix.setScale(scale, scale)
                
                val scaledWidth = bitmap.width * scale
                val scaledHeight = bitmap.height * scale
                val dx = (bounds.width() - scaledWidth) / 2
                val dy = (bounds.height() - scaledHeight) / 2
                matrix.postTranslate(bounds.left + dx, bounds.top + dy)
            }
            "contain" -> {
                val scaleX = bounds.width() / bitmap.width
                val scaleY = bounds.height() / bitmap.height
                val scale = kotlin.math.min(scaleX, scaleY)
                matrix.setScale(scale, scale)
                
                val scaledWidth = bitmap.width * scale
                val scaledHeight = bitmap.height * scale
                val dx = (bounds.width() - scaledWidth) / 2
                val dy = (bounds.height() - scaledHeight) / 2
                matrix.postTranslate(bounds.left + dx, bounds.top + dy)
            }
            "stretch" -> {
                matrix.setScale(bounds.width() / bitmap.width, bounds.height() / bitmap.height)
                matrix.postTranslate(bounds.left, bounds.top)
            }
            "center" -> {
                val dx = (bounds.width() - bitmap.width) / 2
                val dy = (bounds.height() - bitmap.height) / 2
                matrix.setTranslate(bounds.left + dx, bounds.top + dy)
            }
            "repeat" -> {
                // Pour repeat, on utilise un shader au lieu d'une matrice
                matrix.setTranslate(bounds.left, bounds.top)
            }
        }
        
        return matrix
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        resetSquirclePath(newWidth.toFloat(), newHeight.toFloat())
    }

    private fun resetSquirclePath(
        width: Float,
        height: Float
    ) {
        if (width == 0f || height == 0f) {
            return
        }

        val pixelBorderWidth = Utils.convertDpToPixel(this.borderWidth, context);

        val squirclePath = SquirclePath(
            width - pixelBorderWidth,
            height - pixelBorderWidth,
            borderRadius,
            cornerSmoothing / 100f, // Maintenant support 0-200% (0.0-2.0)
            preserveSmoothing
        )

        // if borderWidth is greater than 0, we need to shift the shape
        // to match the original width & height
        val shiftX = pixelBorderWidth / 2f
        val shiftY = pixelBorderWidth / 2f
        val translationMatrix = Matrix().apply {
            setTranslate(shiftX, shiftY)
        }
        val translatedPath = Path().apply {
            squirclePath.transform(translationMatrix, this)
        }

        path.reset()
        path.addPath(translatedPath)
    }

    // Méthodes existantes
    fun setCornerSmoothing(c: Int) {
        cornerSmoothing = c
        resetSquirclePath(width.toFloat(), height.toFloat())
        invalidate()
    }

    fun setBorderRadius(b: Float) {
        val pixelRadius = Utils.convertDpToPixel(b, context)
        borderRadius = pixelRadius

        resetSquirclePath(width.toFloat(), height.toFloat())
        invalidate()
    }

    fun setPreserveSmoothing(p: Boolean) {
        preserveSmoothing = p

        resetSquirclePath(width.toFloat(), height.toFloat())
        invalidate()
    }

    fun setViewBackgroundColor(color: Int) {
        backgroundColor = color
        paint.color = backgroundColor
        invalidate()
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        invalidate()
    }

    fun setBorderWidth(width: Float) {
        borderWidth = width
        resetSquirclePath(this.width.toFloat(), this.height.toFloat())
        invalidate()
    }

    // Nouvelles méthodes pour les gradients de fond
    fun setBackgroundGradientType(type: String?) {
        backgroundGradientType = type
        invalidate()
    }

    fun setBackgroundGradientColors(colorsJson: String?) {
        backgroundGradientColors = parseColorsFromJson(colorsJson)
        invalidate()
    }

    fun setBackgroundGradientAngle(angle: Float) {
        backgroundGradientAngle = angle
        invalidate()
    }

    fun setBackgroundGradientCenter(centerJson: String?) {
        backgroundGradientCenter = parseFloatArrayFromJson(centerJson)
        invalidate()
    }

    fun setBackgroundGradientRadius(radius: Float) {
        backgroundGradientRadius = radius
        invalidate()
    }

    fun setBackgroundGradientLocations(locationsJson: String?) {
        backgroundGradientLocations = parseFloatArrayFromJson(locationsJson)
        invalidate()
    }

    // Nouvelles méthodes pour les gradients de bordure
    fun setBorderGradientType(type: String?) {
        borderGradientType = type
        invalidate()
    }

    fun setBorderGradientColors(colorsJson: String?) {
        borderGradientColors = parseColorsFromJson(colorsJson)
        invalidate()
    }

    fun setBorderGradientAngle(angle: Float) {
        borderGradientAngle = angle
        invalidate()
    }

    fun setBorderGradientCenter(centerJson: String?) {
        borderGradientCenter = parseFloatArrayFromJson(centerJson)
        invalidate()
    }

    fun setBorderGradientRadius(radius: Float) {
        borderGradientRadius = radius
        invalidate()
    }

    fun setBorderGradientLocations(locationsJson: String?) {
        borderGradientLocations = parseFloatArrayFromJson(locationsJson)
        invalidate()
    }

    // Nouvelles méthodes pour les images de fond
    fun setBackgroundImageSource(source: Any?) {
        when (source) {
            is Int -> {
                // Image depuis les ressources
                try {
                    val drawable = context.resources.getDrawable(source, null)
                    backgroundImageBitmap = (drawable as? BitmapDrawable)?.bitmap
                    invalidate()
                } catch (e: Exception) {
                    backgroundImageBitmap = null
                }
            }
            is String -> {
                // Image depuis URI (pas implémenté pour les images locales uniquement)
                backgroundImageBitmap = null
            }
            else -> {
                backgroundImageBitmap = null
            }
        }
    }

    fun setBackgroundImageResizeMode(mode: String) {
        backgroundImageResizeMode = mode
        invalidate()
    }

    fun setBackgroundImagePosition(position: String) {
        backgroundImagePosition = position
        invalidate()
    }

    fun setBackgroundImageOpacity(opacity: Float) {
        backgroundImageOpacity = opacity.coerceIn(0f, 1f)
        invalidate()
    }

    // Méthodes utilitaires
    private fun parseColorsFromJson(json: String?): IntArray? {
        if (json.isNullOrEmpty()) return null
        
        return try {
            val array = JSONArray(json)
            IntArray(array.length()) { i ->
                array.getInt(i)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseFloatArrayFromJson(json: String?): FloatArray? {
        if (json.isNullOrEmpty()) return null
        
        return try {
            val array = JSONArray(json)
            FloatArray(array.length()) { i ->
                array.getDouble(i).toFloat()
            }
        } catch (e: Exception) {
            null
        }
    }
}
