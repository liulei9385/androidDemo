#include <include/png.h>
#include <stdlib.h>
#include <android/log.h>
#include <errno.h>
#include <jni.h>

jstring chartojstring(JNIEnv* env, const char* pat) {
	return (*env)->NewStringUTF(env, pat);
}

void get_version(char* ret) {
	unsigned long versionNum = (unsigned long) png_access_version_number();
	const char * version = png_get_header_version(NULL);
	sprintf(ret, "Version Number: %lu \n %s", versionNum, version);
}

jstring Java_com_hello_png_PngActivity_getVersion(JNIEnv* env, jobject thiz) {
	char versionInfo[1000];
	get_version(versionInfo);
	return chartojstring(env, versionInfo);
}

void logcat(char* str) {
	__android_log_print(ANDROID_LOG_INFO, "MyLog", str, NULL);
}

void set_palette(png_colorp palette, int type) {
	if (type == 0) {
		int i;
		for (i = 0; i < PNG_MAX_PALETTE_LENGTH; ++i) {
			palette[i].red = palette[i].green = palette[i].blue = i;
		}
	}
	if (type == 1) {
		int i;
		for (i = 0; i < PNG_MAX_PALETTE_LENGTH; ++i) {
			palette[i].red = 255 * i / PNG_MAX_PALETTE_LENGTH;
			palette[i].green = 0;
			palette[i].blue = 0;
		}
	}
	if (type == 2) {
		int i;
		for (i = 0; i < PNG_MAX_PALETTE_LENGTH; ++i) {
			palette[i].red = 255 * i / PNG_MAX_PALETTE_LENGTH;
			palette[i].green = 0;
			palette[i].blue = 255 - 255 * i / PNG_MAX_PALETTE_LENGTH;
		}
	}
}

void generatePNG(char* filename, int type) {
	logcat(filename);

	// Define the variables.
	FILE *fp;
	png_structp png_ptr;
	png_infop info_ptr;
	png_colorp palette;
	logcat("1");
	// Open the file.
	fp = fopen(filename, "wb");
	logcat("2");

	// Init png struct.
	png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	info_ptr = png_create_info_struct(png_ptr);

	if (setjmp(png_jmpbuf(png_ptr))) {
		// . . .
	}
	png_init_io(png_ptr, fp);

	const int width = 120;
	const int height = 512;
	png_set_IHDR(png_ptr, info_ptr, width, height, 8, PNG_COLOR_TYPE_PALETTE,
			PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_BASE,
			PNG_FILTER_TYPE_BASE);

	palette = (png_colorp) png_malloc(png_ptr,
			PNG_MAX_PALETTE_LENGTH * sizeof(png_color));
	// set_palette(palette, RED_BLACK);
	set_palette(palette, type);

	png_set_PLTE(png_ptr, info_ptr, palette, PNG_MAX_PALETTE_LENGTH);
	png_write_info(png_ptr, info_ptr);

	png_uint_32 k;
	png_byte image[height][width];
	png_bytep row_pointers[height];
	for (k = 0; k < height; k++) {
		memset(image[k], k / 2, width);
		row_pointers[k] = image[k];
	}

	png_write_image(png_ptr, row_pointers);

	png_write_end(png_ptr, info_ptr);
	png_free(png_ptr, palette);
	png_destroy_write_struct(&png_ptr, &info_ptr);
	fclose(fp);
}

char* jstringTostring(JNIEnv* env, jstring jstr) {
	char* rtn = NULL;
	jclass clsstring = (*env)->FindClass(env, "java/lang/String");
	jstring strencode = (*env)->NewStringUTF(env, "utf-8");
	jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid,
			strencode);
	jsize alen = (*env)->GetArrayLength(env, barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	return rtn;
}

void Java_com_example_libpng_MainActivity_generateJniPng(JNIEnv* env,
		jobject thiz, jstring filename, jint type) {
	generatePNG(jstringTostring(env, filename), type);
}
