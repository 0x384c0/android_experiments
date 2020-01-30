#!/bin/sh
function banner {
    if [ -z "$TERM" ]; then >&2 echo "######## $1 #######";
    else >&2 echo "$(tput setaf 5; tput bold;)######## $1 #######$(tput sgr0)"; fi
}
function info {
    if [ -z "$TERM" ];then >&2 echo "INFO: $1";
    else >&2 echo "$(tput setaf 2; tput bold;)INFO: $1$(tput sgr0)"; fi
}
function warn {
    if [ -z "$TERM" ];then >&2 echo "WARNING: $1";
    else >&2 echo "$(tput setaf 3; tput bold;)WARNING: $1$(tput sgr0)"; fi
}
function error {
    if [ -z "$TERM" ];then >&2 echo "ERROR: $1";
    else >&2 echo "$(tput setaf 1; tput bold;)ERROR: $1$(tput sgr0)"; fi
}


set -e
VERSION_STR=$1
PROJECT_DIR=$2

semver=( ${VERSION_STR//./ } )
major="${semver[0]}"
minor="${semver[1]}"
patch="${semver[2]}"

BUNDLE_VERSION=$patch
SHORT_BUNDLE_VERSION="${major}.${minor}"
banner "Will set version for $PROJECT_DIR"
info "major:    $major"
info "minor:    $minor"
info "patch:    $patch"
info "BUNDLE_VERSION:        $BUNDLE_VERSION"
info "SHORT_BUNDLE_VERSION:  $SHORT_BUNDLE_VERSION"

if [[ $BUNDLE_VERSION = *[!\ ]* ]]; then
    #set new version
    sed  -i ".bak" -e "/    /s/versionName \"[^\"]*\"/versionName \"$VERSION_STR\"/" $PROJECT_DIR"build.gradle"
    #get versionCode
    versionCode=$(grep -Eo "    versionCode [0-9]+" $PROJECT_DIR"build.gradle" | grep -Eo "[0-9]+")
    versionCode=$((versionCode+1))
    #set new versionCode
    sed -i ".bak" -e "/    /s/versionCode [^\"]*/versionCode $versionCode/" $PROJECT_DIR"build.gradle"
    #cleanup
    rm -f $PROJECT_DIR"build.gradle.bak"
fi

banner DONE
