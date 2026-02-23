#include <jni.h>

#ifndef _Included_NativeSample
#define _Included_NativeSample
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_NativeSample_sayHello(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
