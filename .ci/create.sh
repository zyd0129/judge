#!/bin/env bash
#
# 解析.ci/config.json文件并根据.ci/deploy_jar.sh生成部署shell脚本
# 廖发友
# 20200707

config=".ci/config.json"
template=".ci/deployJar.sh"
upload_max_count=3

python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read());print(j[\"app\"]))"

if [[ $? -ne 0 ]];then
    echo -e "配置文件\t${config}\t解析错误"
    exit 1
fi

function deploy() {
    local app_number=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read());print(len(j[\"app\"]))")

        for (( app_count=0; app_count<${app_number}; app_count++ ));do
        local host_number=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding=\""utf-8\"");print(len(j[\"app\"][${app_count}][\"ProductionHost\"]))")
        local jar_name=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"JarName\"])")
        local production_path=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"ProductionPath\"])")
        local prefix_cmd=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"PrefixCmd\"])")
        local suffix_cmd=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"SuffixCmd\"])")
        local port=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"Port\"])")

        for text in host_number jar_name production_path prefix_cmd suffix_cmd port;do
            check "${!text}" "${text}" "${app_count}"
        done

        for (( count=0; count<${host_number}; count++ ));do
            local host_ip=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding=\""utf-8\"");print(j[\"app\"][${app_count}][\"ProductionHost\"][${count}])")
            local temp_name=${host_ip}_${jar_name}_${template}
            local start_cmd=${prefix_cmd} ${jar_name} ${suffix_cmd}
            echo -e "开始生成部署Shell文件, HOST:${$host_ip}\tJAR:${jar_name}\tSTART_CMD:${start_cmd}"
            cp ${template}  ${temp_name}
            sed -i "s#JAR_PATH#${production_path}/${jar_name}#" ${temp_name}
            sed -i "s#JAR_PROT#${port}#" ${temp_name}
            sed -i "s#START_CMD#${start_cmd}#" ${temp_name}
            for (( uploadCount=0; uploadCount<${upload_max_count}; uploadCount++ ));do
                echo -e "将文件 ${jar_name} 上传到 ${host_ip} 目录 /data/.tmp-gitlab-runner"
                scp -B -C ${HOME}/${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}/${jar_name} root@${host_ip}:/data/.tmp-gitlab-runner
                echo -e "${jar_name} 开始进行文件md5校验"
                remote_md5=$(ssh root@${host_ip} "cd /data/.tmp-gitlab-runner && md5sum ${jar_name}")
                if ( fgrep -q ${remote_md5} ${HOME}/${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}/md5.txt );then
                    echo "md5值相同"
                    break
                else
                    echo "暂停3秒后重新上传文件 ${jar_name}"
                    sleep 3
                fi
            done
            echo "上传部署Shell文件 ${temp_name} 到 ${host_ip} 目录 /data/.tmp-gitlab-runner"
            scp -B -C ${temp_name} root@${host_ip}:/data/.tmp-gitlab-runner
            echo "执行Shel脚本, 开始部署 ${jar_name}"
            ssh root@${host_ip} "cd /data/.tmp-gitlab-runner && sh ${temp_name}"
        done
    done
}

function build() {
    local app_number=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read());print(len(j[\"app\"]))")

    for (( app_count=0; app_count<${app_number}; app_count++ ));do
        local jar_name=$(python -c "import json;import sys;reload(sys);sys.setdefaultencoding('utf-8');f=open(\"${config}\");j=json.loads(f.read(),encoding='utf-8');print(j[\"app\"][${app_count}][\"JarName\"])")
        check "${jar_name}" "jar_name" "${app_count}"
        prefix_name=$(echo ${jar_name} | grep -oP '(.*)(?=-(\d+\.)+(.*?)jar)')
        echo -e "转存 ${jar_name} --> ${HOME}/${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}/${jar_name}"
        cp -rf ${prefix_name}/target/${jar_name} ${HOME}/${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}
    done
    cd ${HOME}/${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}
    for file in $(ls);do
        echo -e "生成文件\t${file}\t的md5值"
        md5sum $file >> md5.txt
    done
}

function check() {
    local status=$1
    local name=$2
    local index=$3
    if [[ ${status:-"err"} == "err" ]];then
        echo "解析json配置文件app项列表:${index}, 字段: ${name}失败"
        exit 1
    fi
}

if [[ ${$CI_JOB_STAGE} == "build" ]];then
    build
elif [[ ${$CI_JOB_STAGE} == "build" ]];then
	deploy
else
	echo "没有检索到相应的CI阶段"
	exit 1
fi

