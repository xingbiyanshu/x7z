# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("a7z")

set(TARGET ${CMAKE_PROJECT_NAME})

MACRO(HEADER_DIRECTORIES return_list)
    FILE(GLOB_RECURSE new_list *.h)
    SET(dir_list "")
    FOREACH(file_path ${new_list})
        GET_FILENAME_COMPONENT(dir_path ${file_path} PATH)
        SET(dir_list ${dir_list} ${dir_path})
    ENDFOREACH()
    LIST(REMOVE_DUPLICATES dir_list)
    SET(${return_list} ${dir_list})
ENDMACRO()

function(get_all_directories result rootdir recursive)
    message("get_all_directories rootdir=${rootdir} recursive=${recursive}")
    set(all_directories)
    if (recursive EQUAL 1)
        message("recursive=1")
        file(GLOB FILES LIST_DIRECTORIES true "${rootdir}/*")
    else()
        message("recursive=0")
        file(GLOB FILES "${rootdir}/*")
    endif ()
    message("get_all_directories FILES=${FILES}")
    foreach(f ${FILES})
        if (IS_DIRECTORY ${f})
            list(APPEND all_directories ${f})
        endif()
    endforeach()
    set(${result} ${all_directories} PARENT_SCOPE)
endfunction()

function(get_file_list result dest_dirs file_exts)
    #message("collected_src_files=${${collected_src_files}} \nsrcdirs=${srcdirs} \nrecursive=${recursive} \nfile_exts=${file_exts}")
    set(file_list)
    foreach(dir ${dest_dirs})
        foreach(e ${file_exts})
            file(GLOB SRC_FILES "${dir}/*${e}")
            list(APPEND file_list ${SRC_FILES})
        endforeach()
    endforeach()
    set(${result} ${file_list} PARENT_SCOPE)
endfunction()

function(collect_src_files collected_src_files srcdirs recursive file_exts)
    #message("collected_src_files=${${collected_src_files}} \nsrcdirs=${srcdirs} \nrecursive=${recursive} \nfile_exts=${file_exts}")
    set(tmp_file_list)
    foreach(dir ${srcdirs})
        foreach(e ${file_exts})
            if (recursive EQUAL 1)
                file(GLOB_RECURSE SRC_FILES "${dir}/*${e}")
            else()
                file(GLOB SRC_FILES "${dir}/*${e}")
            endif()
            list(APPEND tmp_file_list ${SRC_FILES})
        endforeach()
    endforeach()
    set(${collected_src_files} ${${collected_src_files}} ${tmp_file_list} PARENT_SCOPE)
endfunction()

set(SRC_EXTS
        .c
#        .cpp
#        .cxx
#        .cc
#        .c++
)

set(SRC_FILE_LIST)

set(SRC_DIRS)

#get_all_directories(SRC_DIRS "${CMAKE_CURRENT_SOURCE_DIR}" 1)

message("SRC_DIRS=${SRC_DIRS}")

HEADER_DIRECTORIES(HEADER_DIRS)
#set(HEADER_DIRS ${SRC_DIRS})
message("HEADER_DIRS=${HEADER_DIRS}")

#get_file_list(SRC_FILE_LIST "${SRC_DIRS}" "${SRC_EXTS}")

collect_src_files(SRC_FILE_LIST "${SRC_DIRS}" 1 "${SRC_EXTS}")
message("SRC_FILE_LIST=${SRC_FILE_LIST}")


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
add_library(${TARGET} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        ${SRC_FILE_LIST})

target_include_directories(${TARGET} PUBLIC ${HEADER_DIRS})

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
#target_link_libraries(${CMAKE_PROJECT_NAME}
#        # List libraries link to the target library
#        android
#        log)