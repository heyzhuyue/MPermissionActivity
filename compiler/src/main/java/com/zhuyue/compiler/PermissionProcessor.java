package com.zhuyue.compiler;

import com.google.auto.service.AutoService;
import com.zhuyue.annotation.PermissionDenied;
import com.zhuyue.annotation.PermissionGrant;
import com.zhuyue.annotation.ShowRequestPermissionRationale;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by win7 on 2017/3/24.
 */

@AutoService(ProxyInfo.class)
public class PermissionProcessor extends AbstractProcessor {
    private Messager messager;
    private Elements elementUtils;
    private Map<String, ProxyInfo> mProxyMap = new HashMap<String, ProxyInfo>();
    private Filer mFilerUtis;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        mFilerUtis = processingEnv.getFiler();
    }

    /**
     * 返回支持源码类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(PermissionDenied.class.getCanonicalName());
        supportTypes.add(PermissionGrant.class.getCanonicalName());
        supportTypes.add(ShowRequestPermissionRationale.class.getCanonicalName());
        return supportTypes;
    }

    /**
     * 返回支持的源码版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mProxyMap.clear();
        messager.printMessage(Diagnostic.Kind.NOTE, "process...");

        if (!processAnnotations(roundEnv, PermissionGrant.class)) return false;
        if (!processAnnotations(roundEnv, PermissionDenied.class)) return false;
        if (!processAnnotations(roundEnv, ShowRequestPermissionRationale.class)) return false;


        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        proxyInfo.getTypeElement(), e.getMessage());
            }

        }
        return true;
    }

    /**
     * 处理Annotations
     *
     * @param roundEnv
     * @param clazz
     * @return
     */
    private boolean processAnnotations(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(clazz)) {

            if (!checkMethodValid(annotatedElement, clazz)) return false;

            ExecutableElement annotatedMethod = (ExecutableElement) annotatedElement;
            //class type
            TypeElement classElement = (TypeElement) annotatedMethod.getEnclosingElement();
            //full class name
            String fqClassName = classElement.getQualifiedName().toString();

            ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo(elementUtils, classElement);
                mProxyMap.put(fqClassName, proxyInfo);
                proxyInfo.setTypeElement(classElement);
            }


            Annotation annotation = annotatedMethod.getAnnotation(clazz);
            if (annotation instanceof PermissionGrant) {
                int requestCode = ((PermissionGrant) annotation).value();
                proxyInfo.grantMethodMap.put(requestCode, annotatedMethod.getSimpleName().toString());
            } else if (annotation instanceof PermissionDenied) {
                int requestCode = ((PermissionDenied) annotation).value();
                proxyInfo.deniedMethodMap.put(requestCode, annotatedMethod.getSimpleName().toString());
            } else if (annotation instanceof ShowRequestPermissionRationale) {
                int requestCode = ((ShowRequestPermissionRationale) annotation).value();
                proxyInfo.rationaleMethodMap.put(requestCode, annotatedMethod.getSimpleName().toString());
            } else {
                error(annotatedElement, "%s not support .", clazz.getSimpleName());
                return false;
            }

        }

        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }

    /**
     * 检测方法合法性
     *
     * @param annotatedElement
     * @param clazz
     * @return
     */
    private boolean checkMethodValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.METHOD) {
            error(annotatedElement, "%s must be declared on method.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement) || ClassValidator.isAbstract(annotatedElement)) {
            error(annotatedElement, "%s() must can not be abstract or private.", annotatedElement.getSimpleName());
            return false;
        }

        return true;
    }
}
