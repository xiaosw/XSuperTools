#!/bin/bash
src_suffix=".java"
doc_dir="DOC"
cmd_head="javadoc "$1" -d "$doc_dir" -author -version "
cmd_content=" "
cmd_footer=" -encoding UTF-8 -charset UTF-8"
cmd_space=" "
function scanDir() {
	cur_dir=$1
	ast_dir=""

    for dir in $(ls $cur_dir)
    do 
        if test -d ${dir};then
        	if [[  $dir == "build"
        			|| $dir == "libs"
        			|| $dir == "jni"
        			|| $dir == "res"
        			|| $dir == "androidTest"
        			|| $dir == "test"
                    || $dir == $doc_dir ]]; then
        		continue;
        	fi
        	cd ${dir}
            scanDir $(pwd)
            cd ..
        elif [[ $dir == *$src_suffix 
        		&& $dir != "R.java" 
        		&& $last_dir != $(pwd) ]]; then
            last_dir=$(pwd);
            echo ${last_dir}
            cmd_content=${cmd_content}${last_dir}"/*.java"${cmd_space}
        fi

    done

}

echo "##################### 准备生成文档 ######################"
scanDir $(pwd)
echo "####################### 准备完成 #######################"
echo
echo "##################### 开始生成文档 ######################"
cmd=${cmd_head}${cmd_content}${cmd_footer}
$cmd
echo "##################### 文档生成完成 ######################"
echo
echo "文档地址："$(pwd)/${doc_dir}""
echo