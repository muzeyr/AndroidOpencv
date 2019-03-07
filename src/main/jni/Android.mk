LOCAL_PATH := ..
include $(CLEAR_VARS)
OPENCV_INSTALL_MODULES:=on
include C:\ProgramData\MATLAB\SupportPackages\R2018b\3P.instrset\androidopencv.instrset\opencv-android-sdk\sdk\native\jni\OpenCV.mk
LOCAL_MODULE := cam_test1
LOCAL_CFLAGS += -DMODEL=cam_test1 -DNUMST=1 -DNCSTATES=0 -DHAVESTDIO -DMODEL_HAS_DYNAMICALLY_LOADED_SFCNS=0 -DCLASSIC_INTERFACE=0 -DALLOCATIONFCN=0 -DTID01EQ=0 -DTERMFCN=1 -DONESTEPFCN=1 -DMAT_FILE=0 -DMULTI_INSTANCE_CODE=0 -DINTEGER_CODE=0 -DMT=0 -DSTACK_SIZE=64 -D__MW_TARGET_USE_HARDWARE_RESOURCES_H__ -DRT -DPORTABLE_WORDSIZES 
LOCAL_SRC_FILES := ssprintf_rt.c vipdrawtext_main_rt.c ert_main.c cam_test1.c cam_test1_data.c rtGetInf.c rtGetNaN.c rt_nonfinite.c androidinitialize.c driver_android_camera.cpp driver_android_videodisplay.cpp 
LOCAL_C_INCLUDES += F:/Other/andr C:/PROGRA~3/MATLAB/SUPPOR~1/R2018b/toolbox/target/SUPPOR~1/android/include E:/MATLAB/R2018b/toolbox/shared/dsp/vision/matlab/include E:/MATLAB/R2018b/toolbox/vision/include E:/MATLAB/R2018b/toolbox/vision/visionrt/export/include E:/MATLAB/R2018b/toolbox/vision/visionrt/export/include/visionrt F:/Other/andr/cam_test1_ert_rtw E:/MATLAB/R2018b/extern/include E:/MATLAB/R2018b/simulink/include E:/MATLAB/R2018b/rtw/c/src E:/MATLAB/R2018b/rtw/c/src/ext_mode/common E:/MATLAB/R2018b/rtw/c/ert 
LOCAL_LDLIBS  +=  -llog -ldl
include $(BUILD_SHARED_LIBRARY)
