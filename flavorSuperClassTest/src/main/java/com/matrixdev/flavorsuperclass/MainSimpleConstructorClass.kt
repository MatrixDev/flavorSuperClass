package com.matrixdev.flavorsuperclass

/**
 * @author Rostyslav.Lesovyi
 */

@GenerateSuperClass(name = "SimpleConstructorClass", priority = 1)
open class MainSimpleConstructorClass {
	open val flavor: String = "main"
}

@GenerateSuperClass(name = "SimpleConstructorClass", priority = 2)
open class FlavorSimpleConstructorClass : MainSimpleConstructorClass() {
	override val flavor: String = "flavor"
}

//

@GenerateSuperClass(name = "ConstructorWithArgClass", priority = 1)
open class MainConstructorWithArgClass(val arg: String) {
	open val flavor: String = "main"
}

@GenerateSuperClass(name = "ConstructorWithArgClass", priority = 2)
open class FlavorConstructorWithArgClass(arg: String) : MainConstructorWithArgClass(arg) {
	override val flavor: String = "flavor"
}

//

@GenerateSuperClass(name = "ConstructorWithGenericClass", priority = 1)
open class MainConstructorWithGenericClass<T : Number>(val arg: T) {
	open val flavor: String = "main"
}

@GenerateSuperClass(name = "ConstructorWithGenericClass", priority = 2)
open class FlavorConstructorWithGenericClass<T : Number>(arg: T) : MainConstructorWithGenericClass<T>(arg) {
	override val flavor: String = "flavor"
}

//

//@GenerateSuperClass(name = "FinalClass", priority = 2)
//class MainFinalClass
