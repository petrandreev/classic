package cz.muni.fi.xharting.classic.util.reference;

import java.lang.reflect.Constructor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.SignatureAttribute.ClassSignature;
import javassist.bytecode.SignatureAttribute.ClassType;
import javassist.bytecode.SignatureAttribute.TypeArgument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Javassist utilities for <code>DirectReferenceHolder</code>.
 *
 * @author pan
 *
 */
public abstract class DirectReferenceUtils {

    private static final Logger log = LoggerFactory.getLogger(DirectReferenceUtils.class);

    private static ClassPool CLASS_POOL = ClassPool.getDefault();

    static {
        CLASS_POOL.appendClassPath(new LoaderClassPath(DirectReferenceFactory.class.getClassLoader()));
    }

    static <T> DirectReferenceHolderImpl<T> createDirectReferenceHolder(Class<DirectReferenceHolderImpl<T>> concreteClass, T initarg) {
        try {
            Constructor<DirectReferenceHolderImpl<T>> constructor = concreteClass.getDeclaredConstructor(Object.class);
            return constructor.newInstance(initarg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<DirectReferenceHolderImpl<T>> getOrCreateConcreteClass(Class<DirectReferenceHolderImpl<T>> parameterizedClass, Class<T> typeArgument) {
        String concreteClassName = parameterizedClass.getName() + typeArgument.getSimpleName();
        try {
            return (Class<DirectReferenceHolderImpl<T>>) Class.forName(concreteClassName);
        } catch (ClassNotFoundException e1) {
            try {
                CtClass cl = getOrCreateConcreteCtClass(parameterizedClass, typeArgument);
                return cl.toClass();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    static <T> CtClass getOrCreateConcreteCtClass(Class<DirectReferenceHolderImpl<T>> parameterizedClass, Class<T> typeArgument) throws NotFoundException, CannotCompileException {
        String concreteClassName = parameterizedClass.getName() + typeArgument.getSimpleName();
        CtClass concreteClass = CLASS_POOL.getOrNull(concreteClassName);
        if (concreteClass == null) {
            concreteClass = CLASS_POOL.makeClass(concreteClassName);
            concreteClass.setSuperclass(CLASS_POOL.get(parameterizedClass.getName()));
            ClassSignature classSignature =
                new ClassSignature(null, new ClassType(parameterizedClass.getName(), new TypeArgument[] { new TypeArgument(new ClassType(typeArgument.getName())) }), null);
            concreteClass.setGenericSignature(classSignature.encode());
        }
        return concreteClass;
    }
}
