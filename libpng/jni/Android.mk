LOCAL_PATH := $(call my-dir)
  
include $(CLEAR_VARS)    
LOCAL_MODULE := png  
LOCAL_SRC_FILES := prebuild/libpng.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := test
LOCAL_SHARED_LIBRARIES := png
LOCAL_SRC_FILES := \
	pngtest.c
LOCAL_LDLIBS := -lz -llog
include $(BUILD_SHARED_LIBRARY)