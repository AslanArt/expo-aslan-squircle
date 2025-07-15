package expo.modules.squircleview

import SquirclePath
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView

class ExpoSquircleView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var cornerSmoothing = 0
    private var borderColor = 0xFF000000.toInt()
    private var borderWidth = 0f
    private var backgroundColor = 0x00000000
    private var borderRadius = 0f
    private var preserveSmoothing = false

    private var borderGradientColors: IntArray? = null
    private var borderGradientAngle = 0f

    private var currentImageUri: String? = null
    private var backgroundImageResizeMode = "cover"
    private var backgroundImageOpacity = 1f
    private var backgroundBitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var shaderMatrix = Matrix()

    private val imageLoader = ImageLoader(context)

    init {
        mainPaint.style = Paint.Style.FILL
        borderPaint.style = Paint.Style.STROKE
        imagePaint.style = Paint.Style.FILL
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // --- TEST --- Forcer les valeurs pour le débogage
        val debugGradientColors = intArrayOf(Color.MAGENTA, Color.CYAN)
        val debugGradientAngle = 45f
        // --- FIN TEST ---


        // Dessiner le fond uni si nécessaire
        if (backgroundColor != 0) {
            mainPaint.shader = null
            mainPaint.color = backgroundColor
            mainPaint.alpha = 255
            canvas.drawPath(path, mainPaint)
        }

        // Dessiner l'image si disponible
        if (backgroundBitmap != null && bitmapShader != null) {
            updateShaderMatrix()
            imagePaint.shader = bitmapShader
            imagePaint.alpha = (backgroundImageOpacity * 255).toInt()
            canvas.drawPath(path, imagePaint)
        }

        // Dessiner la bordure si nécessaire
        if (borderWidth > 0) {
            if (debugGradientColors.size > 1) {
                val angleRad = Math.toRadians(debugGradientAngle.toDouble())
                val x0 = (width / 2 * (1 - Math.cos(angleRad))).toFloat()
                val y0 = (height / 2 * (1 - Math.sin(angleRad))).toFloat()
                val x1 = (width / 2 * (1 + Math.cos(angleRad))).toFloat()
                val y1 = (height / 2 * (1 + Math.sin(angleRad))).toFloat()

                val gradient = LinearGradient(x0, y0, x1, y1, debugGradientColors, null, Shader.TileMode.CLAMP)
                borderPaint.shader = gradient
            } else {
                borderPaint.shader = null
                borderPaint.color = borderColor
            }
            borderPaint.strokeWidth = Utils.convertDpToPixel(borderWidth, context).toFloat()
            canvas.drawPath(path, borderPaint)
        }
    }

    private fun updateShaderMatrix() {
        if (backgroundBitmap == null || bitmapShader == null) return

        val bitmapWidth = backgroundBitmap!!.width.toFloat()
        val bitmapHeight = backgroundBitmap!!.height.toFloat()
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        shaderMatrix.reset()

        val scale = when (backgroundImageResizeMode) {
            "cover" -> Math.max(viewWidth / bitmapWidth, viewHeight / bitmapHeight)
            "contain" -> Math.min(viewWidth / bitmapWidth, viewHeight / bitmapHeight)
            else -> Math.max(viewWidth / bitmapWidth, viewHeight / bitmapHeight)
        }

        // Calculer les translations pour centrer l'image
        val scaledWidth = bitmapWidth * scale
        val scaledHeight = bitmapHeight * scale
        val dx = (viewWidth - scaledWidth) * 0.5f
        val dy = (viewHeight - scaledHeight) * 0.5f
        
        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx, dy)
        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        resetSquirclePath(newWidth.toFloat(), newHeight.toFloat())
    }

    private fun resetSquirclePath(width: Float, height: Float) {
        if (width == 0f || height == 0f) {
            return
        }

        val pixelBorderWidth = Utils.convertDpToPixel(this.borderWidth, context)
        val halfBorder = pixelBorderWidth / 2f

        // Créer le path avec la taille ajustée pour la bordure
        val squirclePath = SquirclePath(
            width - pixelBorderWidth,
            height - pixelBorderWidth,
            borderRadius,
            cornerSmoothing / 100f,
            preserveSmoothing
        )

        path.reset()
        
        // Appliquer la translation pour centrer le path
        val matrix = Matrix()
        matrix.setTranslate(halfBorder, halfBorder)
        squirclePath.transform(matrix, path)
    }

    fun setCornerSmoothing(c: Int) {
        cornerSmoothing = c
        resetSquirclePath(width.toFloat(), height.toFloat())
        invalidate()
    }

    fun setBorderRadius(b: Float) {
        borderRadius = Utils.convertDpToPixel(b, context)
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

    fun setBorderGradientColors(colors: List<Int>?) {
        borderGradientColors = colors?.toIntArray()
        invalidate()
    }

    fun setBorderGradientAngle(angle: Float) {
        borderGradientAngle = angle
        invalidate()
    }

    fun setBackgroundImageSource(uri: String?) {
        Log.d("ExpoSquircleView", "setBackgroundImageSource called with URI: $uri")
        if (uri == null) {
            currentImageUri = null
            backgroundBitmap = null
            bitmapShader = null
            invalidate()
            return
        }

        if (uri == currentImageUri) {
            return
        }
        currentImageUri = uri

        val request = ImageRequest.Builder(context)
            .data(uri)
            .target(
                onSuccess = { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
                    backgroundBitmap = bitmap
                    if (backgroundBitmap != null) {
                        bitmapShader = BitmapShader(backgroundBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                        Log.d("ExpoSquircleView", "Image loaded successfully from URI: $uri")
                        invalidate() // Redraw the view with the new image
                    }
                },
                onError = {
                    backgroundBitmap = null
                    bitmapShader = null
                    Log.e("ExpoSquircleView", "Error loading image from URI: $uri")
                    invalidate()
                }
            )
            .build()
        imageLoader.enqueue(request)
    }

    fun setBackgroundImageResizeMode(mode: String) {
        if (backgroundImageResizeMode != mode) {
            backgroundImageResizeMode = mode
            invalidate()
        }
    }

    fun setBackgroundImageOpacity(opacity: Float) {
        if (backgroundImageOpacity != opacity) {
            backgroundImageOpacity = opacity
            invalidate()
        }
    }
}
