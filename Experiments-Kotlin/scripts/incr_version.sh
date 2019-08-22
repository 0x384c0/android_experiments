#!/bin/sh
function banner {
    if [ -z "$TERM" ]; then echo "######## $1 #######";
    else echo "$(tput setaf 5; tput bold;)######## $1 #######$(tput sgr0)"; fi
}
function info {
    if [ -z "$TERM" ];then echo "INFO: $1";
    else echo "$(tput setaf 2; tput bold;)INFO: $1$(tput sgr0)"; fi
}
function warn {
    if [ -z "$TERM" ];then echo "WARNING: $1";
    else echo "$(tput setaf 3; tput bold;)WARNING: $1$(tput sgr0)"; fi
}
function error {
    if [ -z "$TERM" ];then echo "ERROR: $1";
    else echo "$(tput setaf 1; tput bold;)ERROR: $1$(tput sgr0)"; fi
}

OLD_VERSION_STR=$1
version_to_increment=$2

banner "Will increment version"
#get version

#parse
semver=( ${OLD_VERSION_STR//./ } )
major="${semver[0]}"
minor="${semver[1]}"
patch="${semver[2]}"

#increment
if [ "$version_to_increment" = "minor" ]; then
minor=$((minor+1))
patch=0
elif [ "$version_to_increment" = "major" ]; then
major=$((major+1))
minor=0
patch=0
else
patch=$((patch+1))
fi

NEW_VERSION_STR="${major}.${minor}.${patch}"

info "Old version number: $OLD_VERSION_STR"
info "New version number: $NEW_VERSION_STR"

#save
export NEW_APP_VERSION=$NEW_VERSION_STR
banner DONE
