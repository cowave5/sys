#! /bin/bash

pwd=$(cd "$(dirname "$0")" && pwd) || { echo "cd failed"; exit 1; }
if [ ! -f "$pwd/setenv.sh" ];then
    echo "can not find setenv.sh."
    exit 1
else
    source "$pwd/setenv.sh"
fi

java_commond=$(which java 2>/dev/null)
if [ -n "$java_home" ];then
    java_commond=$java_home/bin/java
fi
jps_commond=$(which jps 2>/dev/null)
if [ -n "$java_home" ];then
    jps_commond=$java_home/bin/jps
fi

[ -n "${app_name}" ] || { LogError "app_name not set"; exit 1; }
[ -n "${app_home}" ] || { LogError "app_home not set"; exit 1; }
[ -n "${app_version}" ] || { LogError "app_version not set"; exit 1; }

version(){
    $SETCOLOR_SUCCESS
    echo "$app_name: $app_version"
    $SETCOLOR_NORMAL

    echo "commit: $code_version"
    echo "build: $build_time"
    echo "install: $install_time"
}

status(){
    pid=$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')
    if [ -n "$pid" ];then
        $SETCOLOR_SUCCESS
        echo "$app_name Running[pid=$pid]."
        $SETCOLOR_NORMAL
    else
        $SETCOLOR_FAILURE
        echo "$app_name Dead."
        $SETCOLOR_NORMAL
    fi
    echo "last start time: $start_time"
}

up(){
    [[ $pwd = $app_home* ]] || { LogError "[run.sh up] can only run in $app_name home: $app_home"; exit 1; }
    s_time=$(date "+%Y-%m-%d %H:%M:%S")
    echo "## $s_time" >> "$app_home/log/boot.log"
    echo "$app_name $app_version($code_version) Copyright ©$build_time Cowave All Rights Reserved" | tee -a "$app_home/log/boot.log"
    java -version 2>&1 | tee -a "$app_home/log/boot.log" || { LogError "java not found"; exit 1; }

    setenv || { LogError "setenv failed"; exit 1; }
    config || { LogError "config failed"; exit 1; }

    jvm_option="$jvm_option -Duser.dir=$app_home -Dspring.config.location=$app_home/config/"
    if [ -d "$app_home/config/prod" ];then
        jvm_option="$jvm_option -Dspring.profiles.active=prod"
    fi
    if [ "on" = "$java_agent" ];then
        jvm_option="$jvm_option -javaagent:$app_home/lib/$app_name-$app_version.jar"
    fi
    echo "[Arguments]: $jvm_option" | tee -a "$app_home/log/boot.log"

    mkdir -p "$app_home/log"
    java $jvm_option -jar "$app_home/lib/$app_name-$app_version.jar"
}

waitUp(){
    declare -i counter=0
    declare -i max_counter=40 # 80s
    declare -i total_time=0

    SERVER_URL="http://localhost:$server_port"

    printf "waiting for %s startup..." "$app_name"
    until [[ (( counter -ge max_counter )) || "$(curl -X GET --silent --connect-timeout 1 --max-time 2 --head "$SERVER_URL" | grep "HTTP")" != "" ]];
    do
        printf "."
        counter+=1
        sleep 2
        if [ "$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')" == "" ];then
            printf "\n"
            LogError "$app_name start failed, see details in $app_home/log/boot.log"
            exit 1
        fi
    done
    printf "\n"

    total_time=$((counter * 2))
    if [[ (( counter -ge max_counter )) ]];then
        LogWarn "$app_name failed to start in $total_time seconds, see details in $app_home/log/boot.log"
        exit 1;
    fi

    s_time=$(date "+%Y-%m-%d %H:%M:%S")
    pid=$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')
    sed -i 's/export start_time=.*/export start_time="'"$s_time"'"/' "$app_home/bin/setenv.sh"
    echo -en "\\033[1;32m"
    LogInfo "$app_name started in ${total_time}sec, pid=$pid" | tee -a "$app_home/log/boot.log"
    echo -en "\\033[0;39m"
}

start(){
    [[ $pwd = $app_home* ]] || { LogError "[run.sh start] can only run in $app_name home: $app_home"; exit 1; }
    echo "$app_name $app_version($code_version) Copyright ©$build_time Cowave All Rights Reserved" | tee -a "$app_home/log/boot.log"

    [ -n "${java_commond}" ] || { LogError "java not found"; exit 1; }
    echo "[Effective java]: $java_commond" | tee -a "$app_home/log/boot.log"
    $java_commond -version 2>&1 | tee -a "$app_home/log/boot.log" || { LogError "java version failed"; exit 1; }
    setenv || { LogError "setenv failed"; exit 1; }
    config || { LogError "config failed"; exit 1; }

    jvm_option="$jvm_option -Duser.dir=$app_home -Dspring.config.location=$app_home/config/"
    if [ -d "$app_home/config/prod" ];then
        jvm_option="$jvm_option -Dspring.profiles.active=prod"
    fi
    if [ "on" = "$java_agent" ];then
        jvm_option="$jvm_option -javaagent:$app_home/lib/$app_name-$app_version.jar"
    fi
    echo "[Arguments]: $jvm_option" | tee -a "$app_home/log/boot.log"

    mkdir -p "$app_home/log"

    exec "$java_commond" $jvm_option -jar "$app_home/lib/$app_name-$app_version.jar" 1>> "$app_home/log/boot.log" 2>&1 &
    waitUp
}

waitKill(){
    declare -i counter=0
    declare -i max_counter=30 # 15s

    pid=$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')
    kill -15 "$pid"

    printf "waiting for %s shutdown..." "$app_name"
    until [[ (( counter -ge max_counter )) || "$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')" == "" ]];
    do
        printf "."
        counter+=1
        sleep 1
    done
    printf "\n"

    if [[ (( counter -ge max_counter )) ]];then
        kill -9 "$pid"
    fi
    echo -en "\\033[1;32m"
    LogInfo "$app_name stoped in ${counter}sec, pid=$pid" | tee -a "$app_home/log/boot.log"
    echo -en "\\033[0;39m"
}

stop(){
    [[ $pwd = $app_home* ]] || { LogError "[run.sh stop] can only run in $app_name home: $app_home"; exit 1; }
    pid=$($jps_commond -l | grep "$app_home/lib/$app_name-$app_version.jar" | awk '{print $1}')
    if [ -z "$pid" ];then
        LogWarn "$app_name was not running."
        exit
    fi
    waitKill
}

restart(){
    [[ $pwd = $app_home* ]] || { LogError "[run.sh restart] can only run in $app_name home: $app_home"; exit 1; }
    stop
    start
}

case "$1" in
    version)
        version
        ;;
    status)
        status
        ;;
    config)
        setenv
        config
        ;;
    up)
        up
        ;;
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    *)
    LogError $"usage: $0 {version|status|config|up|start|stop|restart}"
    exit 1
esac
exit 0