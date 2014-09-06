import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class MainClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        if (className.startsWith("example/")) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(className.replace('/', '.'));
                for (CtMethod method : cc.getDeclaredMethods()) {
                    method.addLocalVariable("elapsedTime", CtClass.longType);
                    method.insertBefore("elapsedTime = System.nanoTime();");
                    method.insertAfter("new LoggerSingleton().get().log(elapsedTime, Thread.currentThread());");
                }
                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return byteCode;
    }
}
