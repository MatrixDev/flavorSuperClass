package com.matrixdev.flavorsuperclass;

import com.squareup.javapoet.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.*;

/**
 * @author Rostyslav.Lesovyi
 */
public class GenerateSuperClassProcessor extends AbstractProcessor {

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(GenerateSuperClass.class.getName());
	}

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		Map<String, ClassInfo> map = new HashMap<>();
		for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(GenerateSuperClass.class)) {
			if (!(annotatedElement instanceof TypeElement) || annotatedElement.getKind() != ElementKind.CLASS) {
				continue;
			}

			TypeElement element = (TypeElement) annotatedElement;
			GenerateSuperClass annotation = element.getAnnotation(GenerateSuperClass.class);
			String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
			String className = packageName + "." + annotation.name();

			ClassInfo info = map.get(className);
			if (info != null) {
				if (info.annotation.priority() == annotation.priority()) {
					TypeName type1 = TypeName.get(info.element.asType());
					TypeName type2 = TypeName.get(element.asType());
					throw new RuntimeException("identical priorities for " + type1 + " amd " + type2);
				}
				if (info.annotation.priority() > annotation.priority()) {
					continue;
				}
			}

			map.put(className, new ClassInfo(packageName, element, annotation));
		}

		for (ClassInfo info : map.values()) {
			JavaFile javaFile = JavaFile.builder(info.packageName, generateClass(info)).build();
			try {
				javaFile.writeTo(processingEnv.getFiler());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return true;
	}

	private TypeSpec generateClass(ClassInfo info) {
		if (info.element.getModifiers().contains(Modifier.FINAL)) {
			throw new RuntimeException("Class cannot be final: " + info.element.getQualifiedName());
		}
		if (info.element.getModifiers().contains(Modifier.PRIVATE)) {
			throw new RuntimeException("Class cannot be private: " + info.element.getQualifiedName());
		}

		TypeSpec.Builder builder = TypeSpec.classBuilder(info.annotation.name());

		builder.superclass(TypeName.get(info.element.asType()));

		for (TypeParameterElement element : info.element.getTypeParameters()) {
			builder.addTypeVariable(TypeVariableName.get(element));
		}

		boolean constructorAvailable = false;
		for (Element enclosedElement : info.element.getEnclosedElements()) {
			if (enclosedElement.getKind() != ElementKind.CONSTRUCTOR) {
				continue;
			}
			if (enclosedElement.getModifiers().contains(Modifier.PRIVATE)) {
				continue;
			}
			constructorAvailable = true;
			builder.addMethod(generateConstructor((ExecutableElement) enclosedElement));
		}

		if (!constructorAvailable) {
			throw new RuntimeException("At least one non-private constructor must be present in " + info.element.getQualifiedName());
		}

		return builder.build();
	}

	private MethodSpec generateConstructor(ExecutableElement subElement) {
		MethodSpec.Builder builder = MethodSpec.constructorBuilder();

		List<String> parameters = new ArrayList<>();
		for (Element element : subElement.getParameters()) {
			String name = element.getSimpleName().toString();
			TypeName type = TypeName.get(element.asType());

			parameters.add(name);
			builder.addParameter(type, name);
		}

		StringBuilder sb = new StringBuilder("super(");
		for (int index = 0; index < parameters.size(); ++index) {
			if (index > 0) {
				sb.append(",");
			}
			sb.append("$N");
		}
		sb.append(")");

		return builder.addStatement(sb.toString(), parameters.toArray()).build();
	}

	class ClassInfo {
		String packageName;
		TypeElement element;
		GenerateSuperClass annotation;

		ClassInfo(String packageName, TypeElement element, GenerateSuperClass annotation) {
			this.packageName = packageName;
			this.element = element;
			this.annotation = annotation;
		}
	}

}
