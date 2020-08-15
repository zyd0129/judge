#!/bin/env bash
#
# jar部署通用脚本
# date: 20200715
# auth: 廖发友

DEBUG

jar_path="JAR_PATH"
jar_port="JAR_PROT"
start_cmd="START_CMD"
stop_max_wait_time=10
start_max_wait_time=60
Time=$(date "+%Y%m%d%H%M%S")

function check_environment() {
	local os_release=$(awk -F[=] '/ID_LIKE/{print $2}' /etc/os-release  | sed 's/"//g')
	if [[ ${os_release} == "debian" ]];then
		local INSTALL=apt-get
	else
		local INSTALL=yum
	fi
	if ( ! (type tcping > /dev/null 2>&1) );then
		echo "安装tcping"
		${INSTALL} install -y tcping
	fi
	if ( ! (type python > /dev/null 2>&1) && ! ( type python3 > /dev/null 2>&1 ) );then
		echo "系统没有安装python,开始安装python3"
		${INSTALL} -y python3
	fi
}

function stop_process() {
	local stop_status=0
	local stop_flag=0
	local path=${jar_path%/*}
	for (( count=0; count<${stop_max_wait_time}; count++ ));do
		local name=${jar_path##*/}
		local pid=$(ps -ef | grep java | fgrep ${name} | awk '{print $2}')
		if [[ ${pid} ]];then
			echo -e "开始停止进程\t-->\t${name}\tPID为\t${pid}"
			kill -9 ${pid}
			stop_flag=1
		else
			echo -e "进程\t${name}\t没有运行"
			break
		fi
		sleep 1
		if ( ! ( ps -ef | grep java | fgrep ${name} -q ) );then
			stop_status=1
			echo -e "进程\t${name}\t停止成功"
			break
		fi
	done
	if [[ ${stop_status} -eq 0 && ${stop_flag} -eq 1 ]];then
		echo -e "进程\t${name}\t停止失败"
		echo "输出nohup日志文件"
		cd ${path}
		cat nohup.out
		exit 1
	fi
}

function start_process() {
	local timestamp=$(date "+%s")
	local success=0
	local started_flag=0
	
	local path=${jar_path%/*}
	local name=${jar_path##*/}

	if [[ ! -d ${path} ]];then
		echo -e "目录不存在,新建目录 ${path}"
		mkdir -p ${path}
	fi
	cd ${path}
	if ( ls | grep ${name}_ -q );then
		echo -e "删除历史版本 $(ls ${name}_*)"
		rm -rf ${name}_*
	fi
	if [[ $(ls | grep -E "^${name}$" | wc -l) -eq 1 ]];then
		echo -e "备份\t${name}\t前一个版本"
		mv ${name} ${name}_${Time}
	fi

	mv /data/.tmp-gitlab-runner/${name} ${path}
	local cmd_array=()
	for i in ${start_cmd};do
		cmd_array+=(${i})
	done
	echo > ${path}/nohup.out
	echo -e "开始启动进程\t${name}"
	echo "进程启方式为: nohup ${cmd_array[@]} > nohup.out 2>&1 &"
	nohup ${cmd_array[@]} > nohup.out 2>&1 &

	sleep 5
#	for (( count=1; count<${start_max_wait_time}; count++ ));do
#		if [[ ${started_flag} -eq 0 ]];then
#			local time=$(date -d "@$(( ${timestamp} + ${count} ))" "+%Y-%m-%d %H:%M:%S")
#		fi
#		if ( fgrep "${time}" "${path}/logs/catalina" | fgrep "Started" -q );then
#			started_flag=1
#			echo -e "检查进程\t${name}\t端口 ${jar_port}是否可以访问"
#			if ( tcping 127.0.0.1 ${jar_port} | grep open -q );then
#				echo -e "进程\t${name}\t端口 ${jar_port}正常"
#				echo -e "进程\t${name}\t启动成功"
#				success=1
#				break
#			else
#				sleep 1
#			fi
#		else
#			sleep 1
#		fi
#	done
	echo -e "检查进程\t${name}\t端口 ${jar_port}是否可以访问"
	for (( count=1; count<${start_max_wait_time}; count++ ));do
		if ( ! ( ps -ef | grep java | fgrep "${name}" -q ) );then
			echo -e "\n进程\t${name}\t检测不存在"
			break
		fi
		local flag=''
		number=$(( ${count}%10 ))
		if ( tcping 127.0.0.1 ${jar_port} | grep open -q );then
			echo -e "\n进程\t${name}\t端口 ${jar_port}正常\n进程\t${name}\t启动成功"
			success=1
			break
		else
			sleep 1
			for ((i=0; i<=${number}; i++));do
				flag+=". "
			done
	  	fi
		printf "端口检测中: 第%02d秒 %-20s\r" "${count}" "${flag% *}"
	done
#	if [[ ${started_flag} -eq 1 && ${success} -eq 0 ]];then
#		echo -e "进程\t${name}\t启动失败,开始结束进程..."
#		stop_process
#		exit 1
#	fi
	if [[ ${success} -eq 0 ]];then
		echo -e "进程\t${name}\t启动失败,开始结束进程..."
		stop_process
                echo "输出nohup日志文件"
                cd ${path}
                cat nohup.out
		exit 1
	fi
}

check_environment
stop_process
start_process

