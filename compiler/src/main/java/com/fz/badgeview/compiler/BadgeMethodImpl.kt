package com.fz.badgeview.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

/**
 * 类方法生成实现
 * @author dingpeihua
 * @date 2022/2/15 16:50
 * @version 1.0
 */
class BadgeMethodImpl(packageName:String,processingEnv: ProcessingEnvironment) :
    AbstractBadgeMethod(packageName,processingEnv) {
    override fun constructor(typeBuilder: TypeSpec.Builder, clazz: String) {
        val contextType: TypeName = ClassName.get("android.content", "Context")
        val attributeSetType: TypeName = ClassName.get("android.util", "AttributeSet")
        val constructorOne = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(contextType, "context")
            .addStatement("this(context, null)")
            .build()
        val constructorTwo = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(contextType, "context")
            .addParameter(attributeSetType, "attrs")
            .addStatement("this(context, attrs, 0)")
            .build()
        val constructorThreeBuilder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(contextType, "context")
            .addParameter(attributeSetType, "attrs")
            .addParameter(Int::class.javaPrimitiveType, "defStyleAttr")
            .addStatement("super(context, attrs, defStyleAttr)")
        if (isAssignable(clazz, "android.widget.ImageView")
            || isAssignable(clazz, "android.widget.RadioButton")
        ) {
            constructorThreeBuilder.addStatement("mBadgeViewHelper = new BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightTop)")
        } else {
            constructorThreeBuilder.addStatement(
                "mBadgeViewHelper = new BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightCenter)"
            )
        }
        typeBuilder.addMethod(constructorOne)
            .addMethod(constructorTwo)
            .addMethod(constructorThreeBuilder.build())
    }

    override fun showCirclePointBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("showCirclePointBadge")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("mBadgeViewHelper.showCirclePointBadge()")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun showTextBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("showTextBadge")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String::class.java, "badgeText")
            .addStatement("mBadgeViewHelper.showTextBadge(badgeText)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun hiddenBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("hiddenBadge")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("mBadgeViewHelper.hiddenBadge()")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun showDrawableBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("showDrawableBadge")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get("android.graphics", "Bitmap"), "bitmap")
            .addStatement("mBadgeViewHelper.showDrawable(bitmap)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun onTouchEvent(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("onTouchEvent")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get("android.view", "MotionEvent"), "event")
            .addStatement("return mBadgeViewHelper.onTouchEvent(event)")
            .returns(Boolean::class.javaPrimitiveType)
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun callSuperOnTouchEvent(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("callSuperOnTouchEvent")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get("android.view", "MotionEvent"), "event")
            .addStatement("return super.onTouchEvent(event)")
            .returns(Boolean::class.javaPrimitiveType)
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setDragDismissDelegate(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setDragDismissDelegate")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get(packageName, "DragDismissDelegate"), "delegate")
            .addStatement("mBadgeViewHelper.setDragDismissDelegate(delegate)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun onDraw(typeBuilder: TypeSpec.Builder, clazz: String) {
        if (isAssignable(clazz, "android.view.ViewGroup")) {
            dispatchDraw(typeBuilder)
        } else {
            val methodSpec = MethodSpec.methodBuilder("onDraw")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.graphics", "Canvas"), "canvas")
                .addStatement("super.onDraw(canvas)")
                .addStatement("mBadgeViewHelper.drawBadge(canvas)")
                .build()
            typeBuilder.addMethod(methodSpec)
        }
    }

    override fun dispatchDraw(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("dispatchDraw")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get("android.graphics", "Canvas"), "canvas")
            .addStatement("super.dispatchDraw(canvas)")
            .addStatement("mBadgeViewHelper.drawBadge(canvas)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun isShowBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("isShowBadge")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return mBadgeViewHelper.isShowBadge()")
            .returns(Boolean::class.javaPrimitiveType)
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun isDraggable(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("isDraggable")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return mBadgeViewHelper.isDraggable()")
            .returns(Boolean::class.javaPrimitiveType)
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun isDragging(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("isDragging")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return mBadgeViewHelper.isDragging()")
            .returns(Boolean::class.javaPrimitiveType)
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun getBadgeViewHelper(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("getBadgeViewHelper")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return mBadgeViewHelper")
            .returns(ClassName.get(packageName, "BadgeViewHelper"))
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeBgColorInt(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeBgColorInt")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgeBgColor")
            .addStatement("mBadgeViewHelper.setBadgeBgColorInt(badgeBgColor)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeTextColorInt(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeTextColorInt")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgeTextColor")
            .addStatement("mBadgeViewHelper.setBadgeTextColorInt(badgeTextColor)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeTextSizeSp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeTextSizeSp")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgeTextSize")
            .addStatement("mBadgeViewHelper.setBadgeTextSizeSp(badgeTextSize)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeVerticalMarginDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeVerticalMarginDp")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgeVerticalMargin")
            .addStatement("mBadgeViewHelper.setBadgeVerticalMarginDp(badgeVerticalMargin)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeHorizontalMarginDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeHorizontalMarginDp")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgeHorizontalMargin")
            .addStatement("mBadgeViewHelper.setBadgeHorizontalMarginDp(badgeHorizontalMargin)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgePaddingDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgePaddingDp")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.INT, "badgePadding")
            .addStatement("mBadgeViewHelper.setBadgePaddingDp(badgePadding)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setBadgeGravity(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setBadgeGravity")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get(packageName, "BadgeViewHelper.BadgeGravity"), "badgeGravity")
            .addStatement("mBadgeViewHelper.setBadgeGravity(badgeGravity)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }

    override fun setDraggable(typeBuilder: TypeSpec.Builder) {
        val methodSpec = MethodSpec.methodBuilder("setDraggable")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.BOOLEAN, "draggable")
            .addStatement("mBadgeViewHelper.setDraggable(draggable)")
            .build()
        typeBuilder.addMethod(methodSpec)
    }
}