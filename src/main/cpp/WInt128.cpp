#include "wideint_WInt128.h"
#include <jni.h>

union jbytear_to_i128 {
    jbyte j[wideint_WInt128_NUM_BYTES];
    __int128 i;
};

/*
 * Class:     wideint_WInt128
 * Method:    __natiev_i128_setl
 * Signature: ([BJ)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt128__1_1native_1i128_1setl(
    JNIEnv* env,
    jclass c,
    jbyteArray a_,
    jlong l_)
{
    jbytear_to_i128 a;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);

    a.i = l_;

    env->SetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_add
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt128__1_1native_1i128_1add(
    JNIEnv* env,
    jclass c,
    jbyteArray a_,
    jbyteArray b_)
{
    jbytear_to_i128 a;
    jbytear_to_i128 b;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
    env->GetByteArrayRegion(b_, 0, wideint_WInt128_NUM_BYTES, b.j);

    a.i += b.i;

    env->SetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_sub
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt128__1_1native_1i128_1sub(
    JNIEnv* env,
    jclass c,
    jbyteArray a_,
    jbyteArray b_)
{
    jbytear_to_i128 a;
    jbytear_to_i128 b;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
    env->GetByteArrayRegion(b_, 0, wideint_WInt128_NUM_BYTES, b.j);

    a.i -= b.i;

    env->SetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_mul
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt128__1_1native_1i128_1mul(
    JNIEnv* env,
    jclass c,
    jbyteArray a_,
    jbyteArray b_)
{
    jbytear_to_i128 a;
    jbytear_to_i128 b;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
    env->GetByteArrayRegion(b_, 0, wideint_WInt128_NUM_BYTES, b.j);

    a.i *= b.i;

    env->SetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_div
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL
Java_wideint_WInt128__1_1native_1i128_1div(
    JNIEnv* env,
    jclass c,
    jbyteArray a_,
    jbyteArray b_)
{
    jbytear_to_i128 a;
    jbytear_to_i128 b;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
    env->GetByteArrayRegion(b_, 0, wideint_WInt128_NUM_BYTES, b.j);

    a.i /= b.i;

    env->SetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);
}

/*
 * Class:     wideint_WInt
 * Method:    __native_to_long
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL
Java_wideint_WInt128__1_1native_1i128_1to_1long(
    JNIEnv* env,
    jclass c,
    jbyteArray a_)
{
    jbytear_to_i128 a;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);

    return a.i;
}

/*
 * Class:     wideint_WInt
 * Method:    __native_to_double
 * Signature: ([B)D
 */
JNIEXPORT jdouble JNICALL
Java_wideint_WInt128__1_1native_1i128_1to_1double(
    JNIEnv* env,
    jclass c,
    jbyteArray a_)
{
    jbytear_to_i128 a;
    env->GetByteArrayRegion(a_, 0, wideint_WInt128_NUM_BYTES, a.j);

    return a.i;
}
