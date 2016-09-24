#include "wideint_WInt.h"
#include <jni.h>

union jlongar_to_i128 {
    jlong j[2];
    __int128 i;
};

/*
 * Class:     wideint_WInt
 * Method:    __native_add
 * Signature: ([J[J)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt__1_1native_1add(
    JNIEnv* env,
    jclass c,
    jlongArray a_,
    jlongArray b_)
{
    jlongar_to_i128 a;
    jlongar_to_i128 b;
    env->GetLongArrayRegion(a_, 0, 2, a.j);
    env->GetLongArrayRegion(b_, 0, 2, b.j);

    a.i += b.i;

    env->SetLongArrayRegion(a_, 0, 2, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_sub
 * Signature: ([J[J)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt__1_1native_1sub(
    JNIEnv* env,
    jclass c,
    jlongArray a_,
    jlongArray b_)
{
    jlongar_to_i128 a;
    jlongar_to_i128 b;
    env->GetLongArrayRegion(a_, 0, 2, a.j);
    env->GetLongArrayRegion(b_, 0, 2, b.j);

    a.i -= b.i;

    env->SetLongArrayRegion(a_, 0, 2, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_mul
 * Signature: ([J[J)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt__1_1native_1mul(
    JNIEnv* env,
    jclass c,
    jlongArray a_,
    jlongArray b_)
{
    jlongar_to_i128 a;
    jlongar_to_i128 b;
    env->GetLongArrayRegion(a_, 0, 2, a.j);
    env->GetLongArrayRegion(b_, 0, 2, b.j);

    a.i *= b.i;

    env->SetLongArrayRegion(a_, 0, 2, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_div
 * Signature: ([J[J)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt__1_1native_1div(
    JNIEnv* env,
    jclass c,
    jlongArray a_,
    jlongArray b_)
{
    jlongar_to_i128 a;
    jlongar_to_i128 b;
    env->GetLongArrayRegion(a_, 0, 2, a.j);
    env->GetLongArrayRegion(b_, 0, 2, b.j);

    a.i /= b.i;

    env->SetLongArrayRegion(a_, 0, 2, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_to_long
 * Signature: ([J)J
 */
JNIEXPORT jlong JNICALL
Java_wideint_WInt__1_1native_1to_1long(
    JNIEnv* env,
    jclass c,
    jlongArray a_)
{
    jlongar_to_i128 a;
    env->GetLongArrayRegion(a_, 0, 2, a.j);

    return a.i;
}

/*
 * Class:     wideint_WInt
 * Method:    __native_to_double
 * Signature: ([J)D
 */
JNIEXPORT jdouble JNICALL
Java_wideint_WInt__1_1native_1to_1double(
    JNIEnv* env,
    jclass c,
    jlongArray a_)
{
    jlongar_to_i128 a;
    env->GetLongArrayRegion(a_, 0, 2, a.j);

    return a.i;
}
