function banner {
    if [ -z "$TERM" ]; then echo "######## $1 #######";
    else echo "$(tput setaf 5; tput bold;)######## $1 #######$(tput sgr0)"; fi
}
function info {
    if [ -z "$TERM" ];then echo "INFO: $1";
    else echo "$(tput setaf 2; tput bold;)INFO: $1$(tput sgr0)"; fi
}

set -e
VERSION_STR=$1
PROJECT="Experiments-Android"

semver=( ${VERSION_STR//./ } )
major="${semver[0]}"
minor="${semver[1]}"
patch="${semver[2]}"

BUNDLE_VERSION=$patch
SHORT_BUNDLE_VERSION="${major}.${minor}"
banner "Will set version for $PROJECT"
info "major:    $major"
info "minor:    $minor"
info "patch:    $patch"
info "BUNDLE_VERSION:        $BUNDLE_VERSION"
info "SHORT_BUNDLE_VERSION:  $SHORT_BUNDLE_VERSION"

if [[ $BUNDLE_VERSION = *[!\ ]* ]]; then

    sed  -i bak -e "/    /s/versionName \"[^\"]*\"/versionName \"$VERSION_STR\"/" "CoreNetwork/build.gradle"
    sed  -i bak -e "/    /s/versionName \"[^\"]*\"/versionName \"$VERSION_STR\"/" "Application/app/build.gradle"

    versionCode=$(grep -Eo "    versionCode [0-9]+" CoreNetwork/build.gradle | grep -Eo "[0-9]+")
    versionCode=$((versionCode+1))

    sed -i bak -e "/    /s/versionCode [^\"]*/versionCode $versionCode/" "CoreNetwork/build.gradle"
    sed -i bak -e "/    /s/versionCode [^\"]*/versionCode $versionCode/" "Application/app/build.gradle"
    
fi

banner DONE
