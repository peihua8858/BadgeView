package com.fz.badgeview.compiler

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

abstract class AbstractKotlinBadgeMethod(protected val packageName: String, protected val processor: KspBadgeProcessor) :
    IKotlinBadgeMethod {
    protected fun isAssignable(classDeclaration: KSClassDeclaration, superClazz: String): Boolean {
        val child = classDeclaration.asStarProjectedType()
        val supper1 = processor.getClassDeclarationByName(superClazz)?.asStarProjectedType()
        return supper1 != null && supper1.isAssignableFrom(child)
    }
}

class KotlinBadgeMethodImpl(packageName: String, processor: KspBadgeProcessor) : AbstractKotlinBadgeMethod(packageName, processor) {
    override fun constructor(typeBuilder: TypeSpec.Builder, classDeclaration: KSClassDeclaration) {
        val contextType: TypeName = ClassName("android.content", "Context")
        val attributeSetType: TypeName = ClassName("android.util", "AttributeSet").copy(nullable = true)
        val constructorOne = FunSpec.constructorBuilder()
            .addModifiers(KModifier.PUBLIC)
            .addParameter("context", contextType)
            .callThisConstructor("context","null")
            .build()
        val constructorTwo = FunSpec.constructorBuilder()
            .addModifiers(KModifier.PUBLIC)
            .addParameter("context", contextType)
            .addParameter("attrs", attributeSetType)
            .callThisConstructor("context","attrs","0")
            .build()
        val constructorThreeBuilder = FunSpec.constructorBuilder()
            .addParameter("context", contextType)
            .addParameter("attrs", attributeSetType)
            .addParameter("defStyleAttr", Int::class)
        val initFun = CodeBlock.builder()
        if (isAssignable(classDeclaration, "android.widget.ImageView")
            || isAssignable(classDeclaration, "android.widget.RadioButton")
        ) {
            initFun.addStatement("mBadgeViewHelper =  BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightTop)")
        } else {
            initFun.addStatement(
                "mBadgeViewHelper =  BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightCenter)"
            )
        }
        typeBuilder.addFunction(constructorOne)
            .addFunction(constructorTwo)
            .primaryConstructor(constructorThreeBuilder.build())
            .addSuperclassConstructorParameter("context")
            .addSuperclassConstructorParameter("attrs")
            .addSuperclassConstructorParameter("defStyleAttr")
            .addInitializerBlock(initFun.build())

    }

    override fun showCirclePointBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("showCirclePointBadge")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addStatement("mBadgeViewHelper.showCirclePointBadge()")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun showTextBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("showTextBadge")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeText", String::class)
            .addStatement("mBadgeViewHelper.showTextBadge(badgeText)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun hiddenBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("hiddenBadge")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addStatement("mBadgeViewHelper.hiddenBadge()")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun showDrawableBadge(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("showDrawableBadge")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("bitmap", ClassName("android.graphics", "Bitmap"))
            .addStatement("mBadgeViewHelper.showDrawable(bitmap)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun onTouchEvent(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("onTouchEvent")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("event", ClassName("android.view", "MotionEvent"))
            .addStatement("return mBadgeViewHelper.onTouchEvent(event)")
            .returns(Boolean::class)
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun callSuperOnTouchEvent(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("callSuperOnTouchEvent")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("event", ClassName("android.view", "MotionEvent"))
            .addStatement("return super.onTouchEvent(event)")
            .returns(Boolean::class)
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setDragDismissDelegate(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setDragDismissDelegate")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("delegate", ClassName(packageName, "DragDismissDelegate"))
            .addStatement("mBadgeViewHelper.setDragDismissDelegate(delegate)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun onDraw(typeBuilder: TypeSpec.Builder, classDeclaration: KSClassDeclaration) {
        if (isAssignable(classDeclaration, "android.view.ViewGroup")) {
            dispatchDraw(typeBuilder)
        } else {
            val methodSpec = FunSpec.builder("onDraw")
                .addModifiers(KModifier.OVERRIDE)
                .addModifiers(KModifier.PUBLIC)
                .addParameter("canvas", ClassName("android.graphics", "Canvas"))
                .addStatement("super.onDraw(canvas)")
                .addStatement("mBadgeViewHelper.drawBadge(canvas)")
                .build()
            typeBuilder.addFunction(methodSpec)
        }
    }

    override fun dispatchDraw(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("dispatchDraw")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("canvas", ClassName("android.graphics", "Canvas"))
            .addStatement("super.dispatchDraw(canvas)")
            .addStatement("mBadgeViewHelper.drawBadge(canvas)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun isShowBadge(typeBuilder: TypeSpec.Builder) {
        typeBuilder.addProperty(
            PropertySpec.builder("isShowBadge", Boolean::class)
                .addModifiers(KModifier.OVERRIDE)
                .addModifiers(KModifier.PUBLIC)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return mBadgeViewHelper.isShowBadge")
                        .build()
                )
                .build()
        )
    }

    override fun isDraggable(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("isDraggable")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addStatement("return mBadgeViewHelper.isDraggable")
            .returns(Boolean::class)
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun isDragging(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("isDragging")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addStatement("return mBadgeViewHelper.isDragging")
            .returns(Boolean::class)
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun getBadgeViewHelper(typeBuilder: TypeSpec.Builder) {
        typeBuilder.addProperty(
            PropertySpec.builder("badgeViewHelper", ClassName(packageName, "BadgeViewHelper"))
                .addModifiers(KModifier.OVERRIDE)
                .addModifiers(KModifier.PUBLIC)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return mBadgeViewHelper")
                        .build()
                )
                .build()
        )
    }

    override fun setBadgeBgColorInt(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeBgColorInt")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeBgColor", INT)
            .addStatement("mBadgeViewHelper.setBadgeBgColorInt(badgeBgColor)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgeTextColorInt(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeTextColorInt")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeTextColor", INT)
            .addStatement("mBadgeViewHelper.setBadgeTextColorInt(badgeTextColor)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgeTextSizeSp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeTextSizeSp")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeTextSize", INT)
            .addStatement("mBadgeViewHelper.setBadgeTextSizeSp(badgeTextSize)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgeVerticalMarginDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeVerticalMarginDp")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeVerticalMargin", INT)
            .addStatement("mBadgeViewHelper.setBadgeVerticalMarginDp(badgeVerticalMargin)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgeHorizontalMarginDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeHorizontalMarginDp")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeHorizontalMargin", INT)
            .addStatement("mBadgeViewHelper.setBadgeHorizontalMarginDp(badgeHorizontalMargin)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgePaddingDp(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgePaddingDp")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgePadding", INT)
            .addStatement("mBadgeViewHelper.setBadgePaddingDp(badgePadding)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setBadgeGravity(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setBadgeGravity")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("badgeGravity", ClassName(packageName, "BadgeViewHelper.BadgeGravity"))
            .addStatement("mBadgeViewHelper.setBadgeGravity(badgeGravity)")
            .build()
        typeBuilder.addFunction(methodSpec)
    }

    override fun setDraggable(typeBuilder: TypeSpec.Builder) {
        val methodSpec = FunSpec.builder("setDraggable")
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("draggable", BOOLEAN)
            .addStatement("mBadgeViewHelper.isDraggable = draggable")
            .build()
        typeBuilder.addFunction(methodSpec)
    }
}