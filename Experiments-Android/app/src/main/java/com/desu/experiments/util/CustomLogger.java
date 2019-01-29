package com.desu.experiments.util;

public class CustomLogger {
    public static String getLinePointer() {
        try {
            StackTraceElement[] stackTraceElement = Thread.currentThread()
                    .getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("getLinePointer") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            String fullClassName = stackTraceElement[currentIndex].getClassName();
            String className = fullClassName.substring(fullClassName
                    .lastIndexOf("."));
            String methodName = stackTraceElement[currentIndex].getMethodName();
            String lineNumber = String
                    .valueOf(stackTraceElement[currentIndex].getLineNumber());
            return ("at " + fullClassName + "." + methodName + "("
                    + className + ".java:" + lineNumber + ")");
        } catch (Exception e) {
            e.printStackTrace();
            return("getLinePointer Error");
        }
    }
}
